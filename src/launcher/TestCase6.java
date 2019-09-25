package launcher;

import i18n.I18nUtils;

import java.util.ArrayList;
import java.util.Locale;

import javafx.scene.paint.Color;
import objects.Ball;
import physics.PhysicsVector;
import status.AverageSpeedTracker;
import status.ObjectCollisionCounter;
import status.StatusElement;
import ui.Simulator;

/**
 * Tests a collision, with a large difference in object masses.
 * @author Wei Liang
 */
public final class TestCase6 {

	public static void main(String[] args) {
		Locale[] lang = {
				Locale.ENGLISH
		};
		ArrayList<Ball> objects = new ArrayList<>();
		objects.add(new Ball(-100, 21, 1, new PhysicsVector(0, 0), Color.RED));
		objects.add(new Ball(-100, -21, 1, new PhysicsVector(0, 0), Color.GREEN));
		objects.add(new Ball(-100, 62, 1, new PhysicsVector(0, 0), Color.BLUE));
		objects.add(new Ball(-100, -62, 1, new PhysicsVector(0, 0), Color.YELLOW));
		objects.add(new Ball(100, 0, 100, new PhysicsVector(-1, 0), Color.BLACK));

		StatusElement.StatusElementInitializer initStatuses = new StatusElement.StatusElementInitializer(){
			@Override
			public ArrayList<StatusElement> getStatuses() {
				ArrayList<StatusElement> statuses = new ArrayList<>();
				statuses.add(new AverageSpeedTracker());
				statuses.add(new ObjectCollisionCounter());
				return statuses;
			}
		};

		Simulator.initialize(new I18nUtils("CollisionStringResource", lang), objects, initStatuses);
	}

}
