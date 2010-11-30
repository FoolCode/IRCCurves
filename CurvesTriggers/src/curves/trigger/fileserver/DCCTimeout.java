package curves.trigger.fileserver;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

public class DCCTimeout extends Thread {
	
	Socket socket;
	long timeout;
	long timer;
	Logger log = Logger.getLogger(DCCTimeout.class);
	
	public DCCTimeout(long timeout, Socket socket){
		this.socket = socket;
		this.timeout = timeout;
		refresh();
	}
	
	public void refresh(){
		this.timer = System.currentTimeMillis();
	}
	
	public void run(){
		while(System.currentTimeMillis() - timer < timeout){
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				log.error("Sleep failed.", e);
			}
		}
		try {
			socket.close();
			log.debug("socket timed out.");
		} catch (IOException e) {
			log.error("Closing socket failed.", e);
		}
	}
	
}
