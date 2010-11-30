package curves.trigger.system;

import java.util.ArrayList;

import curves.trigger.ICloseHandler;
import curves.trigger.IPeriodicHandler;
import curves.trigger.IReadHandler;
import curves.trigger.ITriggerIndex;
import curves.trigger.IWriteHandler;

public class Index extends ITriggerIndex {

	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
		list.add(new R_PingPong());
		list.add(new R_PingTimeoutReceive());
		list.add(new R_AutoRejoin());
		list.add(new R_NickTaken());
		list.add(new R_AcceptInvite());
		list.add(new R_ReceiveChannels());
		list.add(new R_Raw());
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
		list.add(new P_GarbageCollection());
		list.add(new P_PingTimeoutSend());
		list.add(new P_CheckLoad());
		list.add(new P_QueryChannels());
		return list;
	}

}
