package curves.trigger.query;

import java.util.ArrayList;

import curves.trigger.*;


public class Index extends ITriggerIndex {

	public ArrayList<IReadHandler> readHandlers() {
		ArrayList<IReadHandler> list = new ArrayList<IReadHandler>();
		list.add(new R_EightBall());
		list.add(new R_Knowledge());
		list.add(new R_GoogleWeb());
		list.add(new R_GoogleImages());
		list.add(new R_GoogleVideo());
		list.add(new R_GoogleNews());
		list.add(new R_GoogleBlogs());
		list.add(new R_GoogleWeather());
		list.add(new R_GoogleFight());
		list.add(new R_Quote());
		list.add(new R_Find());
		list.add(new R_About());
		list.add(new R_Translate());
		list.add(new R_Wiki());
//		list.add(new R_Wikipath());
//		list.add(new R_Bounce());
//		list.add(new R_Whois());
		return list;
	}

	public ArrayList<ICloseHandler> closeHandlers() {
		return null;
	}

	public ArrayList<IWriteHandler> writeHandlers() {
		return null;
	}

	public ArrayList<IPeriodicHandler> periodicHandlers() {
		return null;
	}

}
