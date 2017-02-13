/**
 * Sample Skeleton for 'NameReaderGui.fxml' Controller Class
 */

package excelUtils.nameReader.gui;

import java.io.File;

import excelUtils.nameReader.NameReader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import main.Main;
import marking.SoundPlayer;
import marking.written.gui.ExcelFileHandler;

public class NameReaderGuiController {

	@FXML // fx:id="infoText"
	private TextArea infoText; // Value injected by FXMLLoader

	@FXML // fx:id="sheetIndex"
	private TextField sheetIndex; // Value injected by FXMLLoader

	@FXML // fx:id="chooseFileButton"
	private Button chooseFileButton; // Value injected by FXMLLoader

	@FXML // fx:id="columnIndex"
	private TextField columnIndex; // Value injected by FXMLLoader

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="appendText"
	private TextField appendText; // Value injected by FXMLLoader

	@FXML // fx:id="startRowIndex"
	private TextField startRowIndex; // Value injected by FXMLLoader

	@FXML
	private MenuBar menu;

	private static String filePath;

	@FXML
	void readAppend(ActionEvent event) {

		int sheet = Integer.parseInt(sheetIndex.getText());
		int column = ExcelFileHandler.getExcelColumnIndex(columnIndex.getText());
		int row = Integer.parseInt(startRowIndex.getText())-1;
		String append = appendText.getText();

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy Reading");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				alert.show();
				System.out.println("Started");
				String result = NameReader.readNamesEmail(filePath, sheet, column, row, append);
				infoText.appendText("\n"+result);
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
	void chooseFilePressed(ActionEvent event) {

		File f = ExcelFileHandler.getFile("zip");
		chooseFileButton.setText(f.getName());

		filePath = f.getAbsolutePath();
		System.out.println(filePath);

		infoText.appendText("\nDropped file "+filePath);
	}

	@FXML
	void initialize() {
		assert infoText != null : "fx:id=\"infoText\" was not injected: check your FXML file 'NameReaderGui.fxml'.";
		assert sheetIndex != null : "fx:id=\"sheetIndex\" was not injected: check your FXML file 'NameReaderGui.fxml'.";
		assert chooseFileButton != null : "fx:id=\"chooseFileButton\" was not injected: check your FXML file 'NameReaderGui.fxml'.";
		assert columnIndex != null : "fx:id=\"columnIndex\" was not injected: check your FXML file 'NameReaderGui.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'NameReaderGui.fxml'.";
		assert appendText != null : "fx:id=\"appendText\" was not injected: check your FXML file 'NameReaderGui.fxml'.";
		assert startRowIndex != null : "fx:id=\"startRowIndex\" was not injected: check your FXML file 'NameReaderGui.fxml'.";

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



		//File Dragging functionality...  END


		columnIndex.setText("A");
		startRowIndex.setText("3");
		sheetIndex.setText("0");
		appendText.setText("@sun.ac.za; "); 




		String s = "Well hey!! \n";
		s+=			"You've reached the NameReader tab  \n";
		s+=			"This is used to read a list of names  \n";
		s+=			"or numbers from an excel file.  \n";
		s+=			"Just drag & drop or choose a file,  \n";
		s+=			"Set the parameters and press the button.  \n";
		s+=			" The string will pop up below. \n";
		infoText.setText(s);
	}
}





