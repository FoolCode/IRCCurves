package curves.trigger.fileserver;

import java.util.ArrayList;

import curves.trigger.lib.R_Whois;
import curves.trigger.ICloseHandler;
import curves.trigger.IPeriodicHandler;
import curves.trigger.IReadHandler;
import curves.trigger.ITriggerIndex;
import curves.trigger.IWriteHandler;

public class Index extends ITriggerIndex {
	
	P_Queue queue;
	
	public Index(){
		queue = new P_Queue();
	}

	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
		list.add(new R_Request(queue));
		list.add(new R_DownloadList());
		list.add(new R_DCCResume());
		list.add(new R_Whois());
		return list;
	}

	public ArrayList<ICloseHandler> closeHandlers() {
		return null;
	}

	public ArrayList<IWriteHandler> writeHandlers() {
		return null;
	}
	
	public ArrayList<IPeriodicHandler> periodicHandlers(){
		ArrayList<IPeriodicHandler> list = new ArrayList<IPeriodicHandler>();
		list.add(new P_Crawl());
		list.add(queue);
		return list;
	}

}
