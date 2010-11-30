package curves.trigger;

import java.util.ArrayList;

public abstract class ITriggerIndex {

	public abstract ArrayList<IReadHandler> readHandlers();

	public abstract ArrayList<IWriteHandler> writeHandlers();

	public abstract ArrayList<ICloseHandler> closeHandlers();

	public abstract ArrayList<IPeriodicHandler> periodicHandlers();

}
