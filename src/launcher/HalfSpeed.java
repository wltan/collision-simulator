package launcher;

import i18n.I18nUtils;
import objects.GasParticle;
import physics.PhysicsVector;
import status.*;
import ui.Simulator;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
	
/**
 * Simulates gas molecules in a closed container.
 * Each molecule has half the speed of the corresponding molecule in the control.
 * @author Wei Liang
 */
public class HalfSpeed {
	
	public static void main(String[] args) {
		Locale[] lang = {
				Locale.ENGLISH
		};
		Simulator.fieldPrefWidth = Simulator.fieldPrefHeight = 500;
		ArrayList<PhysicsVector> velocities = new ArrayList<>();
		for(int i = -4; i <= 5; i++)
			for(int j = -4; j <= 5; j++)
				velocities.add(new PhysicsVector(i/4.0, j/4.0));
		randomize(velocities);
		ArrayList<GasParticle> gas = new ArrayList<>();
		for(int i = -4; i <= 5; i++)
			for(int j = -4; j <= 5; j++)
				gas.add(new GasParticle(i*GasParticle.SIZE*3, j*GasParticle.SIZE*3, velocities.get((i+4)*10+(j+4))));

		StatusElement.StatusElementInitializer initStatuses = new StatusElement.StatusElementInitializer(){
			@Override
			public ArrayList<StatusElement> getStatuses() {
				ArrayList<StatusElement> statuses = new ArrayList<>();
				statuses.add(new WallCollisionRateCounter(5));
				statuses.add(new AverageSpeedTracker());
				statuses.add(new SimulatorHeightTracker());
				statuses.add(new SimulatorWidthTracker());
				return statuses;
			}
		};

		Simulator.initialize(new I18nUtils("CollisionStringResource", lang), gas, initStatuses);
	}
	
	/**
	 * Randomizes the speeds
	 */
	private static void randomize(ArrayList<PhysicsVector> data){
		final Random r = new Random();
		for(int i = 0; i < data.size()*10; i++){
			data.add(data.remove(r.nextInt(data.size())));
		}
	}
	
}
