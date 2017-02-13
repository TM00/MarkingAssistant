package marking.written.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import marking.SoundPlayer;
import marking.written.Student;
import marking.written.StudentList;
import res.ResourceLoader;
import utils.ExcelUtils;
import utils.ExcelView;
import utils.OtherUtils;

public class MarksController implements TextOutput{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private TextField markText;

	@FXML
	private MenuBar menu;

	@FXML
	private VBox hBoxContainer;

	@FXML
	private Label fileNameLabel;

	@FXML
	private TextArea infoText;


	private static String filePath;
	private static StudentList list;
	//	private final AutoFillTextBox<String> fillbox = new AutoFillTextBox<String>();
	//	private final AutoCompleteTextField autoTex = new AutoCompleteTextField();
	private final AutoCompleteTextField2 autoTex = new AutoCompleteTextField2();
	private static ExcelView excelView;



	private void chooseFilePressed(ActionEvent event) {

		/*fillbox.setListLimit(10);

		fillbox.setFilterMode(true);

		fillbox.setMinWidth(hBoxContainer.getWidth());
		hBoxContainer.getChildren().addAll(fillbox);*/

		//		hBoxContainer.getChildren().addAll(autoTex);

		File f = ExcelFileHandler.getFile();
		fileNameLabel.setText(f.getName());

		filePath = f.getAbsolutePath();

		loadExcelFile();
	}

	@FXML
	void configurePressed(ActionEvent event) {

		// Show configure gui...
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.primaryStage);
		dialog.setTitle("Written Configurations");
		dialog.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("ConfigureGui.fxml"));

			AnchorPane page = (AnchorPane) fxmlLoader.load();
			ConfigureController fooController = (ConfigureController) fxmlLoader.getController();
			fooController.setStage(dialog);		
			fooController.setParent(this);
			fooController.loadData();
			Scene dialogScene = new Scene(page);

			dialog.setScene(dialogScene);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void doneConfiguring(){
		System.out.println("Done with configure...");

		// TODO update fields
		ExcelFileHandler.loadConstantsFromSettings();
		
		loadExcelFile();
	}



	@FXML
	void addMarkPressed(ActionEvent event) {



		//		String selected = fillbox.getText();
		String selected = autoTex.getText();
		Student stud = list.getStudent(selected);

		if(stud != null){
			stud.setMark(Double.parseDouble(this.markText.getText()));

			excelView.getGrid().setCellValue(stud.getRowNumber(), ExcelUtils.getExcelColumnIndex(Main.settings.markColumnLetter)+1,
					markText.getText());

			autoTex.setText("");

			autoTex.requestFocus();
		}else{
			displayText("No student found by the name "+selected+" try again", false);
		}


	}

	@FXML
	void writeMarks(ActionEvent event) {

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy Writing");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				alert.show();
				System.out.println("Started");
				ExcelFileHandler.writeStudentMarksToFile(list);

				System.out.println("DONE!!");
				displayText("Succesfully written to the file...",true);	
				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				SoundPlayer.stopPlayingSound();
				alert.close();
			}

			@Override protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
				System.out.println("Cancelled");
				infoText.appendText("\nAn error ocurred ");
				SoundPlayer.stopPlayingSound();
				alert.close();
			}

			@Override protected void failed() {
				super.failed();
				updateMessage("Failed!");
				System.out.println("Failed");
				infoText.appendText("\nAn error ocurred ");
				SoundPlayer.stopPlayingSound();
				alert.close();
			}
		};
		task.run();

		try {
			excelView.updateView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayText("An error occurred while updating the Excel view", true);
		}


	}

	@FXML
	void initialize() {
		assert markText != null : "fx:id=\"markText\" was not injected: check your FXML file 'Gui.fxml'.";
		assert hBoxContainer != null : "fx:id=\"hBoxContainer\" was not injected: check your FXML file 'Gui.fxml'.";
		assert fileNameLabel != null : "fx:id=\"fileNameLabel\" was not injected: check your FXML file 'Gui.fxml'.";

		// Menu
		Main.createMenu(menu);
		// Add choose file option

		Menu fileMenu = OtherUtils.getMenuFromBar(menu, "File");

		if(fileMenu!=null){

			MenuItem choose = new MenuItem("Choose File");
			choose.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
			choose.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					chooseFilePressed(null);
				}
			});

			fileMenu.getItems().add(0, choose);
		}

		Menu EditMenu = OtherUtils.getMenuFromBar(menu, "Edit");

		if(EditMenu!=null){

			MenuItem config = new MenuItem("Configure");
			config.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
			config.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					configurePressed(null);
				}
			});

			MenuItem refresh = new MenuItem("Refresh data");
			refresh.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
			refresh.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					loadExcelFile();
				}

			});

			EditMenu.getItems().add(0, config);
			EditMenu.getItems().add(1, refresh);
		}


		// Done with menu

		hBoxContainer.getChildren().addAll(autoTex);
		markText.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					addMarkPressed(null);
				}
			}
		});

		//File Dragging functionality...  START


		mainPane.setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.COPY);
				event.consume();

			}
		});

		mainPane.setOnDragEntered(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {

				event.consume();

			}
		});

		mainPane.setOnDragExited(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {

				//				String s = infoTXT.getText();
				//				infoTXT.setText(s+"\n Drop finished\n");

				event.consume();

			}
		});


		mainPane.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					success = true;

					if(db.getFiles().size()==1){
						File file = db.getFiles().get(0);
						fileNameLabel.setText(file.getName());
						filePath= file.getAbsolutePath();
						System.out.println(filePath);

						//	loadExcelFile();

					}

				}
				event.setDropCompleted(success);
				event.consume();
			}
		});



		//File Dragging functionality...  END

		this.infoText.setFocusTraversable(false);
		
		this.autoTex.requestFocus();


		Student.outText=this;

		String def  = "Welcome to the mark logging program!!\n"
				+ "This program lets you log marks to an excel file\n"
				+ "HOW IT WORKS: You first need to create the column you want the marks to be entered in and specify this column in Settings. "
				+ "Then close the excel file. Drag and drop the selected excel file to which the marks should be added. "
				+ "The student details are now read by the program. You may continue to enter the student name/number "
				+ "and select one of the suggestions by pressing the ENTER key. \n"
				+ "After adding the marks, press the \" Write to file\" button to export the newly added student data to the"
				+ "excel file.\n"
				+ "FYI the excel file should be in a specific format such as the one from the example. Additional settings can be set "
				+ "by using the settings menu. \n\n";
		this.infoText.setText(def);
		//menuBarStuff();
	}

	/**
	 * Refreshes the excelview
	 * @throws Exception 
	 */
	private void refreshSpeadsheet() throws Exception {
		excelView.updateView();

	}

	/**
	 * Loads the excel file from the given path
	 * @throws Exception 
	 */
	private void loadExcelFile(){
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy Reading");
		alert.setHeaderText("Please wait...");

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));	

		alert.setX(Main.primaryStage.getX());
		alert.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);
		//alert.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));

		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				alert.show();
				System.out.println("Started");
				ExcelFileHandler.setFilePath_to_excel(filePath);

				ExcelFileHandler.loadConstantsFromSettings();


				list=ExcelFileHandler.readStudentListFromFile();
				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				SoundPlayer.stopPlayingSound();
				alert.close();
			}

			@Override protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
				System.out.println("Cancelled");
				infoText.appendText("\nAn error ocurred ");
				SoundPlayer.stopPlayingSound();
				alert.close();
			}

			@Override protected void failed() {
				super.failed();
				updateMessage("Failed!");
				System.out.println("Failed");
				infoText.appendText("\nAn error ocurred ");
				SoundPlayer.stopPlayingSound();
				alert.close();
			}
		};
		task.run();


		//						fillbox.setData(list.getObservableList());
		autoTex.getEntries().addAll(list.getObservableList());
		autoTex.setCaseSensitive(false);

		if(excelView == null){
			excelView = new ExcelView(filePath, 0, false);
			excelView.showInNewWindow();
		}
		else{
			try {
				excelView.updateView();
			} catch (Exception e) {
				displayText("An error occured while updating the Spreadsheet", false);
				e.printStackTrace();
			}
		}
	}

	/*	private void menuBarStuff(){
		ObservableList<Menu> menus=menu.getMenus();
		menus.clear();

		Menu filemenu = new Menu("File");

		MenuItem chooseRoot = new MenuItem("Settings");
		chooseRoot.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				System.out.println("ResourcePath = "+getClass().getResource("SettingsGUI.fxml"));
				fxmlLoader.setLocation(settingsResource);
				AnchorPane page = null;

				try {
					page = fxmlLoader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
				SettingsController mc = (SettingsController) fxmlLoader.getController();

				Stage stage = Main.primaryStage;//new Stage(StageStyle.DECORATED);
				Scene scene = new Scene(page);
				scene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());	
				stage.setScene(scene); 


				mc.startOFF();
				stage.show();
			}
		});


		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Platform.exit();
			}

		});
		filemenu.getItems().addAll(chooseRoot,exit);

		menu.getMenus().addAll(filemenu);

	}*/


	@Override
	public void displayText(String text,boolean doublenl) {
		infoText.appendText(text+"\n");	

		if(doublenl){
			infoText.appendText("\n");
		}
	}


}
