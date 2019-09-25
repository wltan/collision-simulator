package status;

import ui.Simulator;

/**
 * Tracks the width of the simulation field.
 * @author Wei Liang
 */
public class SimulatorWidthTracker extends Tracker<Integer> {

	private static final String name = "simWidth";
	
	public SimulatorWidthTracker() {
		super(name, 0);
	}

	@Override
	public void updateValue() {
		value = (int) Simulator.getInstance().getField().getWidth();
		updateNode();
	}

}
