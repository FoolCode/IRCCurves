package curves.main;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Connection;

import curves.message.*;

public class Bot extends Thread {
	Logger log = Logger.getLogger(Bot.class);
	String server;
	int port;
	Profile profile;
	ArrayList<Profile> admins;
	Socket socket;
	BufferedWriter out;
	BufferedReader in;
	ArrayList<MessageListener> listeners;
	Periodic periodic;
	Connection database;
	long periodicTimer;
	long writeSerial;
	long readSerial;
	boolean highLoad = false;

	public boolean hasHighLoad() {
		return highLoad;
	}

	public void setHighLoad(boolean highLoad) {
		this.highLoad = highLoad;
	}

	public Connection getDB() {
		return database;
	}
	
	public void reloadDB() {
		database = DatabaseFactory.New();
	}

	public Profile getProfile() {
		return profile;
	}

	public String getServer() {
		return server;
	}

	public int getPort() {
		return port;
	}

	public long getPeriodicTimer() {
		return periodicTimer;
	}

	public void setAdmins(ArrayList<Profile> admins) {
		this.admins = admins;
	}

	public Profile[] getAdmins() {
		Profile[] ret = new Profile[admins.size()];
		return admins.toArray(ret);
	}

	public void addListener(MessageListener listener) {
		listeners.add(listener);
	}

	public Bot(String server, int port, Profile profile, long periodicTimer,
			Connection dbConnection) {
		this.server = server;
		this.port = port;
		this.profile = profile;
		listeners = new ArrayList<MessageListener>();
		this.periodicTimer = periodicTimer;
		this.database = dbConnection;
	}

	public void send(IMessage message) {
		synchronized (out) {
			try {
				out.write(message + "\n");
				out.flush();
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).write(message, this, writeSerial);
				}
				writeSerial++;
			} catch (IOException e) {
				log.error("Sending message failed.", e);
			}
		}
	}

	public void run() {
		long backoffTimer = 0;

		while (true) {
			try {
				Thread.sleep(backoffTimer);
			} catch (InterruptedException e) {
				log.error("Sleep failed.", e);
			}
			backoffTimer = backoffTimer * 2;

			log.info("Bot connecting");

			// initialize
			periodic = new Periodic(listeners, periodicTimer, this);

			// open connection
			try {
				socket = new Socket(server, port);
				socket.setKeepAlive(true);
				out = new BufferedWriter(new OutputStreamWriter(socket
						.getOutputStream(), "UTF-8"));
				in = new BufferedReader(new InputStreamReader(socket
						.getInputStream(), "UTF-8"));
			} catch (Exception e) {
				log.error("Open connection failed.", e);
				close();
				continue;
			}

			// identify
			send(new NickMsg(profile));
			send(new UserMsg(profile));
			if (profile.getNickserv() != null && !profile.getNickserv().equals(""))
				send(new PrivMsg("NickServ", "IDENTIFY "
						+ profile.getNickserv()));

			for (Channel channel : getProfile().getChannels()) {
				send(new PrivMsg("ChanServ", "UNBAN " + channel));
				send(new JoinMsg(channel));
				send(new ModeMsg(channel, "+v", getProfile().getNickname()));
			}

			// receive and parse messages until input stream dies
			String currentLine;

			periodic.start();
			writeSerial = 0;
			readSerial = 0;
			backoffTimer = 10000;
			try {
				while ((currentLine = in.readLine()) != null) {
					IMessage message = IMessage.parseMessage(currentLine);
					for (MessageListener listener : listeners) {
						listener.read(message, this, readSerial);
					}
					readSerial++;
				}
			} catch (IOException e) {
				log.error("I/O exception while reading from socket.", e);
				close();
			}
		}
	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		} catch (Exception e) {
			log.error("Closing socket failed.", e);
		}
		socket = null;
		in = null;
		out = null;
		periodic.kill();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).close(this);
		}
	}
}
