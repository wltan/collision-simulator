package ui;

import file.FileIO;
import i18n.I18nUtils;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import objects.Thing;
import status.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The "main" class of the application.
 * It contains most of the application's GUI and its elements, along with simple animation to move the elements.
 * It should be entered via the static initialize() method.
 * @author Wei Liang
 */
public final class Simulator extends Application {
	
	// instantiation
	private static Simulator instance;
	/**
	 * @return The instance of this application, so one can access its instance variables and methods.
	 */
	public static final Simulator getInstance(){
		return instance;
	}
	
	// i18n related
	private static I18nUtils i18n;
	public static final I18nUtils getI18nUtils(){
		return i18n;
	}
	private ArrayList<Pair<? extends Labeled, String>> labels;
	private ArrayList<Pair<? extends MenuItem, String>> menus;
	private ArrayList<Pair<? extends Button, String>> buttons;
	
	// GUI related
	public static final int HEIGHT=600,WIDTH=800;
	public static int fieldPrefWidth, fieldPrefHeight;
	protected Stage primaryStage;
	public final Stage getWindow(){
		return primaryStage;
	}
	protected Button startStop;
	protected HBox bottom;
	protected StackPane field;
	protected final EventHandler<ActionEvent> pauseAction = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			animator.shutdownNow();
			startStop.setText(i18n.getString("start"));
			startStop.setOnAction(startAction);
			bottom.setDisable(false);
		}
	};
	protected final EventHandler<ActionEvent> startAction = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			bottom.setDisable(true);
			startStop.setText(i18n.getString("pause"));
			startStop.setOnAction(pauseAction);
			animator = Executors.newSingleThreadScheduledExecutor();
			animator.scheduleAtFixedRate(animation, 0, tickDelay, TimeUnit.MILLISECONDS);
		}
	};
	
	// animation related
	private int tickDelay = 10;
	public int getTickDelay(){
		return tickDelay;
	}
	public void setTickDelay(int tickDelay){
		if(startStop.getOnAction()==pauseAction)
			throw new IllegalStateException("Simulation must be paused first!");
		this.tickDelay = tickDelay;
	}
	protected ScheduledExecutorService animator;
	protected final Runnable animation = new Runnable(){
		@Override
		public void run(){
			for(Thing t: objects)
				t.move();
			for(StatusElement e: statuses)
				if(e instanceof AverageSpeedTracker)
					((AverageSpeedTracker) e).updateValue();
		}
	};
	
	// simulation related
	protected static ArrayList<Thing> startObjects;
	protected static ArrayList<Thing> objects;
	
	// status related
	protected static ArrayList<StatusElement> statuses;
	private static StatusElement.StatusElementInitializer statusMaker;
	
	public static final ArrayList<StatusElement> getStatuses(){
		return statuses;
	}

	/**
	 * @see Application.start()
	 */
	@Override
	public void start(final Stage primaryStage) {
		// initialization of variables
		Simulator.instance = this;
		this.primaryStage = primaryStage;
		labels = new ArrayList<>();
		menus = new ArrayList<>();
		buttons = new ArrayList<>();
		statuses = statusMaker.getStatuses();
		
		Thread splash = new Thread(AppSplashScreen.splashScreenRunnable());
		splash.start();
		
		final BorderPane root = new BorderPane();
		
		this.field = new StackPane();
		for(Thing t: objects)
			field.getChildren().add(t.toNode());
		field.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				for(StatusElement e: statuses)
					if(e instanceof SimulatorHeightTracker)
						((SimulatorHeightTracker) e).updateValue();
			}
		});
		field.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,Number arg1, Number arg2) {
				for(StatusElement e: statuses)
					if(e instanceof SimulatorWidthTracker)
						((SimulatorWidthTracker) e).updateValue();
			}
		});
		root.setCenter(field);
				
		final VBox left = new VBox();
		
		this.startStop = new Button(i18n.getString("start"));
		labels.add(new Pair<>(startStop, "start"));
		startStop.setOnAction(startAction);
		
		final Button reset = new Button(i18n.getString("reset"));
		buttons.add(new Pair<>(reset, "reset"));
		reset.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				if(startStop.getOnAction()==pauseAction)
					startStop.fire();
				startStop.setDisable(true);
				objects.clear();
				field.getChildren().clear();
				for(StatusElement e: statuses)
					if(e instanceof RateCounter)
						((RateCounter) e).flushCount();
					else 
						if(e instanceof Counter)
							((Counter) e).resetCount();
				for(Thing t: startObjects){
					final Thing clone = t.clone();
					objects.add(clone);
					field.getChildren().add(clone.toNode());
				}
				startStop.setDisable(false);
			}
		});
		
		left.setSpacing(10);
		left.setPrefWidth(60);
		left.setFillWidth(true);
		left.getChildren().addAll(new Label(), startStop, reset);
		root.setLeft(left);
		
		final VBox right = new VBox();
		right.getChildren().add(new Label());
		for(StatusElement s: statuses)
			right.getChildren().add(s.toNode());
		right.setSpacing(5);
		root.setRight(right);
		
		bottom = new HBox();
		final HBox speed = new HBox();
		final Label speedLabel = new Label(i18n.getString("speed"));
		labels.add(new Pair<>(speedLabel, "speed"));
		final TextField speedEdit = new TextField(""+tickDelay);
		speedEdit.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
		      @Override public void handle(KeyEvent keyEvent) {
		    	  if ("0123456789".contains(keyEvent.getCharacter())) {
		        	  if(!speedEdit.getText().isEmpty())
		        		  tickDelay = Integer.parseInt(speedEdit.getText());
		        	  else{
		        		  speedEdit.setText("100");
		        		  tickDelay = 100;
		        	  }  
		          }else{
		        	  keyEvent.consume();
		          }
		        }
		      });
		speedEdit.setOnKeyReleased(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent keyEvent) {
				if(!speedEdit.getText().isEmpty())
	        		  tickDelay = Integer.parseInt(speedEdit.getText());
			}
		});
		
		speed.getChildren().addAll(speedLabel, speedEdit);
		bottom.getChildren().addAll(speed);
		root.setBottom(bottom);
		
		final MenuBar menuBar = new MenuBar();
		
		final Menu display = new Menu(i18n.getString("display"));
		menus.add(new Pair<>(display, "display"));
		
		final MenuItem toggleStatus = new MenuItem(i18n.getString("toggleGUI"));
		menus.add(new Pair<>(toggleStatus, "toggleGUI"));
		toggleStatus.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				left.setVisible(!left.isVisible());
				bottom.setVisible(!bottom.isVisible());
				right.setVisible(!right.isVisible());
			}
		});
		
		final MenuItem fullScreen = new MenuItem(i18n.getString("fullScreen"));
		menus.add(new Pair<>(fullScreen, "fullScreen"));
		fullScreen.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.setFullScreen(!primaryStage.fullScreenProperty().get());
			}
		});
		
		display.getItems().addAll(toggleStatus, fullScreen);
		
		final Menu saveLoad = new Menu(i18n.getString("saveLoad"));
		menus.add(new Pair<>(saveLoad, "saveLoad"));
		
		final MenuItem save = new MenuItem(i18n.getString("save"));
		menus.add(new Pair<>(save, "save"));
		save.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(startStop.getOnAction()==pauseAction) startStop.fire();
				FileIO.saveData();
			}
		});

		final MenuItem load = new MenuItem(i18n.getString("load"));
		menus.add(new Pair<>(load, "load"));
		load.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				if(startStop.getOnAction()==pauseAction) startStop.fire();
				FileIO.loadData();
			}
		});
		
		saveLoad.getItems().addAll(save, load);
		
		final Menu language = new Menu(i18n.getString("language"));
		menus.add(new Pair<>(language, "language"));
		for(final Locale l: i18n.languages){
			final MenuItem i = new MenuItem(l.getDisplayLanguage());
			i.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					changeLanguage(l);
				}
			});
			language.getItems().add(i);
		}
		language.setDisable(i18n.languages.length <= 1);
		
		menuBar.getMenus().addAll(display, saveLoad, language);
		root.setTop(menuBar);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent arg0) {
				try{
					animator.shutdownNow();
				}catch(NullPointerException e){}
				for(StatusElement e: statuses)
					if(e instanceof RateCounter)
						((RateCounter) e).shutdownTimer();
			}
		});
		primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		primaryStage.setTitle(Simulator.getI18nUtils().getString("appName"));
		for(StatusElement e: statuses)
			e.updateNode();
		primaryStage.show();
		primaryStage.setWidth(WIDTH+((fieldPrefWidth>0)?fieldPrefWidth-field.getWidth()+129:0));
		primaryStage.setHeight(HEIGHT+((fieldPrefHeight>0)?fieldPrefHeight-field.getHeight()+39:0));
		try {
			splash.join();
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Changes the display language, and applies the change to all affected GUI elements.
	 * @param l The new display {@code Locale}
	 */
	private void changeLanguage(Locale l){
		i18n.changeLanguage(l);
		startStop.setText(startStop.getOnAction()==startAction?i18n.getString("start"):i18n.getString("pause"));
		for(Pair<? extends Labeled, String> p: labels)
			p.getKey().setText(i18n.getString(p.getValue()));
		for(Pair<? extends MenuItem, String> p: menus)
			p.getKey().setText(i18n.getString(p.getValue()));
		for(Pair<? extends Button, String> p: buttons)
			p.getKey().setText(i18n.getString(p.getValue()));
		for(StatusElement e: statuses)
			e.updateNode();
	}
	
	/**
	 * @return The list of {@code Thing}s in the simulator.
	 */
	public ArrayList<Thing> getObjects(){
		return objects;
	}
	
	/**
	 * @return The simulation field.
	 * It can be used to add and remove objects in the field.
	 */
	public Region getField(){
		return field;
	}
	
	/**
	 * Starts the application.
	 * Note that like {@code Application.launch()}, this method will not return until the application has exited.
	 * As such, all pre-application initialization should be done beforehand.
	 * @param i18nUtils The internationalization utility. See {@code i18n.I18nUtils}. 
	 * @param initObjects The initial list of {@code Thing}s to load at startup.
	 * @param statusDisplays The list of statuses to be added into the status bar. If necessary, more than one instance of a specific {@code StatusElement} may be added.
	 */
	public static void initialize(
			final I18nUtils i18nUtils,
			final ArrayList<? extends Thing> initObjects,
			final StatusElement.StatusElementInitializer initStatuses){
		i18n = i18nUtils;
		startObjects = new ArrayList<>();
		for(Thing t: initObjects)
			startObjects.add(t.clone());
		objects = new ArrayList<>();
		objects.addAll(initObjects);
		statusMaker = initStatuses;
		launch();
	}

}