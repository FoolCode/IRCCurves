package curves.main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import curves.message.IMessage;
import curves.message.NoticeMsg;
import curves.trigger.*;

public class MessageListener {

	Hashtable<Class<IMessage>, ArrayList<IReadHandler>> readHandlers;
	Hashtable<Class<IMessage>, ArrayList<IWriteHandler>> writeHandlers;
	ArrayList<ICloseHandler> closeHandlers;
	ArrayList<IPeriodicHandler> periodicHandlers;
	String indexClasspath;
	String indexClassname;
	String propertiesFile;
	Logger log = Logger.getLogger(MessageListener.class);

	Hashtable<String, Object> storage;
	boolean critical;

	public boolean isCritical() {
		return critical;
	}

	public MessageListener(String indexClasspath, String indexClassname,
			boolean critical, String propertiesFile) {
		this.indexClassname = indexClassname;
		this.indexClasspath = indexClasspath;
		this.critical = critical;
		this.propertiesFile = propertiesFile;
		load(propertiesFile);
	}

	@SuppressWarnings("unchecked")
	private void load(String propertiesFile) {
		ITriggerIndex index = loadIndex(indexClasspath, indexClassname);
		readHandlers = new Hashtable<Class<IMessage>, ArrayList<IReadHandler>>();
		writeHandlers = new Hashtable<Class<IMessage>, ArrayList<IWriteHandler>>();
		ArrayList<IReadHandler> readIndex = index.readHandlers();
		ArrayList<IWriteHandler> writeIndex = index.writeHandlers();

		log.debug("Loading R/W/P/C triggers from " + indexClassname);
		// differentiate read and write handlers by message type
		for (Class<IMessage> messageType : IMessage.getCommands()) {
			ArrayList<IReadHandler> readHandlerArray = new ArrayList<IReadHandler>();
			ArrayList<IWriteHandler> writeHandlerArray = new ArrayList<IWriteHandler>();
			if (readIndex != null)
				for (IReadHandler readHandler : readIndex) {
					if (readHandler.messageType(messageType)) {
						readHandlerArray.add(readHandler);
						log.debug(">> "
								+ readHandler.getClass().getName()
								+ " (R) <-> " + messageType.getName());
					}
				}
			if (writeIndex != null)
				for (IWriteHandler writeHandler : writeIndex) {
					if (writeHandler.messageType(messageType)) {
						writeHandlerArray.add(writeHandler);
						log.debug(">> "
								+ writeHandler.getClass().getName()
								+ " (W) <-> " + messageType.getName());
					}
				}
			if (readHandlerArray.size() > 0)
				readHandlers.put(messageType, readHandlerArray);
			if (writeHandlerArray.size() > 0)
				writeHandlers.put(messageType, writeHandlerArray);
		}

		closeHandlers = index.closeHandlers();
		periodicHandlers = index.periodicHandlers();

		if (storage == null)
			storage = new Hashtable<String, Object>();

		if (propertiesFile == null || propertiesFile.isEmpty())
			return;

		try {
			XMLConfiguration config = new XMLConfiguration(propertiesFile);
			log.debug("Loading properties from " + propertiesFile);
			if (config.getString("reset") != null) {
				log.debug(">> reset storage");
				storage.clear();
			}
			List<HierarchicalConfiguration> properties = config
					.configurationsAt("property");
			for (Iterator<HierarchicalConfiguration> ipro = properties
					.iterator(); ipro.hasNext();) {
				HierarchicalConfiguration property = ipro.next();
				storage.put(property.getString("name"), property
						.getString("value"));
				log.debug(">> " + property.getString("name") + " := "
						+ property.getString("value"));
			}
		} catch (Exception e) {
			log.error("Could not read config file.", e);
		}
	}

	public void read(final IMessage message, final Bot bot, final long serial) {
		if (critical || !bot.hasHighLoad()) {
			Thread run = new Thread() {
				public void run() {
					_read(message, bot, serial);
				}
			};
			run.start();
		}
	}

	/**
	 * put the actual write method in a Thread wrapper so it won't block
	 */
	public void write(final IMessage message, final Bot bot, final long serial) {
		if (critical || !bot.hasHighLoad()) {
			Thread run = new Thread() {
				public void run() {
					_write(message, bot, serial);
				}
			};
			run.start();
		}
	}

	/**
	 * put the actual close method in a Thread wrapper so it won't block
	 */
	public void close(final Bot bot) {
		Thread run = new Thread() {
			public void run() {
				_close(bot);
			}
		};
		run.start();
	}

	protected void _close(Bot bot) {
		if (closeHandlers == null)
			return;
		for (ICloseHandler closeHandler : closeHandlers) {
			if (closeHandler.reactsTo(bot, storage)) {
				log.debug("[C] invoking "
						+ closeHandler.getClass().getName());
				closeHandler.process(bot, storage);
			}
		}
	}

	protected void _read(IMessage message, Bot bot, long serial) {
		// reload message
		if (message instanceof NoticeMsg && message.isFromAdminOf(bot)
				&& ((NoticeMsg) message).getMessage().equals("reload triggers")) {
			log.info("Loading all triggers.");
			load(propertiesFile);
			return;
		}
		// check triggers one by one
		if (readHandlers == null
				|| !readHandlers.containsKey(message.getClass()))
			return;
		ArrayList<IReadHandler> readHandlerArray = readHandlers.get(message
				.getClass());
		for (IReadHandler readHandler : readHandlerArray) {
			if (readHandler.reactsTo(message, bot, storage)) {
				log.debug("[R " + serial + "] invoking "
						+ readHandler.getClass().getName());
				readHandler.process(message, bot, storage);
			}
		}
	}

	protected void _write(IMessage message, Bot bot, long serial) {
		if (writeHandlers == null
				|| !writeHandlers.containsKey(message.getClass()))
			return;
		ArrayList<IWriteHandler> writeHandlerArray = writeHandlers.get(message
				.getClass());
		for (IWriteHandler writeHandler : writeHandlerArray) {
			if (writeHandler.reactsTo(message, bot, storage)) {
				log.debug("[W " + serial + "] invoking "
						+ writeHandler.getClass().getName());
				writeHandler.process(message, bot, storage);
			}
		}
	}

	protected void periodic(Bot bot, long serial) {
		if (periodicHandlers == null)
			return;
		if (critical || !bot.hasHighLoad()) {
			for (IPeriodicHandler periodicHandler : periodicHandlers) {
				if (periodicHandler.isReady(bot, storage)) {
					log.debug("[P " + serial + "] invoking "
							+ periodicHandler.getClass().getName());
					periodicHandler.process(bot, storage);
				}
			}
		}
	}

	/**
	 * method to load a remote TriggerIndex with TriggerHandlers
	 * 
	 * @param classpath
	 * @param classname
	 * @return
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	public ITriggerIndex loadIndex(String classpath, String classname) {
		URL[] urls = null;
		try {
			// Convert the file object to a URL
			File dir = new File(classpath);
			URL url = dir.toURL();
			urls = new URL[] { url };
		} catch (MalformedURLException e) {
			log.error("Could not load index class.", e);
			return null;
		}
		try {
			// Create a new class loader with the directory
			ClassLoader cl = new URLClassLoader(urls);
			// Load in the class
			Class cls = cl.loadClass(classname);
			// Create a new instance of the new class
			return (ITriggerIndex) cls.newInstance();
		} catch (Exception e) {
			log.error("Could not load index class.", e);
			return null;
		}
	}
}
