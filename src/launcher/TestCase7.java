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
 * An approximation of Newton's Cradle.
 * @author Wei Liang
 */
public final class TestCase7 {

	public static void main(String[] args) {
		Locale[] lang = {
				Locale.ENGLISH
		};
		ArrayList<Ball> objects = new ArrayList<>();
		objects.add(new Ball(-173, 0, 1, new PhysicsVector(2, 0), Color.RED));
		objects.add(new Ball(-82, 0, 1, new PhysicsVector(0, 0), Color.ORANGE));
		objects.add(new Ball(-41, 0, 1, new PhysicsVector(0, 0), Color.YELLOW));
		objects.add(new Ball(0, 0, 1, new PhysicsVector(0, 0), Color.GREEN));
		objects.add(new Ball(41, 0, 1, new PhysicsVector(0, 0), Color.BLUE));
		objects.add(new Ball(82, 0, 1, new PhysicsVector(0, 0), Color.INDIGO));
		objects.add(new Ball(123, 0, 1, new PhysicsVector(0, 0), Color.VIOLET));

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
