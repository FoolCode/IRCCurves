package curves.trigger.record;

import java.util.ArrayList;

import curves.trigger.*;
import curves.trigger.lib.R_Whois;

public class Index extends ITriggerIndex {

	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
		list.add(new R_PrivMsg());
		list.add(new R_ReceiveNames());
		list.add(new R_FinishNames());
		list.add(new R_NewMail());
		list.add(new R_DeliverMail());
		list.add(new R_Whois());
		return list;
	}

	public ArrayList<ICloseHandler> closeHandlers() {
		return null;
	}

	public ArrayList<IWriteHandler> writeHandlers() {
		ArrayList<IWriteHandler> list = new ArrayList<IWriteHandler>();
		list.add(new W_PrivMsg());
		return list;
	}

	public ArrayList<IPeriodicHandler> periodicHandlers() {
		ArrayList<IPeriodicHandler> list = new ArrayList<IPeriodicHandler>();
		list.add(new P_QueryNames());
		list.add(new P_PurgeMails());
		return list;
	}

}
