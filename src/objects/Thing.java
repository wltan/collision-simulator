package objects;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import physics.CollisionEngine;
import physics.PhysicsVector;
import ui.GraphicElement;
import ui.Simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * The base class of objects that are simulated on the field.
 * Other features can be modified through subclasses.
 * @author Wei Liang
 */
public class Thing implements GraphicElement, Cloneable, Serializable {
	
	private static final long serialVersionUID = -796745693359529780L;
	
	// strictly GUI related
	protected transient Circle c;
	private final Color fill;
	
	// overlap
	protected volatile double x, y;
	public final int size;
	
	// strictly physics related
	protected final int mass;
	protected final PhysicsVector v;

	/**
	 * Creates a new object to be placed in the simulator.
	 * The object is assumed to be circular.
	 * Note that the origin is defined as the center of the simulation field.
	 * The fill color of the circle default to {@code Color.BLACK}
	 * @param x the horizontal distance from the origin 
	 * @param y the vertical distance from the origin
	 * @param size the radius of the object
	 * @param velocity the initial velocity of the object
	 */
	protected Thing(double x, double y, int size, int mass, PhysicsVector velocity) {
		this(x, y, size, mass, velocity, Color.BLACK);
	}
	
	/**
	 * Creates a new object to be placed in the simulator.
	 * The object is assumed to be circular.
	 * Note that the origin is defined as the center of the simulation field.
	 * @param x the horizontal distance from the origin 
	 * @param y the vertical distance from the origin
	 * @param size the radius of the object
	 * @param velocity the initial velocity of the object
	 * @param fill the fill color of the object
	 */
	protected Thing(double x, double y, int size, int mass, PhysicsVector velocity, Color fill) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.mass = mass;
		this.v = velocity;
		this.fill = fill;
		c = new Circle(size);
		c.setTranslateX(x);
		c.setTranslateY(y);
		c.setFill(fill);
	}

	/**
	 * @return the horizontal distance of the object from the origin
	 */
	public final double getX() {
		return x;
	}

	/**
	 * @return the distance of the object from the origin
	 */
	public final double getY() {
		return y;
	}
	
	/**
	 * @return the mass of the object
	 */
	public final double getMass() {
		return mass;
	}
	
	/**
	 * Momentum is defined as the product of an object's mass (a scalar quantity) and its velocity (a vector quantity).
	 * @return the momentum of the current object in a {@code PhysicsVector}
	 */
	public final PhysicsVector getMomentumVector(){
		return v.scalarMultiply(mass);
	}
	
	/**
	 * The object's velocity defines how many pixels an object will travel in a single frame.
	 * @return the velocity of the object
	 */
	public final PhysicsVector getVelocityVector(){
		return v;
	}
	
	/**
	 * The object's position can be expressed in terms of a vector, starting from the origin and ending at the object's center.
	 * This is known as the position vector of an object.
	 * @return the position vector of the object
	 */
	public final PhysicsVector getPositionVector(){
		return new PhysicsVector(x, y);
	}
	
	/**
	 * Advances the object forward one step in time.
	 * The length/frequency of this step is determined by the simulation speed.
	 * This method also checks and resolves any collision involving this object and another object, or the wall.
	 * Finally, the corresponding GUI element is updated to reflect the new position.
	 */
	public synchronized final void move() {
		x += v.x;
		y += v.y;
		ArrayList<Thing> obj = Simulator.getInstance().getObjects();
		for(Thing t: obj){
			if(t != this)
				CollisionEngine.computePossibleCollision(this, t);
			CollisionEngine.computePossibleWallCollision(this);
		}
		updateNode();
	}
	
	/**
	 * Updates the GUI element to the current position.
	 */
	private final void updateNode() {
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				c.setTranslateX(x);
				c.setTranslateY(y);				
			}
		});
	}
	
	@Override
	public final Node toNode() {
		return c;
	}
	
	public static final char delim = ',';
	
	/**
	 * Expresses the data in the object to be written into a file.
	 */
	@Override
	public final String toString(){
		return "" + x + delim + y + delim + size + delim + mass + delim + v.x + delim + v.y + delim + fill.getRed() + delim + fill.getGreen() + delim + fill.getBlue();
	}
	
	/**
	 * Loads a new object from data previously saved in a file.
	 * Note that only {@code Thing}s can be saved and loaded, and any subclass instances can only be saved and loaded as {@code Thing}s
	 * @param rawString The line of characters representing the object.
	 * @return The loaded object.
	 * @throws NumberFormatException The line is invalid.
	 */
	public static final Thing loadFromData(String rawString) throws NumberFormatException {
		StringTokenizer st = new StringTokenizer(rawString, ""+delim);
		Thing t = new Thing(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), new PhysicsVector(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())), Color.color(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
		return t;
	}
	
	@Override
	public Thing clone(){
		return new Thing(x, y, size, mass, v.clone(), fill);
	}
	
}
