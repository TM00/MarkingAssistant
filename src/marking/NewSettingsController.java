package marking;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.Main;

public class NewSettingsController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;


	@FXML
	private AnchorPane mainPane;

	@FXML
	private TextField checkStringDefault;

	@FXML
	private TextField zipSuffixDefault;

	@FXML
	private TextField appendStringDefault;

	@FXML
	private MenuBar menu;

	@FXML
	private CheckBox playBackground;

	@FXML
	private CheckBox playLoading;

	@FXML
	void saveClicked(ActionEvent event) {


		// Java

		Main.settings.appendString = appendStringDefault.getText();
		Main.settings.CheckString = checkStringDefault.getText();
		Main.settings.zipSuffixString = zipSuffixDefault.getText();

		// Sound

		Main.settings.playBackgroundSound = playBackground.isSelected();
		Main.settings.playLoadingSound = playLoading.isSelected();

		FileIOManager.writeSettings();

	}


	@FXML
	void initialize() {
		assert playBackground != null : "fx:id=\"playBackground\" was not injected: check your FXML file 'NewSettingsGui.fxml'.";
		assert playLoading != null : "fx:id=\"playLoading\" was not injected: check your FXML file 'NewSettingsGui.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'NewSettingsGui.fxml'.";
		assert checkStringDefault != null : "fx:id=\"checkStringDefault\" was not injected: check your FXML file 'NewSettingsGui.fxml'.";
		assert zipSuffixDefault != null : "fx:id=\"zipSuffixDefault\" was not injected: check your FXML file 'NewSettingsGui.fxml'.";
		assert appendStringDefault != null : "fx:id=\"appendStringDefault\" was not injected: check your FXML file 'NewSettingsGui.fxml'.";

		// Menu
		Main.createMenu(menu);


		// Java

		this.appendStringDefault.setText(Main.settings.appendString);
		this.checkStringDefault.setText(Main.settings.CheckString);
		this.zipSuffixDefault.setText(Main.settings.zipSuffixString);

		// Sound

		this.playBackground.setSelected(Main.settings.playBackgroundSound);
		this.playLoading.setSelected(Main.settings.playLoadingSound);

	}
}
