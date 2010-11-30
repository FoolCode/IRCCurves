package curves.trigger.system;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.main.Channel;
import curves.message.ModeMsg;
import curves.trigger.IPeriodicHandler;

public class P_CheckLoad implements IPeriodicHandler {

	int skip = 0;
	Logger log = Logger.getLogger(P_CheckLoad.class);
	
	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (skip + 1) % 6;
		return skip == 0;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		float load = 0;
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("/proc/loadavg");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			if ((strLine = br.readLine()) != null)   {
				// Print the content on the console
				load = Float.parseFloat(strLine.substring(0,4));
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
		}
		
		float highload = Float.parseFloat((String) storage.get("high load"));
		float lowload = Float.parseFloat((String) storage.get("low load"));
		
		if (load > highload && !bot.hasHighLoad()){
			bot.setHighLoad(true);
			log.info("High load detected.");
			for(Channel channel: bot.getProfile().getChannels()) {
				bot.send(new ModeMsg(channel, "-v", bot.getProfile().getNickname()));
			}
		}
		if (load < lowload && bot.hasHighLoad()){
			bot.setHighLoad(false);
			log.info("low load detected.");
			for(Channel channel: bot.getProfile().getChannels()) {
				bot.send(new ModeMsg(channel, "+v", bot.getProfile().getNickname()));
			}
		}
	}

}
