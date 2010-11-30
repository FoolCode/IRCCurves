package curves.trigger.foolrulez;

import java.util.ArrayList;

import curves.trigger.*;
import curves.trigger.lib.R_Whois;

public class Index extends ITriggerIndex {

	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
		list.add(new R_NiceBoat());
		list.add(new R_NiceBoatNick());
		list.add(new R_NiceBoatLeave());
		list.add(new R_AutoVoice());
		list.add(new R_Roulette());
		list.add(new R_Whois());
		return list;
	}

	public ArrayList<ICloseHandler> closeHandlers() {
		return null;
	}

	public ArrayList<IWriteHandler> writeHandlers() {
		ArrayList<IWriteHandler> list = new ArrayList<IWriteHandler>();
		return list;
	}

	public ArrayList<IPeriodicHandler> periodicHandlers() {
		return null;
	}

}
