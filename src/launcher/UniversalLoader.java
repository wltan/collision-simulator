package launcher;

import i18n.I18nUtils;

import java.util.ArrayList;
import java.util.Locale;

import objects.Ball;
import status.AverageSpeedTracker;
import status.ObjectCollisionCounter;
import status.ObjectCollisionRateCounter;
import status.SimulatorHeightTracker;
import status.SimulatorWidthTracker;
import status.StatusElement;
import status.WallCollisionCounter;
import status.WallCollisionRateCounter;
import ui.Simulator;

/**
 * Loads a simulator with all current status displays and no objects.
 * The objects themselves can be loaded from file data.
 * @author Wei Liang
 */
public class UniversalLoader {
	
	public static void main(String[] args) {
		Locale[] lang = {
				Locale.ENGLISH
		};
		ArrayList<Ball> objects = new ArrayList<>();
		StatusElement.StatusElementInitializer initStatuses = new StatusElement.StatusElementInitializer(){
			@Override
			public ArrayList<StatusElement> getStatuses() {
				ArrayList<StatusElement> statuses = new ArrayList<>();
				statuses.add(new ObjectCollisionRateCounter(5));
				statuses.add(new WallCollisionRateCounter(5));
				statuses.add(new AverageSpeedTracker());
				statuses.add(new ObjectCollisionCounter());
				statuses.add(new WallCollisionCounter());
				statuses.add(new SimulatorHeightTracker());
				statuses.add(new SimulatorWidthTracker());
				return statuses;
			}
		};

		Simulator.initialize(new I18nUtils("CollisionStringResource", lang), objects, initStatuses);
	}
	
}