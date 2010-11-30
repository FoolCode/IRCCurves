package curves.trigger.mana;

import java.util.ArrayList;

import curves.trigger.*;

public class Index extends ITriggerIndex {

	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
		list.add(new R_Duel());
		list.add(new R_DuelNick());
		list.add(new R_DuelLeave());
		list.add(new R_Leave());
		list.add(new R_ChangeNick());
		list.add(new R_Join());
		list.add(new R_QueryStats());
		list.add(new R_SummonCreature());
		return list;
	}


	public ArrayList<ICloseHandler> closeHandlers() {
		return null;
	}

	public ArrayList<IWriteHandler> writeHandlers() {
		ArrayList<IWriteHandler> list = new ArrayList<IWriteHandler>();
		list.add(new W_Kick());
		return list;
	}

	public ArrayList<IPeriodicHandler> periodicHandlers() {
		ArrayList<IPeriodicHandler> list = new ArrayList<IPeriodicHandler>();
		list.add(new P_UpdateMana());
		// list.add(new P_Reset());
		return list;
	}

}
