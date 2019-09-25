package file;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import objects.Thing;
import ui.Simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Contains utilities for reading and writing object data from files.
 * @author Wei Liang
 */
public final class FileIO {
	
	/**
	 * Saves the state of the simulation in a file.
	 * Note that each object is saved as a {@code Thing}, and any additional data is lost.
	 */
	public static final void saveData(){
		FileChooser saveloc = new FileChooser();
		saveloc.setTitle(Simulator.getI18nUtils().getString("saveState"));
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(saveloc.showSaveDialog(Simulator.getInstance().getWindow())));
			pw.println(ResourceBundle.getBundle(Simulator.getI18nUtils().strings).getString("appName"));
			pw.println(Simulator.getInstance().getTickDelay());
			ArrayList<Thing> objects = Simulator.getInstance().getObjects(); 
			for(Thing t: objects)
				pw.println(t);
			pw.close();
		} catch (FileNotFoundException e) {}
	}
	
	/**
	 * Loads the state of the simulation from a file.
	 * Note that each object is loaded as a {@code Thing}, since any additional data is lost in saving.
	 */
	public static final void loadData(){
		FileChooser loadloc = new FileChooser();
		loadloc.setTitle(Simulator.getI18nUtils().getString("loadState"));
		try {
			Scanner sc = new Scanner(new FileInputStream(loadloc.showOpenDialog(Simulator.getInstance().getWindow())));
			if(!sc.nextLine().equals(ResourceBundle.getBundle(Simulator.getI18nUtils().strings).getString("appName"))){
				final Stage badFileWarning = new Stage();
				badFileWarning.initStyle(StageStyle.UTILITY);
				badFileWarning.setScene(new Scene(new Group(new Text(0, 35, Simulator.getI18nUtils().getString("badFile"))), 200, 50));
				badFileWarning.setTitle(Simulator.getI18nUtils().getString("badFile"));
				badFileWarning.setResizable(false);
				badFileWarning.show();
				sc.close();
				return;
			}
			final int tick = Integer.parseInt(sc.nextLine());
			ArrayList<Thing> stuff = new ArrayList<>();
			try{
				while(sc.hasNextLine())
					stuff.add(Thing.loadFromData(sc.nextLine()));
			}finally{
				sc.close();
			}
			Simulator.getInstance().setTickDelay(tick);
			Simulator.getInstance().getObjects().clear();
			Simulator.getInstance().getObjects().addAll(stuff);
			((StackPane) Simulator.getInstance().getField()).getChildren().clear();
			for(Thing t: stuff)
				((StackPane) Simulator.getInstance().getField()).getChildren().add(t.toNode());
		} catch (FileNotFoundException e) {}
	}

}