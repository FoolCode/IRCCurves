package curves.trigger.quote;

import java.util.ArrayList;

import curves.trigger.*;


public class Index extends ITriggerIndex {

	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
		list.add(new R_Quote());
		list.add(new R_Find());
		list.add(new R_About());
		return list;
	}

	public ArrayList<ICloseHandler> closeHandlers() {
		return null;
	}

	public ArrayList<IWriteHandler> writeHandlers() {
		return null;
	}

	public ArrayList<IPeriodicHandler> periodicHandlers() {
		ArrayList<IPeriodicHandler> list = new ArrayList<IPeriodicHandler>();
		list.add(new P_Archive());
		return list;
	}

}
