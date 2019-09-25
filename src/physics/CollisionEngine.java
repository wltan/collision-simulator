package physics;

import javafx.scene.layout.Region;
import objects.Thing;
import status.*;
import ui.Simulator;

import java.util.ArrayList;

/**
 * Contains utilities to detect and compute collisions.
 * @author Wei Liang
 */
public final class CollisionEngine {

	private CollisionEngine() {}
	/**
	 * Detects and resolves any collision between two objects.
	 * @param t1 the first colliding object
	 * @param t2 the second colliding object
	 * @throws IllegalArgumentException {@code t1} and {@code t2} are identical
	 */
	public static synchronized void computePossibleCollision(Thing t1, Thing t2){
		if(t1==t2) throw new IllegalArgumentException("Objects are equal!");
		final PhysicsVector rawNormal = t2.getPositionVector().subtract(t1.getPositionVector());
		final double distance = t1.size+t2.size, initDistance = rawNormal.getMagnitude();
		if(initDistance<=distance){
			final PhysicsVector normal = t2.getPositionVector().subtract(t1.getPositionVector()).toUnitVector();
			final PhysicsVector collision = normal.getNormal();
			final double
					n1 = normal.dotProduct(t1.getVelocityVector()),
					n2 = normal.dotProduct(t2.getVelocityVector()),
					c1 = collision.dotProduct(t1.getVelocityVector()),
					c2 = collision.dotProduct(t2.getVelocityVector()),
					m1 = t1.getMass(),
					m2 = t2.getMass();
			
			if(n1 < 0 && n2 > 0)
				return; // objects are separating, do nothing more
			
			final double 
					n1f = (m1*n1+m2*(2*n2-n1))/(m1+m2),
					n2f = (m1*(2*n1-n2)+m2*n2)/(m1+m2);
			final PhysicsVector
					v1f = normal.scalarMultiply(n1f).add(collision.scalarMultiply(c1)),
					v2f = normal.scalarMultiply(n2f).add(collision.scalarMultiply(c2));
			
			t1.getVelocityVector().x = v1f.x;
			t1.getVelocityVector().y = v1f.y;
			t2.getVelocityVector().x = v2f.x;
			t2.getVelocityVector().y = v2f.y;
			incObjectCollisions();
		}
	}
	
	/**
	 * Detects and resolves any collision an object has with the wall boundaries.
	 * If the object is already moving towards the boundary in question, no collision takes place.
	 * @param t the object to check
	 */
	public static synchronized void computePossibleWallCollision(Thing t){
		Region field =  Simulator.getInstance().getField();
		if((t.getY() + t.size > field.getHeight()/2 && t.getVelocityVector().y > 0)||(t.getY() + field.getHeight()/2 - t.size < 0 && t.getVelocityVector().y < 0)){
			incWallCollisions();
			t.getVelocityVector().y *= -1;
		}
		if((t.getX() + t.size > field.getWidth()/2 && t.getVelocityVector().x > 0)||(t.getX() + field.getWidth()/2 - t.size < 0 && t.getVelocityVector().x < 0)){
			incWallCollisions();
			t.getVelocityVector().x *= -1;
		}
	}
	
	/**
	 * Increments the counter of all {@code ObjectCollisionRateCounter}s by one.
	 * This method should not be called directly; it is automatically called when a collision is detected. 
	 */
	private static void incObjectCollisions(){
		ArrayList<StatusElement> array = Simulator.getStatuses();
		for(StatusElement e: array)
			if(e instanceof ObjectCollisionRateCounter)
				((ObjectCollisionRateCounter) e).inc();
			else
				if(e instanceof ObjectCollisionCounter)
					((ObjectCollisionCounter) e).inc();
	}
	
	/**
	 * Increments the counter of all {@code WallCollisionRateCounter}s by one.
	 * This method should not be called directly; it is automatically called when a collision is detected.
	 */
	private static void incWallCollisions(){
		ArrayList<StatusElement> array = Simulator.getStatuses();
		for(StatusElement e: array)
			if(e instanceof WallCollisionRateCounter)
				((WallCollisionRateCounter) e).inc();
			else
				if(e instanceof WallCollisionCounter)
					((WallCollisionCounter) e).inc();
	}
	
}