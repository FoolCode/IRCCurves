package curves.trigger.fileserver;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.io.*;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import curves.main.Bot;
import curves.main.Profile;
import curves.message.PrivMsg;

public class DCCSend extends Thread {

	Logger log = Logger.getLogger(DCCSend.class);
	private int port;
	private int minPort;
	private int maxPort;
	private int localIP;
	private int bufferSize;
	private int serverTimeout;
	private int transferTimeout;

	private ServerSocket serverSocket;
	private Socket dataSocket;
	private BufferedOutputStream output;
	private BufferedInputStream input;

	private String CTCP;

	private Profile user;
	private Bot bot;
	private Hashtable<String, Object> storage;

	private BufferedInputStream infile;
	private File file;
	private String safeName;
	private int bandwidth;
	private int delay;
	private P_Queue transferQueue;

	public DCCSend(SendItem item, P_Queue transferQueue) {
		this.transferQueue = transferQueue;
		init(item.getBot(), item.getUser(), item.getStorage());
		if (!openFile(item.getFile())) {
			log.warn("Could not open file ("
					+ item.getFile().getAbsolutePath() + ")");
			return;
		}
		if (!openServerSocket()){
			log.warn("Could not open server socket ("
					+ item.getFile().getAbsolutePath() + ")");
			return;
		}
	}

	private void init(Bot bot, Profile user, Hashtable<String, Object> storage) {
		this.bot = bot;
		this.user = user;
		this.storage = storage;
		char CTCPchar = 1;
		this.CTCP = "" + CTCPchar + "";
		this.maxPort = Integer.parseInt((String) storage.get("max port"));
		this.minPort = Integer.parseInt((String) storage.get("min port"));
		this.bufferSize = Integer.parseInt((String) storage.get("buffer size"));
		this.bandwidth = Integer.parseInt((String) storage.get("bandwidth"));
		this.serverTimeout = Integer.parseInt((String) storage
				.get("server time out"));
		this.transferTimeout = Integer.parseInt((String) storage
				.get("transfer time out"));
		try {
			byte[] ipbytes = InetAddress.getLocalHost().getAddress();
			localIP = 16777216 * (ipbytes[0] & 0xFF) + 65536
					* (ipbytes[1] & 0xFF) + 256 * (ipbytes[2] & 0xFF)
					+ (ipbytes[3] & 0xFF);
		} catch (UnknownHostException e) {
			log.error("Unable to get own IP.", e);
		}

		this.delay = bandwidth / bufferSize;

	}

	private boolean openServerSocket() {
		serverSocket = null;
		boolean retry = true;
		port = minPort;
		while (retry && port <= maxPort) {
			retry = false;
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException ioe) {
				retry = true;
				port++;
			}
		}
		return !retry;
	}

	private boolean openFile(File file) {
		this.file = file;
		try {
			infile = new BufferedInputStream(new FileInputStream(file));
			return true;
		} catch (FileNotFoundException fnfe) {
			return false;
		}
	}

	public void run() {
		int transferred = 0;
		boolean completed = false;
		Date start = null;
		start = null;
		try {
			sendOffer();
			prepare();
			checkResume();

			byte[] outBuffer = new byte[bufferSize];
			byte[] inBuffer = new byte[4];
			int bytesRead = 0;
			int skip = 0;
			start = new Date();
			DCCTimeout dccTimeout = new DCCTimeout(transferTimeout, dataSocket);
			dccTimeout.start();
			while ((bytesRead = infile.read(outBuffer, 0, outBuffer.length)) != -1) {
				output.write(outBuffer, 0, bytesRead);
				output.flush();
				input.read(inBuffer, 0, inBuffer.length);
				transferred += bytesRead;
				dccTimeout.refresh();
				if ((skip = (skip + 1) % delay) == 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.error("Sleep failed.", e);
					}
				}
			}
			completed = true;

		} catch (IOException ioe) {
			log.info("Sending " + safeName + " to "
					+ user.getNickname() + " failed or was canceled.");
		}

		logDownload(start, transferred, completed);

		transferQueue.done(user);

	}

	private void sendOffer() {
		this.safeName = file.getName().replace(" ", "_");
		PrivMsg message = new PrivMsg(user.getNickname(), CTCP + "DCC SEND "
				+ safeName + " " + localIP + " " + port + " " + file.length()
				+ CTCP);
		bot.send(message);
		log.info("Offering " + file + " to " + user);
	}

	private void prepare() throws SocketException, IOException,
			FileNotFoundException {
		serverSocket.setSoTimeout(serverTimeout);
		dataSocket = serverSocket.accept();
		// dataSocket.setSoTimeout(timeOut);
		serverSocket.close();

		output = new BufferedOutputStream(dataSocket.getOutputStream());
		input = new BufferedInputStream(dataSocket.getInputStream());
		infile = new BufferedInputStream(new FileInputStream(file));
	}

	private void checkResume() throws IOException {
		// check if there has been a DCC RESUME
		int progress = 0;
		String id = user.toString() + " " + safeName + " " + port;
		if (storage.containsKey(id)) {
			progress = Integer.parseInt((String) storage.get(id));
			storage.remove(id);
		}

		log.info("Starting to send " + safeName + " to "
				+ user.getNickname() + " at position " + progress);
		if (progress > 0) {
			long bytesSkipped = 0;
			while (bytesSkipped < progress) {
				bytesSkipped += infile.skip(progress - bytesSkipped);
			}
		}
	}

	private void logDownload(Date started, int transferred, boolean completed) {
		if (started == null)
			return;
		try {
			PreparedStatement ps = (PreparedStatement) bot
					.getDB()
					.prepareStatement(
							"INSERT INTO downloads (nickname, hostname, started, ended, file, transferred, completed)"
									+ "VALUES (?, ?, ?, ?, ?, ?, ?);");
			ps.setString(1, user.getNickname());
			ps.setString(2, user.getHostname());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ps.setString(3, dateFormat.format(started));
			ps.setString(4, dateFormat.format(new Date()));
			String root = (String) storage.get("file root");
			ps.setString(5, file.getCanonicalPath().replace(root, ""));
			ps.setInt(6, transferred);
			ps.setBoolean(7, completed);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			log.error("Logging download to SQL failed", e);
		}

	}

}