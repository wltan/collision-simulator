package physics;

import objects.Thing;

/**
 * Contains any physics-related utilities other then the collision engine itself, which is provided in {@code CollisionEngine}
 * @author Wei Liang
 */
public final class PhysicsUtility {
	
	private PhysicsUtility() {}
	
	/**
	 * Adds up the momentum of several objects.
	 * @param objects The {@code Thing}s to be added.
	 * @return The {@code PhysicsVector} representing the resultant momentum.
	 */
	public static final PhysicsVector momentumSum(Thing... objects){
		PhysicsVector result = new PhysicsVector(0, 0);
		for(Thing t: objects){
			result = result.add(t.getMomentumVector());
		}
			
		return result;
	}
	
}
