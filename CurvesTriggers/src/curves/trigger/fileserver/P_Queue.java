package curves.trigger.fileserver;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.main.Profile;
import curves.trigger.IPeriodicHandler;

public class P_Queue implements IPeriodicHandler {

	Logger log = Logger.getLogger(P_Queue.class);
	private int queueLength;
	private Semaphore parallel;
	private HashSet<String> activeUsers;
	private ConcurrentLinkedQueue<String> mainQueue;
	private Hashtable<String, ArrayBlockingQueue<SendItem>> userReference;
	private int parallelSend;

	public P_Queue() {
		this.queueLength = 10;
		this.parallelSend = 6;
		this.parallel = new Semaphore(parallelSend);
		this.activeUsers = new HashSet<String>();
		this.mainQueue = new ConcurrentLinkedQueue<String>();
		this.userReference = new Hashtable<String, ArrayBlockingQueue<SendItem>>();
	}

	public synchronized void done(Profile user) {
		activeUsers.remove(user.getNickname());
		parallel.release();
		log.debug(user.getNickname() + " is done with a download.");
	}

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		return !mainQueue.isEmpty();
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		SendItem item;
		int loops = mainQueue.size();

		for (int i = 0; i < loops; i++) {

			if (!parallel.tryAcquire()) {
				// no slots free, get outta here.
				log.trace("All slots taken.");
				return;
			}

			// get new send item
			synchronized (this) {
				String username = mainQueue.poll();
				log.trace("Fetching download slot for " + username);

				// see if user is already downloading
				if (activeUsers.contains(username)) {
					// put user into the back
					log.trace(username + " is already downloading.");
					mainQueue.add(username);
					parallel.release();
					continue;
				}

				// get user queue
				ArrayBlockingQueue<SendItem> userQueue = userReference
						.get(username);

				// see if any user is waiting at all
				if (userQueue == null) {
					log.debug("Nobody is waiting for any download.");
					parallel = new Semaphore(parallelSend);
					activeUsers.clear();
					userReference.clear();
					return;
				}

				// user has nothing to download
				if (userQueue.isEmpty()) {
					log.debug(username + "'s queue is empty, next.");
					parallel.release();
					userReference.remove(username);
					continue;
				}

				// get send item
				item = userQueue.poll();

				// put user back to the end of the mainQueue
				log.trace(username
						+ " is put to the back of the queue.");
				mainQueue.add(username);

				activeUsers.add(username);

				new DCCSend(item, this).start();
			}
		}
	}

	public synchronized boolean add(SendItem newItem) {
		if (newItem == null)
			return false;

		ArrayBlockingQueue<SendItem> userQueue;
		boolean newQueue = !userReference.containsKey(newItem.getUser()
				.getNickname());
		// check whether user already has his own queue
		if (newQueue) {
			newQueue = true;
			// create new queue
			userQueue = new ArrayBlockingQueue<SendItem>(queueLength);
			// register into user reference
			userReference.put(newItem.getUser().getNickname(), userQueue);
			log.debug("New queue for " + newItem.getUser().getNickname()
					+ " created.");
		} else {
			userQueue = userReference.get(newItem.getUser().getNickname());
		}

		try {
			// too many items --> return false
			if (userQueue.size() >= queueLength)
				throw new Exception();
			if (userQueue.contains(newItem)){
				log.info(newItem.getFile().getName() + " is already in "
						+ newItem.getUser().getNickname() + "'s download queue.");
				return true;		
			}
			userQueue.add(newItem);
			log.info(newItem.getFile().getName() + " is added to "
					+ newItem.getUser().getNickname() + "'s download queue.");
		} catch (Exception e) {
			log.debug(newItem.getUser().getNickname()
					+ " has too many items in his queue.");
			return false;
		}

		if (newQueue) {
			// queue is new --> add to the end of the mainQueue
			mainQueue.add(newItem.getUser().getNickname());
		}
		return true;
	}
}
