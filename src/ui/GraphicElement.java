package ui;

import javafx.scene.Node;

/**
 * This interface does not yet have any use in the application's logic;
 * it simply annotates to the programmer that the object can be added as a GUI element into the application.
 * @author Wei Liang
 */
public interface GraphicElement {
	
	/**
	 * @return A {@code Node} that can be added into the application's GUI.
	 */
	public Node toNode();
	
}