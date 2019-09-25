package objects;

import physics.PhysicsVector;

public class GasParticle extends Thing {

	private static final long serialVersionUID = -3668098546401043957L;
	
	public static final int SIZE = 5;
	public static final int MASS = 1;
	
	public GasParticle(int x, int y, PhysicsVector velocity) {
		super(x, y, SIZE, MASS, velocity);
	}

}
