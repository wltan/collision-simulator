package status;

/**
 * A {@code RateCounter} that increments each time a {@code Thing} object collides with another.
 * This counter displays the average number of collisions over a fixed time period. 
 * @see CollisionEngine
 * @author Wei Liang
 */
public class ObjectCollisionRateCounter extends RateCounter {

	private static final String name = "objectCollision";
	
	public ObjectCollisionRateCounter(int sampleRate) {
		super(name, sampleRate);
	}

}
