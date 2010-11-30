package curves.main;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Thread that generates periodic calls independent of incoming messages.
 */
public class Periodic extends Thread {
	ArrayList<MessageListener> listeners;
	long sleepTimer;
	Bot bot;
	boolean running;
	
	Logger log = Logger.getLogger(Periodic.class);

	public Periodic(ArrayList<MessageListener> listeners, long sleepTimer,
			Bot bot) {
		this.listeners = listeners;
		this.sleepTimer = sleepTimer;
		this.bot = bot;
	}

	public void run() {
		running = true;
		long serial = 0;
		while (running) {
			try {
				Thread.sleep(sleepTimer);
			} catch (InterruptedException e) {
				log.error("Sleep failed.", e);
			}
			for (MessageListener listener : listeners) {
				if (running)
					listener.periodic(bot, serial);
			}
			serial++;
		}
	}

	public void kill() {
		running = false;
	}
}
