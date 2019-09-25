package status;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ui.Simulator;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A {@code Counter} that, rather than displaying an absolute value, resets at fixed intervals, storing the last few values and displaying the average. 
 * @author Wei Liang
 */
public abstract class RateCounter extends Counter {

	private ScheduledExecutorService timer;
	private final ArrayBlockingQueue<Integer> counts;
	protected final int sampleTime;
	
	/**
	 * @param sampleTime The time delay between resetting each count (the length of time between each fixed interval.
	 */
	protected RateCounter(final String name, final int sampleTime) {
		super(name);
		this.sampleTime = sampleTime;
		this.counts = new ArrayBlockingQueue<>(sampleTime);
		while(this.counts.offer(new Integer(0)))	// fill history with 0
			;
		this.timer = Executors.newSingleThreadScheduledExecutor();
		this.timer.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run(){
				try{
					counts.remove();
					counts.add(count);
				}finally{
					resetCount();
				}
			}
		}, Math.max(5, sampleTime), 1, TimeUnit.SECONDS);
	}
	
	@Override
	public final void updateNode(){
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				double average = 0;
				for(Integer i: counts)
					average += i;
				average /= counts.size();
				((Label) n).setText(Simulator.getI18nUtils().getString(name) + ": " + average);
			}
		});
	}
	
	@Override
	public synchronized void inc(){
		count++;
	}
	
	/**
	 * Clears the memory of the counter to 0.
	 */
	public void flushCount(){
		counts.clear();
		while(this.counts.offer(new Integer(0)))
			;
		updateNode();
	}

	/**
	 * Stops the timer. The status label will remain visible, but it will not be updated.
	 * Note that once the timer has been shut down, it should not be restarted.
	 */
	public final void shutdownTimer(){
		this.timer.shutdownNow();
	}

}