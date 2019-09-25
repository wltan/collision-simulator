package launcher;

import i18n.I18nUtils;

import java.util.ArrayList;
import java.util.Locale;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import objects.Ball;
import physics.PhysicsVector;
import status.AverageSpeedTracker;
import status.ObjectCollisionCounter;
import status.SimulatorHeightTracker;
import status.SimulatorWidthTracker;
import status.StatusElement;
import status.WallCollisionCounter;
import ui.Simulator;

/**
 * Pool table setup.
 * Of course, the balls should slow down due to friction or end up in a pocket somehow.
 * However, this simulation provides neither friction nor holes, so the balls will keep moving almost all the time.
 * @author Wei Liang
 */
public final class TestCase8 {

	public static void main(String[] args) {
		Locale[] lang = {
				Locale.ENGLISH
		};
		ArrayList<Ball> objects = new ArrayList<>();
		Ball cue = new Ball(-200, 0, 1, new PhysicsVector(10, 0), Color.WHITESMOKE);
		((Circle)cue.toNode()).setStroke(Color.BLACK);
		Ball center = new Ball(150, 0, 1, new PhysicsVector(0, 0), Color.BLACK);
		objects.add(cue);
		objects.add(center);
		objects.add(new Ball(68, 0, 1, new PhysicsVector(0, 0), Color.YELLOW));
		objects.add(new Ball(109, 21, 1, new PhysicsVector(0, 0), Color.RED));
		objects.add(new Ball(109, -21, 1, new PhysicsVector(0, 0), Color.RED));
		objects.add(new Ball(150, 41, 1, new PhysicsVector(0, 0), Color.GREEN));
		objects.add(new Ball(150, -41, 1, new PhysicsVector(0, 0), Color.GREEN));
		objects.add(new Ball(191, 21, 1, new PhysicsVector(0, 0), Color.YELLOW));
		objects.add(new Ball(191, -21, 1, new PhysicsVector(0, 0), Color.PURPLE));
		objects.add(new Ball(191, 62, 1, new PhysicsVector(0, 0), Color.MAROON));
		objects.add(new Ball(191, -62, 1, new PhysicsVector(0, 0), Color.ORANGE));
		objects.add(new Ball(232, 0, 1, new PhysicsVector(0, 0), Color.PURPLE));
		objects.add(new Ball(232, 41, 1, new PhysicsVector(0, 0), Color.ORANGE));
		objects.add(new Ball(232, -41, 1, new PhysicsVector(0, 0), Color.BLUE));
		objects.add(new Ball(232, 82, 1, new PhysicsVector(0, 0), Color.BLUE));
		objects.add(new Ball(232, -82, 1, new PhysicsVector(0, 0), Color.MAROON));

		StatusElement.StatusElementInitializer initStatuses = new StatusElement.StatusElementInitializer(){
			@Override
			public ArrayList<StatusElement> getStatuses() {
				ArrayList<StatusElement> statuses = new ArrayList<>();
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
