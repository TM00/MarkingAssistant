package preloader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import main.Main;

public class PreloadController {

	private Stage stage; // DialogToClose

	@FXML
	void markWrittenTestsPressed(ActionEvent event) {
		if(stage!= null)
			stage.close();
		Main.switchWindows(1);
	}

	@FXML
	void markJavaTestsPressed(ActionEvent event) {
		if(stage!= null)
			stage.close();
		Main.switchWindows(2);
	}

	@FXML
	void markExamScripsPressed(ActionEvent event) {
		if(stage!= null)
			stage.close();
		Main.switchWindows(3);
	}

	@FXML
	void readFromExcelPressed(ActionEvent event) {
		if(stage!= null)
			stage.close();
		Main.switchWindows(4);
	}

	@FXML
	void combineFilePressed(ActionEvent event) {
		if(stage!= null)
			stage.close();
		Main.switchWindows(5);
	}

	@FXML
	void basicSettingsPressed(ActionEvent event) {
		if(stage!= null)
			stage.close();
		Main.switchWindows(6);
	}

	public void setStageToClose(Stage dialog) {
		stage = dialog;		
	}

}
