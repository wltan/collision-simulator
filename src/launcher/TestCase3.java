package launcher;

import i18n.I18nUtils;

import java.util.ArrayList;
import java.util.Locale;

import javafx.scene.paint.Color;
import objects.Ball;
import physics.PhysicsVector;
import status.AverageSpeedTracker;
import status.StatusElement;
import status.WallCollisionCounter;
import ui.Simulator;

/**
 * Tests a wall collision.
 * @author Wei Liang
 */
public final class TestCase3 {

	public static void main(String[] args) {
		Locale[] lang = {
				Locale.ENGLISH
		};
		ArrayList<Ball> objects = new ArrayList<>();
		objects.add(new Ball(-10, 0, 1, new PhysicsVector(3, 4), Color.BLACK));

		StatusElement.StatusElementInitializer initStatuses = new StatusElement.StatusElementInitializer(){
			@Override
			public ArrayList<StatusElement> getStatuses() {
				ArrayList<StatusElement> statuses = new ArrayList<>();
				statuses.add(new WallCollisionCounter());
				statuses.add(new AverageSpeedTracker());
				return statuses;
			}
		};

		Simulator.initialize(new I18nUtils("CollisionStringResource", lang), objects, initStatuses);
	}

}
