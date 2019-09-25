package status;

import javafx.scene.Node;
import ui.GraphicElement;

import java.util.ArrayList;

/**
 * The most basic status element, containing only a node.
 * This class must be extended in order to update the node.
 * @author Wei Liang
 */
public abstract class StatusElement implements GraphicElement{
	
	protected Node n;
	
	protected StatusElement(Node n){
		this.n = n;
	}
	
	/**
	 * Displays the node.
	 */
	@Override
	public final Node toNode(){
		return n;
	}
	
	/**
	 * Updates the status in the node.
	 * Since it is not yet clear what kind of status is to be displayed, it is left abstract.
	 */
	public abstract void updateNode();

	/**
	 * This interface is to enable the definition of status elements outside the JavaFX Application Thread,
	 * while deferring the execution of those definitions to the Application Thread itself.
	 */
	public interface StatusElementInitializer{
		/**
		 * Initializes the list of status elements.
		 * @return An {@code ArrayList} of {@code StatusElement}s to be included in the application.
		 */
		ArrayList<StatusElement> getStatuses();
	}

}
