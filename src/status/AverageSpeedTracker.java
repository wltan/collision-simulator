package status;

import objects.Thing;
import ui.Simulator;

import java.util.ArrayList;

/**
 * This {@code Tracker} tracks the average speed of all the objects in the simulator.
 * @author Wei Liang
 */
public class AverageSpeedTracker extends Tracker<Double> {
	
	private static final String name = "avgSpeed";
	
	public AverageSpeedTracker() {
		super(name, 0.0);
	}
	
	@Override
	public synchronized void updateValue() {
		ArrayList<Thing> things = Simulator.getInstance().getObjects();
		value = 0.0;
		for(Thing t: things)
			value += t.getVelocityVector().getMagnitude();
		value /= things.size();
		value = Math.round(value*1000)/1000.0;
		updateNode();
	}

}