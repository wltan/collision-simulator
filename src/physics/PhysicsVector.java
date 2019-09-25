package physics;

/**
 * The base class for storing two-dimensional vectors.
 * Not to be confused with {@code java.util.Vector}
 * @author Wei Liang
 */
public class PhysicsVector implements Cloneable {
	
	public volatile double x, y;
	
	/**
	 * @param x the horizontal component of the vector
	 * @param y the vertical component of the vector
	 */
	public PhysicsVector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the magnitude of this vector
	 */
	public final double getMagnitude(){
		return Math.sqrt(x*x+y*y);
	}
	
	public static final PhysicsVector fromPolar(double r, double theta){
		return new PhysicsVector(r*Math.cos(theta), r*Math.sin(theta));
	}
	
	/**
	 * @return the direction this vector is pointing towards, relative to the positive x-axis, and 0 when the magnitude of the vector is 0.
	 */
	public final double getDirection(){
		return Math.atan2(y, x);
	}
	
	/**
	 * Multiplies a vector by a scalar quantity. The result is a vector.
	 * Note that the original vector object is not changed; a new instance is created.
	 * @param val The scalar to multiply by
	 * @return The resultant vector
	 */
	public final PhysicsVector scalarMultiply(double val){
		return new PhysicsVector(x*val, y*val);
	}
	
	/**
	 * Adds two vectors. The result is a vector.
	 * Note that the original vector object is not changed; a new instance is created.
	 * Vector addition is commutative.
	 * @param v The vector to add
	 * @return The resultant vector
	 */
	public final PhysicsVector add(PhysicsVector v){
		return new PhysicsVector(this.x + v.x, this.y + v.y);
	}
	
	/**
	 * Subtracts one vector from another. The result is a vector.
	 * Note that the original vector object is not changed; a new instance is created.
	 * Also note that subtraction is not commutative.
	 * @param v The vector to add
	 * @return The resultant vector
	 */
	public final PhysicsVector subtract(PhysicsVector v){
		return new PhysicsVector(this.x - v.x, this.y - v.y);
	}
	
	/**
	 * Finds the dot product of two vectors. The result is a scalar.
	 * Note that the original vector object is not changed; a new instance is created.
	 * @param v The other vector
	 * @return The dot product of the two vectors
	 */
	public final double dotProduct(PhysicsVector v){
		return this.x*v.x+this.y*v.y;
	}
	
	/**
	 * Finds the unit vector of the current vector.
	 * A unit vector has the same direction as the original vector, but with a magnitude of exactly 1.
	 * @return The unit vector
	 */
	public final PhysicsVector toUnitVector(){
		final double mag = getMagnitude();
		return new PhysicsVector(x/mag, y/mag);
	}
	
	/**
	 * Finds the normal of the current vector.
	 * The normal to a vector is perpendicular to the original vector.
	 * @return The normal of the current vector.
	 */
	public final PhysicsVector getNormal(){
		return new PhysicsVector(-y, x);
	}
	
	/**
	 * Reverses the current vector.
	 * The resultant vector has the same magnitude as the original but points in the opposite direction. 
	 * @return The inverted vector
	 */
	public final PhysicsVector invert(){
		return new PhysicsVector(-x, -y);
	}
	
	/**
	 * Expresses the vector in Cartesian form.
	 */
	@Override
	public String toString(){
		return "(" + Math.round(x) + "," + Math.round(y) + ")";
	}
	
	/**
	 * Produces an identical {@code PhysicsVector} object.
	 * The two objects are independent of each other.
	 */
	@Override
	public PhysicsVector clone(){
		return new PhysicsVector(x, y);
	}
	
}