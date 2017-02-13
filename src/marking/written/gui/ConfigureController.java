package marking.written.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import marking.FileIOManager;
import marking.exam.gui.ExamMarksController;

public class ConfigureController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="surname"
	private TextField surname; // Value injected by FXMLLoader

	@FXML // fx:id="initials"
	private TextField initials; // Value injected by FXMLLoader

	@FXML // fx:id="rowIndex"
	private TextField rowIndex; // Value injected by FXMLLoader

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="mark"
	private TextField mark; // Value injected by FXMLLoader

	@FXML // fx:id="stNum"
	private TextField stNum; // Value injected by FXMLLoader

	private Stage stage;
	private MarksController parent;

	@FXML
	void saveClicked(ActionEvent event) {


		// Excel
		Main.settings.initialsColumnLetter = this.initials.getText();
		Main.settings.surnameColumnLetter = this.surname.getText();
		Main.settings.studentNumberColumnLetter = this.stNum.getText();
		Main.settings.markColumnLetter = this.mark.getText();
		Main.settings.startRow = Integer.parseInt(this.rowIndex.getText());

		FileIOManager.writeSettings();

		parent.doneConfiguring();
		stage.close();

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert surname != null : "fx:id=\"surname\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert initials != null : "fx:id=\"initials\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert rowIndex != null : "fx:id=\"rowIndex\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert mark != null : "fx:id=\"mark\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert stNum != null : "fx:id=\"stNum\" was not injected: check your FXML file 'ConfigureGui.fxml'.";

	}

	public void setStage(Stage dialog) {
		this.stage = dialog;		
	}

	public void setParent(MarksController a){
		parent = a;
	}

	/**
	 * loads saved data from settings
	 */
	public void loadData() {
		surname.setText(Main.settings.surnameColumnLetter);
		initials.setText(Main.settings.initialsColumnLetter);
		rowIndex.setText(""+Main.settings.startRow);
		mark.setText(Main.settings.markColumnLetter);
		stNum.setText(Main.settings.studentNumberColumnLetter);

	}
}
