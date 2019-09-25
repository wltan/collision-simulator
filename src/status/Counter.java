package status;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ui.Simulator;

/**
 * A {@code StatusElement} that displays some form of counting.
 * Utilities are available to increment, get, and reset the count.
 * @author Wei Liang
 */
public abstract class Counter extends StatusElement {
	
	protected final String name;
	protected volatile int count;
	
	/**
	 * @param name The name of the property to be counted, in terms of its key in the accompanying {@code ResourceBundle}
	 */
	protected Counter(final String name){
		super(new Label());
		this.name = name;
		this.count = 0;
	}
	
	@Override
	public void updateNode(){
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				((Label) n).setText(Simulator.getI18nUtils().getString(name) + ": " + count);
			}
		});
	}
	
	/**
	 * Increments the counter by 1.
	 */
	public synchronized void inc(){
		count++;
		updateNode();
	}
	
	/**
	 * Resets the counter to 0.
	 */
	public final void resetCount(){
		count = 0;
		updateNode();
	}
	
	/**
	 * @return The current count
	 */
	public final int getCount(){
		return count;
	}

}
