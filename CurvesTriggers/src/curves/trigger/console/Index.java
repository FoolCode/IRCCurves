package curves.trigger.console;

import java.util.ArrayList;

import curves.trigger.ICloseHandler;
import curves.trigger.IPeriodicHandler;
import curves.trigger.IReadHandler;
import curves.trigger.ITriggerIndex;
import curves.trigger.IWriteHandler;

public class Index extends ITriggerIndex {
	
	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
	/*	list.add(new R_PrivMsg());
		list.add(new R_NickMsg());
		list.add(new R_PingMsg());
		list.add(new R_InviteMsg());
		list.add(new R_NoticeMsg());
		list.add(new R_KickMsg());
		list.add(new R_PartMsg());
		list.add(new R_JoinMsg());
		list.add(new R_ModeMsg());
		list.add(new R_ErrorMsg());
		list.add(new R_QuitMsg());
		list.add(new R_TopicMsg()); */
		list.add(new R_Incoming());
		list.add(new R_RegisterError());
		return list;
	}

	public ArrayList<ICloseHandler> closeHandlers() {
		return null;
	}

	public ArrayList<IWriteHandler> writeHandlers() {
		ArrayList<IWriteHandler> list = new ArrayList<IWriteHandler>();
		list.add(new W_Outgoing());
		return list;
	}
	
	public ArrayList<IPeriodicHandler> periodicHandlers(){
		// ArrayList<IPeriodicHandler> list = new ArrayList<IPeriodicHandler>();
		// list.add(new P_Time());
		return null;
	}

}
