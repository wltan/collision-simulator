package objects;

import javafx.scene.paint.Color;
import physics.PhysicsVector;

public class Ball extends Thing {
	
	private static final long serialVersionUID = -2289459067538958913L;
	
	public static final int SIZE = 20;
	
	public Ball(double x, double y, int mass, PhysicsVector velocity) {
		super(x, y, SIZE, mass, velocity);
	}

	public Ball(double x, double y, int mass, PhysicsVector velocity, Color fill) {
		super(x, y, SIZE, mass, velocity, fill);
	}

}
