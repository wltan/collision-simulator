package status;

/**
 * A simple {@code Counter} that increments each time a {@code Thing} collides with the simulation boundaries.
 * @author Wei Liang
 */
public class WallCollisionCounter extends Counter {

	private static final String name = "absWallCollision";
	
	public WallCollisionCounter(){
		super(name);
	}
	
}
