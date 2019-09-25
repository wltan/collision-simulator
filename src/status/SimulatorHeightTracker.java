package status;

import ui.Simulator;

/**
 * Tracks the height of the simulation field.
 * @author Wei Liang
 */
public class SimulatorHeightTracker extends Tracker<Integer> {

	private static final String name = "simHeight";
	
	public SimulatorHeightTracker() {
		super(name, 0);
	}

	@Override
	public void updateValue() {
		value = (int) Simulator.getInstance().getField().getHeight();
		updateNode();
	}

}
