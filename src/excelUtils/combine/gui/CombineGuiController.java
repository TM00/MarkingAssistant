package excelUtils.combine.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import excelUtils.combine.CombineFile;
import excelUtils.combine.ExcelCombiner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import marking.SoundPlayer;
import marking.written.gui.ExcelFileHandler;
import utils.ExcelUtils;

public class CombineGuiController {

	@FXML
	private TextArea infoText;

	@FXML
	private Button chooseFileButton;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private MenuBar menu;

	@FXML
	private ListView<CombineFile> fileListView;

	private static String filePath;

	private static String searchcCol;
	private static ArrayList<String> indices;
	private static int startRowIndex, sheetIndex;

	@FXML
	void chooseFilePressed(ActionEvent event) {

		File f = ExcelFileHandler.getFile();
		chooseFileButton.setText(f.getName());

		filePath = f.getAbsolutePath();
	}

	@FXML
	void configureSettingsPressed(ActionEvent event) {

		// Show configure gui...
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.primaryStage);
		dialog.setTitle("Exam Configurations");

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("ConfigGui.fxml"));

			AnchorPane page = (AnchorPane) fxmlLoader.load();
			page.getStylesheets().add(CombineGuiController.class.getResource("application.css").toExternalForm());
			ConfigGuiController fooController = (ConfigGuiController) fxmlLoader.getController();
			fooController.setStage(dialog);		
			fooController.setParent(this);
			Scene dialogScene = new Scene(page);

			dialog.setScene(dialogScene);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void combinePressed(ActionEvent event) {


		// Combine with dialog
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy Writing");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				alert.show();
				System.out.println("Started");

				int searchIndex = ExcelUtils.getExcelColumnIndex(searchcCol);
				ArrayList<Integer> indicesI = new ArrayList<>();

				for (String string : indices) {
					indicesI.add(ExcelUtils.getExcelColumnIndex(string));
				}

				CombineFile cf = new CombineFile(filePath, searchIndex, indicesI);
				cf.setStartRow(startRowIndex);
				cf.setSheetIndex(sheetIndex);

				ExcelCombiner combiner = new ExcelCombiner(cf);

				for(CombineFile cfile: fileListView.getItems()){
					System.out.println(cfile);
					cfile.setSearchColumnIndex(searchIndex);
					cfile.setMergeIndices(indicesI);
					cfile.setStartRow(startRowIndex);
					cfile.setSheetIndex(sheetIndex);

					combiner.addFile(cfile);
				}

				combiner.combine();

				System.out.println("DONE!!");
				displayText("\nSuccesfullycombined the files...",true);	
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
		assert infoText != null : "fx:id=\"infoText\" was not injected: check your FXML file 'CombineGui.fxml'.";
		assert chooseFileButton != null : "fx:id=\"chooseFileButton\" was not injected: check your FXML file 'CombineGui.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'CombineGui.fxml'.";
		assert menu != null : "fx:id=\"menu\" was not injected: check your FXML file 'CombineGui.fxml'.";
		assert fileListView != null : "fx:id=\"fileListView\" was not injected: check your FXML file 'CombineGui.fxml'.";

		ObservableList<CombineFile> data = FXCollections.observableArrayList();
		data.clear();
		fileListView.setItems(data);
		fileListView.setCellFactory((ListView<CombineFile> l) -> new CombineListCell());

		// Menu
		Main.createMenu(menu);
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
						infoText.appendText("\nDropped file "+filePath);
						//	System.out.println(filePath);

					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});


		// ListView

		fileListView.setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.COPY);
				event.consume();

			}
		});

		fileListView.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					success = true;

					for(File f: db.getFiles()){

						if(f.getName().toLowerCase().endsWith(".xlsx")){ // only Excel files
							//							System.out.println("hello peeps!");
							fileListView.getItems().add(new CombineFile(f.getPath()));
						}

					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

		//File Dragging functionality...  END


		String s = "Well hey!! \n";
		s+=			"You've reached the NameReader tab  \n";
		s+=			"This is used to read a list of names  \n";
		s+=			"or numbers from an excel file.  \n";
		s+=			"Just drag & drop or choose a file,  \n";
		s+=			"Set the parameters and press the button.  \n";
		s+=			" The string will pop up below. \n";
		infoText.setText(s);
	}

	private void setupListView(){

		// los vir eers
	}

	public void doneConfiguring(String searchCol, ArrayList<String> comCols, int startrow, int sheet) {

		searchcCol = searchCol;
		indices = comCols;
		startRowIndex = startrow;
		sheetIndex=sheet;
	}

	public void displayText(String text,boolean doublenl) {
		infoText.appendText(text+"\n");	

		if(doublenl){
			infoText.appendText("\n");
		}
	}

}
