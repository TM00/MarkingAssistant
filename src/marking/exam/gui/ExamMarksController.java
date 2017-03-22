package marking.exam.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;
import marking.SoundPlayer;
import marking.exam.ExamStudent;
import marking.exam.ExamStudentList;
import marking.written.gui.AutoCompleteTextField2;
import marking.written.gui.TextOutput;
import res.ResourceLoader;
import utils.CustomTextEventHandler;
import utils.ExcelUtils;
import utils.ExcelView;
import utils.OtherUtils;

public class ExamMarksController implements TextOutput, CustomTextEventHandler{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private VBox hBoxContainer;

	@FXML
	private Button chooseFileButton;

	@FXML
	private TextArea infoText;

	@FXML
	private ListView<StudentExamListData> marksListView;

	@FXML
	private MenuBar menu;


	private static String filePath;
	private static ExamStudentList list;
	//	private final AutoFillTextBox<String> fillbox = new AutoFillTextBox<String>();
	//	private final AutoCompleteTextField autoTex = new AutoCompleteTextField();
	private final AutoCompleteTextField2 autoTex = new AutoCompleteTextField2();
	//private static URL settingsResource;
	private static ExcelView excelView;

	private ObservableList<StudentExamListData> data;

	@FXML
	void configureButtonPressed(ActionEvent event) {

		// Show configure gui...
		final Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(Main.primaryStage);
		dialog.setTitle("Exam Configurations");
		dialog.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));

		dialog.setX(Main.primaryStage.getX());
		dialog.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);

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
		ExamExcelHandler.loadConstantsFromConfigData();

		loadExcelFile();


	}

	@FXML
	void chooseFilePressed(ActionEvent event) {
		/*fillbox.setListLimit(10);

		fillbox.setFilterMode(true);

		fillbox.setMinWidth(hBoxContainer.getWidth());
		hBoxContainer.getChildren().addAll(fillbox);*/

		//		hBoxContainer.getChildren().addAll(autoTex);


		File f = ExamExcelHandler.getFile();
		chooseFileButton.setText(f.getName());

		filePath = f.getAbsolutePath();

		ExamExcelHandler.setFilePath_to_excel(filePath);
		ExamExcelHandler.loadConstantsFromConfigData();


		list=ExamExcelHandler.readExamStudentListFromFile();

		//		fillbox.setData(list.getObservableList());
		autoTex.getEntries().addAll(list.getObservableList());
		autoTex.setCaseSensitive(false);
	}



	@FXML
	void addMarkPressed(ActionEvent event) {



		//		String selected = fillbox.getText();
		String selected = autoTex.getText();
		ExamStudent student = list.getStudent(selected);

		if(student != null){
			int counter =0;
			ConfigListData dat, total = null;
			for (StudentExamListData studentExamListData : data) {
				student.setMark(studentExamListData.getQuestionNumber()+"", studentExamListData.getMark());
				//				excelView.getGrid().setCellValue(student.getRowNumber(), ExcelUtils.getExcelColumnIndex(Main.configData.questionData.get(counter).getExcelColumn()),
				//						studentExamListData.getMark()+"");

				dat = Main.configData.questionData.get(counter);
				System.out.println("name = "+dat.getName());
				if(!dat.getName().equals("TOTAL") && Main.configData.writeQuestions){
					excelView.setCellValue(student.getRowNumber(), ExcelUtils.getExcelColumnIndex(dat.getExcelColumn()),
							studentExamListData.getMark()+"");
				}
				counter++;
			}
			
			// find total
			for(ConfigListData d: Main.configData.questionData){
				if(d.getName().equals("TOTAL")){
					total = Main.configData.questionData.get(counter);
					break;
				}
			}
			
			if(Main.configData.writeTotal && total !=null){
				excelView.setCellValue(student.getRowNumber(), ExcelUtils.getExcelColumnIndex(total.getExcelColumn()),
						student.getTotalMark()+"");
			}

			double percentage = student.getTotalMark()/Main.configData.totalPaper*100;
			DecimalFormat df = new DecimalFormat("##.0");

			infoText.appendText("Total for "+student.getStudentNumber()+" = "+student.getTotalMark()+"\n");
			infoText.appendText("\t Percentage = "+df.format(percentage)+" % "+"\n\n");

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
				ExamExcelHandler.writeExamStudentMarksToFile(list, Main.configData.writeQuestions, Main.configData.writeTotal);

				System.out.println("DONE!!");
				displayText("Succesfully written to the file...",true);	
				displayText("Total students marked = "+list.numMarked(),true);
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


	}

	@FXML
	void initialize() {
		assert hBoxContainer != null : "fx:id=\"hBoxContainer\" was not injected: check your FXML file 'Gui.fxml'.";
		assert marksListView != null : "fx:id=\"marksListView\" was not injected: check your FXML file 'Gui.fxml'.";

		// Menu
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
					configureButtonPressed(null);
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
		Menu helpMenu = OtherUtils.getMenuFromBar(menu, "Help");
		if(helpMenu!=null){

			MenuItem help = new MenuItem("How to use");
			help.setAccelerator(KeyCombination.keyCombination("Shortcut+H"));
			help.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					Main.showHelp(getHelpString());
				}
			});

			helpMenu.getItems().add(0, help);
		}


		// Done with menu
		autoTex.addCustomHandler(this);
		hBoxContainer.getChildren().addAll(autoTex);
		/*	markText.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					addMarkPressed(null);
				}
			}
		});*/

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
						chooseFileButton.setText(file.getName());
						filePath= file.getAbsolutePath();
						System.out.println(filePath);

						displayExcelView();

					}

				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

		//File Dragging functionality...  END

		this.infoText.setFocusTraversable(false);
		this.chooseFileButton.setFocusTraversable(false);
		this.autoTex.requestFocus();

		ExamStudent.outText=this;

		String def  = "Welcome to the exam mark logging program!!\n"
				+ "This program lets you log marks for different\n"
				+ "questions to an excel file\n"
				+ "HOW IT WORKS: You first need to create the column(s) you want the marks to be entered in in your excel file."
				+ "Then close the excel file. Drag and drop the selected excel file to which the marks should be added. "
				+ "\n After loading the file, a display of the file should open. Now you mus press the configure buttom to"
				+ " tell the program where the marks should go and how to locate the candiates with their details. Press save & Return when you're done."
				+ "The student details are now read by the program. You may continue to enter the student name/number "
				+ "and select one of the suggestions by pressing the ENTER key. Now you can enter that student's marks for each question in the table"
				+ "below and press the \"Add Mark\" button to load in into the program. This mark will not be added into the Excel file yet, so you can simply"
				+ " re-enter it if you made a mistake.\n"
				+ "After adding the marks, press the \"Write to file\" button to export the newly added student data to the"
				+ "excel file. This may take a while depending on the amount of students\n"
				+ "FYI the excel file should be in a specific format such as the one from the example. Also keep a copy of the file in case something goes wrong and avoid losing the file. "
				+ "Additional settings can be set "
				+ "by using the settings menu. \n\n";
		this.infoText.setText(def);
		//menuBarStuff();


		/*autoTex.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("Handling auto Event...");
				setupListView();

			}
		});*/


	}

	private void reFreshData(){
		ArrayList<ConfigListData> d = Main.configData.questionData;
		data = FXCollections.observableArrayList();
		data.clear();

		for (ConfigListData c : d) {
			if(!c.getName().equals("TOTAL"))
				data.add(new StudentExamListData(c.getNumber(), c.getName()));
		}
	}

	private void setupListView(){

		reFreshData();

		marksListView.setItems(data);
		marksListView.setCellFactory(new Callback<ListView<StudentExamListData>, ListCell<StudentExamListData>>() {

			@Override
			public ListCell<StudentExamListData> call(ListView<StudentExamListData> arg0) {
				return new ListCell<StudentExamListData>() {

					@Override
					protected void updateItem(StudentExamListData item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {

							Label number = new Label(item.getQuestionNumber()+"");


							Label name = new Label(item.getDescription()+"");

							TextField col = new TextField("");

							col.setOnKeyReleased(new EventHandler<Event>() {

								@Override
								public void handle(Event arg0) {

									try{
										item.setMark(Double.parseDouble(col.getText()));
									}catch(NumberFormatException e){

									}


								}
							});

							number.setPrefWidth(30);
							name.setPrefWidth(90);
							col.setPrefWidth(30);

							HBox hBox = new HBox(number, name,col);

							hBox.setSpacing(10);
							hBox.setAlignment(Pos.CENTER);

							setGraphic(hBox);
						}
					}

				};
			}

		});

		marksListView.requestFocus();
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
				ExamExcelHandler.setFilePath_to_excel(filePath);
				System.out.println("1...");
				ExamExcelHandler.loadConstantsFromConfigData();
				System.out.println("2...");

				list=ExamExcelHandler.readExamStudentListFromFile();
				System.out.println("3...");
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

	private void displayExcelView(){
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

	private String getHelpString(){
		String s="";

		s+="Welcome to the Written help page!\n\n";

		s+="To use the software, follow these steps";

		s+="1. Make sure the column you want to capture the marks to have been created in your Excel file\n\n";
		s+="2. Make sure your Excel file is closed and backed up to avoid corrupting the file\n\n";
		s+="3. Drag and drop your Excel file anywhere into the window, after the drop a loading bar will appear\n\n";
		s+="4. A uneditable preview of the file will open, this may stay open during the capturing. Press the \"Configure\" button and a new window will pop up\n\n";
		s+="5. Enter the column letters where the specific information is located in your file, you may scroll the preview to search where the info is. "
				+ "If not all the info is present, don't panic, only the student number and surname letters are essential. You may simply enter dummy letters to the rest. "
				+ "The row number is the number of the row (indicated on the left on the preview) where the first student information is stored after all the headings etc.\n\n";
		s+="6. Press \"Save and Return\". The number of students recorded will be indicated in the prompt, or inform you when an error has occured\n\n";
		s+="7. Nou you may capture the marks by searching for the students by surname or number in the \"search student\" field. Suggestions will appear and you can scroll"
				+ " with the arrow keys or mouse and select with the ENTER key or double mouse click\n\n";
		s+="8. After the student is located the mark can be entered in the \"Enter mark\" field\n\n";
		s+="9. Repeat steps 7 and 8 for all the written tests\n\n";
		s+="10. After all the marks have been entered, press the \"Write marks to file\" button to export all the captures marks to the Excel file and voila, you're done :)";

		return s;
	}


	@Override
	public void handleCustonTextEvent(String description) {
		System.err.println("Exam reporting for actions... from handleCustonTextEvent des: "+description);
		setupListView();

	}

}
