package main;

import java.io.IOException;
import java.net.URL;

import excelUtils.combine.gui.CombineGuiController;
import excelUtils.nameReader.gui.NameReaderGuiController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import marking.FileIOManager;
import marking.NewSettings;
import marking.SoundPlayer;
import marking.TabbedController;
import marking.exam.gui.ConfigData;
import marking.exam.gui.ExamMarksController;
import marking.javaCode.gui.JavaGuiController;
import marking.written.gui.MarksController;
import preloader.PreloadController;
import res.ResourceLoader;

public class Main extends Application {

	public static Stage primaryStage;
	public static URL resourcePath;
	public static NewSettings settings;
	public static ConfigData configData;
	private static MenuBar menuBar;

	@FXML public AnchorPane content;
	@Override
	public void start(Stage primaryStage) {
		try {

			/*
			 GENERAL COMMENTS + TODO: 

		      autofill text box - Tab key to select


			 - Control where the focus goes after entering/adding the mark, eg to the name textfield for the next entry

			 // Revamp TODO
			    Styling at end...

			    Settings, point to notePad++ of ander text editor?  ipv hele ding insit
			    personal settings in elke scene se eie menu insit... dit sal beter wees

			   // Excel file combine.. goed test


			   should move choose file button to a menu option and replace space with re-read button? 

			 * help option in menuBar for instructions instead of in textArea + About

			 * From stackoverflow: 
			    This seems likes a very fine example! Maybe you could improve it by 
			    detecting the type of data you have in Excel and use the SpreadsheetCellType 
			    according to it. You can also detect the Excel format and set it in the cell 
			    if you want to

			 *hidden cells in display...
			 *
			 *Undo knoppie - Kan net die punt weer insit, dalk vir exam? Dink dis dieselfde
			 *
			 *Viewer skuif 'n kolom aan as twee punte vir dieselfde student inlees - Voorlopig reg dink ek
			 *

			 */
			Main.primaryStage=primaryStage;
			primaryStage.getIcons().addAll(ResourceLoader.getIcons("checkmark.ico"));

			FileIOManager.readSettings(); // Read Settings
			FileIOManager.readConfigData();; // Read Settings
			if(configData==null)
				configData = new ConfigData(); // TODO read from file...

			//resourcePath=Main.class.getResource("Gui.fxml");

			//AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("TabbedGui.fxml"));
			AnchorPane page = (AnchorPane) FXMLLoader.load(PreloadController.class.getResource("PreloaderGUI.fxml"));

			//			MarksController mc = (MarksController) fxmlLoader.getController();
			Scene scene = new Scene(page);
			scene.getStylesheets().add(PreloadController.class.getResource("application.css").toExternalForm());

			//	primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Marking");

			//			Student.outText=mc;

			// Stage positioning
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

			// stage positioning
			primaryStage.setX(primaryScreenBounds.getMaxX()*5/8);
			primaryStage.setY(primaryScreenBounds.getMinY() + page.getPrefHeight()*2/4);


			primaryStage.show();
			primaryStage.setResizable(false);





			//		if(settings.playBackgroundSound)
			//		SoundPlayer.startBackGroundSound(ResourceLoader.getMusicFolder(),settings.soundVolume/100);

			SoundPlayer.initializeLoopsound(ResourceLoader.getLoadingFile());
			//SoundPlayer.startLoopSound(); // Testing purposes

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switches between windows
	 * @param number
	 */
	public static void showStart(){

		try {
			Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(Main.primaryStage);
			dialog.setTitle("Select a service");
			dialog.setResizable(false);

			dialog.setX(primaryStage.getX());
			dialog.setY(primaryStage.getY()+primaryStage.getWidth()/2);
			dialog.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));		

			FXMLLoader fxmlLoader = new FXMLLoader(PreloadController.class.getResource("PreloaderGUI.fxml"));
			AnchorPane	page = (AnchorPane) fxmlLoader.load();
			PreloadController mc = (PreloadController) fxmlLoader.getController();
			mc.setStageToClose(dialog);
			//			MarksController mc = (MarksController) fxmlLoader.getController();
			Scene scene = new Scene(page);
			scene.getStylesheets().add(PreloadController.class.getResource("application.css").toExternalForm());
			//	primaryStage.setResizable(false);
			dialog.setScene(scene);
			dialog.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This option shoes the about menu that explains the software etc...
	 */
	public static void showAbout(){

		// Show configure gui...
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.primaryStage);
		dialog.setTitle("About");
		dialog.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));

		dialog.setX(Main.primaryStage.getX());
		dialog.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);


		try {
			FXMLLoader fxmlLoader = new FXMLLoader(AboutController.class.getResource("AboutGui.fxml"));

			AnchorPane page = (AnchorPane) fxmlLoader.load();
			//ConfigureController fooController = (ConfigureController) fxmlLoader.getController();
			//fooController.setStage(dialog);		
			//fooController.setParent(this);
			//	fooController.loadData();
			Scene dialogScene = new Scene(page);

			dialog.setScene(dialogScene);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This option shoes the help menu that explains how to use the software...
	 */
	public static void showHelp(String helpString){

		// Show help gui...
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.primaryStage);
		dialog.setTitle("Help");
		dialog.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));

		dialog.setX(Main.primaryStage.getX());
		dialog.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);


		try {
			FXMLLoader fxmlLoader = new FXMLLoader(AboutController.class.getResource("AboutGui.fxml"));

			AnchorPane page = (AnchorPane) fxmlLoader.load();
			AboutController fooController = (AboutController) fxmlLoader.getController();
			fooController.setInfoText(helpString);	
			//fooController.setParent(this);
			//	fooController.loadData();
			Scene dialogScene = new Scene(page);

			dialog.setScene(dialogScene);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Switches between windows
	 * @param number
	 */
	public static void switchWindows(int number){
		try {

			BorderPane borderPane = new BorderPane();
			borderPane.setTop(menuBar);


			AnchorPane page;
			switch (number) {
			case 1:

				page = (AnchorPane) FXMLLoader.load(MarksController.class.getResource("Gui.fxml"));
				page.getStylesheets().add(MarksController.class.getResource("application.css").toExternalForm());
				break;
			case 2:

				page = (AnchorPane) FXMLLoader.load(JavaGuiController.class.getResource("JavaGui.fxml"));

				page.getStylesheets().add(JavaGuiController.class.getResource("application.css").toExternalForm());
				break;
			case 3:

				page = (AnchorPane) FXMLLoader.load(ExamMarksController.class.getResource("ExamGui.fxml"));

				page.getStylesheets().add(JavaGuiController.class.getResource("application.css").toExternalForm());
				break;
			case 4:

				page = (AnchorPane) FXMLLoader.load(NameReaderGuiController.class.getResource("NameReaderGui.fxml"));

				page.getStylesheets().add(JavaGuiController.class.getResource("application.css").toExternalForm());
				break;
			case 5:

				page = (AnchorPane) FXMLLoader.load(CombineGuiController.class.getResource("CombineGui.fxml"));

				page.getStylesheets().add(CombineGuiController.class.getResource("application.css").toExternalForm());
				break;
			case 6:

				page = (AnchorPane) FXMLLoader.load(TabbedController.class.getResource("NewSettingsGui.fxml"));

				page.getStylesheets().add(JavaGuiController.class.getResource("application.css").toExternalForm());
				break;
			default:
				page=null;
				break;
			}


			borderPane.setCenter(page);
			Scene scene = new Scene(borderPane);

			primaryStage.setScene(scene);
			primaryStage.sizeToScene();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * MenuItems are created with different functions and added to the MenuBar.
	 * 
	 * This method initializes a global menu for the application
	 * More features may be added by each controller class after calling this method
	 */
	public static void createMenu(MenuBar menuBar) {
		// clear menu
		menuBar.getMenus().clear();

		// --- Menu File
		Menu menuFile = new Menu("File");

		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				System.exit(0);
			}
		});
		menuFile.getItems().addAll(exit);

		// --- Menu Edit
		Menu menuEdit = new Menu("Edit");

		// --- Menu View
		Menu menuView = new Menu("View");

		MenuItem goToStart = new MenuItem("Go to Start");
		goToStart.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		goToStart.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Main.showStart();
			}
		});
		menuView.getItems().add(goToStart);

		// --- Menu View
		Menu menuHelp = new Menu("Help");

		MenuItem about = new MenuItem("About");
		about.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				Main.showAbout();
			}
		});
		menuHelp.getItems().add(about);
		menuBar.getMenus().addAll(menuFile, menuEdit, menuView,menuHelp);
	}

	public static void main(String[] args) {



		launch(args);

	}
}
