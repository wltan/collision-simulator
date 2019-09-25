package status;

/**
 * A {@code RateCounter} that increments each time a {@code Thing} object collides with the wall boundaries.
 * This counter displays the average number of collisions over a fixed time period. 
 * @see CollisionEngine
 * @author Wei Liang
 */
public class WallCollisionRateCounter extends RateCounter {

	private static final String name = "wallCollision";
	
	public WallCollisionRateCounter(int sampleRate) {
		super(name, sampleRate);
	}

}
