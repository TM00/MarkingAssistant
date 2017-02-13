package marking.exam.gui;

import java.io.File;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
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
import marking.written.Student;
import marking.written.gui.AutoCompleteTextField2;
import marking.written.gui.ExcelFileHandler;
import marking.written.gui.TextOutput;
import res.ResourceLoader;

public class ExamMarksController implements TextOutput{

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

	private ObservableList<StudentExamListData> data;

	@FXML
	void configureButtonPressed(ActionEvent event) {

		// Show configure gui...
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.primaryStage);
		dialog.setTitle("Exam Configurations");
		dialog.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("ConfigureGui.fxml"));

			AnchorPane page = (AnchorPane) fxmlLoader.load();
			ConfigureController fooController = (ConfigureController) fxmlLoader.getController();
			fooController.setStage(dialog);		
			fooController.setParent(this);
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


	}

	@FXML
	void chooseFilePressed(ActionEvent event) {
		/*fillbox.setListLimit(10);

		fillbox.setFilterMode(true);

		fillbox.setMinWidth(hBoxContainer.getWidth());
		hBoxContainer.getChildren().addAll(fillbox);*/

		//		hBoxContainer.getChildren().addAll(autoTex);


		File f = ExcelFileHandler.getFile();
		chooseFileButton.setText(f.getName());

		filePath = f.getAbsolutePath();

		ExcelFileHandler.setFilePath_to_excel(filePath);
		ExcelFileHandler.loadConstantsFromSettings();


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
			for (StudentExamListData studentExamListData : data) {
				student.addMark(studentExamListData.getQuestionNumber()+"", studentExamListData.getMark());
			}

			infoText.appendText("Total for "+student.getStudentNumber()+" = "+student.getTotalMark()+"\n\n");

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
				ExamExcelHandler.writeExamStudentMarksToFile(list);

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
		Main.createMenu(menu);
		hBoxContainer.getChildren().addAll(autoTex);
		/*	markText.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					addMarkPressed(null);
				}
			}
		});
		 */

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

						if(Main.settings.playLoadingSound)
							SoundPlayer.startLoopSound();
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Busy Reading");
						alert.setHeaderText("Please wait...");
						Task<Integer> task = new Task<Integer>() {
							@Override protected Integer call() throws Exception {

								alert.show();
								System.out.println("Started");
								ExamExcelHandler.setFilePath_to_excel(filePath);

								ExamExcelHandler.loadConstantsFromConfigData();


								list=ExamExcelHandler.readExamStudentListFromFile();
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


		autoTex.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("Handling auto Event...");
				setupListView();

			}
		});


	}

	private void reFreshData(){
		ArrayList<ConfigListData> d = Main.configData.questionData;
		data = FXCollections.observableArrayList();
		data.clear();

		for (ConfigListData c : d) {
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
