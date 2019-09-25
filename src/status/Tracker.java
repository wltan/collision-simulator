package status;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ui.Simulator;

/**
 * The base class of status elements that track a certain value.
 * @author Wei Liang
 * @param <V> The data type of the value to be stored. There is no need to widen to a {@code double} expression if a tracker only requires {@code int} values.
 */
public abstract class Tracker<V extends Number> extends StatusElement {

	protected final String name;
	protected volatile V value;
	
	protected Tracker(String name, V defaultValue) {
		super(new Label());
		this.name = name;
		this.value = defaultValue;
	}

	@Override
	public void updateNode() {
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				((Label) n).setText(Simulator.getI18nUtils().getString(name) + ": " + value);
			}
		});
	}
	
	/**
	 * Updates the value.
	 * Since we do not yet have sufficient context, the class is left abstract.
	 * The method is expected to be able to obtain the value on its own.
	 * Therefore, no parameters are passed in.
	 */
	public abstract void updateValue();

}
