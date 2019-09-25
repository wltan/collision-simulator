package ui;

import java.awt.*;

/**
 * Contains utilities for displaying a {@code SplashScreen} at the start of the application.
 * @author Wei Liang
 */
final class AppSplashScreen {
	
	/**
	 * Loads the graphics for the splash screen.
	 * @param g The graphics for the splash screen
	 */
	private static void renderSplashFrame(Graphics2D g) {
    	g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLUE);
        g.drawString("Simulator Loading...", 120, 150);
    }
	
	/**
	 * Shows a splash screen for 1 second.
	 * Note that during this time, the thread will lock until 3 seconds have elapsed.
	 * If the splash screen is not defined in any way, a {@code NullPointerException} will be thrown and immediately caught, and the method will return prematurely.
	 * If the thread is interrupted while displaying the splash screen, the resulting {@code InterruptedException} will also be immediately caught, and the method will also return prematurely.
	 */
	public static void showSplashScreen() {
		try{
			final SplashScreen splash = SplashScreen.getSplashScreen();
			final Graphics2D g = splash.createGraphics();
			renderSplashFrame(g);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			} finally {
				splash.close();
			}
		}catch(NullPointerException e){
			return;
		}
	}
	
	/**
	 * Does exactly the same function as showSplashScreen(), but expressed as a {@code Runnable} object.
	 * This object can be run directly or executed by passing it into a {@code Thread} object. 
	 * @return The splash screen display command.
	 * @see showSplashScreen()
	 */
	public static Runnable splashScreenRunnable(){
		return new Runnable(){
			@Override
			public void run(){
				showSplashScreen();
			}
		};
	}
	
}