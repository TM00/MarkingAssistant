package marking;

import java.net.URL;
import java.util.ResourceBundle;

import excelUtils.nameReader.gui.NameReaderGuiController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import main.Main;
import marking.exam.gui.ExamMarksController;
import marking.javaCode.gui.JavaGuiController;
import marking.written.gui.MarksController;
import res.ResourceLoader;

public class TabbedController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane javaAnchor;

	@FXML
	private AnchorPane ExcelAnchor;

	@FXML
	private AnchorPane SettingsAnchor;

	@FXML
	private AnchorPane NameReaderAnchor;
	
	@FXML
	private AnchorPane ExamAnchor;

	@FXML
	private HBox mediaBar;

	@FXML
	private TabPane tabPane;

	@FXML
	void initialize() {
		assert mediaBar != null : "fx:id=\"mediaBar\" was not injected: check your FXML file 'TabbedGui.fxml'.";
		assert javaAnchor != null : "fx:id=\"javaAnchor\" was not injected: check your FXML file 'TabbedGui.fxml'.";
		assert SettingsAnchor != null : "fx:id=\"SettingsAnchor\" was not injected: check your FXML file 'TabbedGui.fxml'.";
		assert NameReaderAnchor != null : "fx:id=\"NameReaderAnchor\" was not injected: check your FXML file 'TabbedGui.fxml'.";
		assert ExcelAnchor != null : "fx:id=\"ExcelAnchor\" was not injected: check your FXML file 'TabbedGui.fxml'.";
		assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'TabbedGui.fxml'.";


		AnchorPane page1, page2, page3, page4, page5;
		try {
			//MarksController.settingsResource=TabbedController.class.getResource("\\written\\gui\\SettingsGui.fxml");
			page1 = (AnchorPane) FXMLLoader.load(MarksController.class.getResource("Gui.fxml"));

			Scene scene = new Scene(page1);
			scene.getStylesheets().add(MarksController.class.getResource("application.css").toExternalForm());

			page2 = (AnchorPane) FXMLLoader.load(JavaGuiController.class.getResource("JavaGui.fxml"));

			Scene scene2 = new Scene(page2);
			scene2.getStylesheets().add(JavaGuiController.class.getResource("application.css").toExternalForm());

			page3 = (AnchorPane) FXMLLoader.load(TabbedController.class.getResource("NewSettingsGui.fxml"));

			Scene scene3 = new Scene(page3);
			scene3.getStylesheets().add(TabbedController.class.getResource("application.css").toExternalForm());
			
			page4 = (AnchorPane) FXMLLoader.load(NameReaderGuiController.class.getResource("NameReaderGui.fxml"));

			Scene scene4 = new Scene(page4);
			scene4.getStylesheets().add(NameReaderGuiController.class.getResource("application.css").toExternalForm());
			
			page5 = (AnchorPane) FXMLLoader.load(ExamMarksController.class.getResource("ExamGui.fxml"));

			Scene scene5 = new Scene(page5);
			scene5.getStylesheets().add(ExamMarksController.class.getResource("application.css").toExternalForm());

			this.ExcelAnchor.getChildren().add(page1);
			this.javaAnchor.getChildren().add(page2);
			this.SettingsAnchor.getChildren().add(page3);
			this.NameReaderAnchor.getChildren().add(page4);
			this.ExamAnchor.getChildren().add(page5);

			setupMediaBar();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// ----------
	// Sound bar stuff
	// -------------


	private static Slider volumeSlider;

	private void setupMediaBar(){

		final Button playButton = new Button(">");
		playButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				updateValues();
				
				if(SoundPlayer.currentPlayer == null && Main.settings.playBackgroundSound){
					SoundPlayer.startBackGroundSound(ResourceLoader.getMusicFolder(),Main.settings.soundVolume/100);
				}
				
				if(SoundPlayer.currentPlayer != null){
					MediaPlayer mp = SoundPlayer.currentPlayer;
					Status status = mp.getStatus();

					if (status == Status.UNKNOWN
							|| status == Status.HALTED) {
						System.out.println("Player is in a bad or unknown state, can't play.");
						return;
					}

					if (status == Status.PAUSED
							|| status == Status.READY
							|| status == Status.STOPPED) {

						mp.play();
						playButton.setText("||");
					} else {
						mp.pause();
						playButton.setText(">");
					}
				}
			}
		});
		final Button skipButton = new Button(">>");
		skipButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				updateValues();
				if(SoundPlayer.currentPlayer != null){

					SoundPlayer.skipBackGroundTrack();
					MediaPlayer mp = SoundPlayer.currentPlayer;
					mp.setVolume(volumeSlider.getValue() / 100.0);
				}
			}
		});

		mediaBar.getChildren().add(playButton);

		Label spacer = new Label("   ");
		spacer.setPrefWidth(3);
		spacer.setMaxWidth(Double.MAX_VALUE);
		mediaBar.getChildren().add(spacer);

		mediaBar.getChildren().add(skipButton);

		Label spacer2 = new Label("   ");
		spacer2.setPrefWidth(3);
		spacer2.setMaxWidth(Double.MAX_VALUE);
		mediaBar.getChildren().add(spacer2);

		volumeSlider = new Slider();
		volumeSlider.setPrefWidth(70);
		volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
		volumeSlider.setMinWidth(30);
		volumeSlider.setPrefHeight(25);
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (volumeSlider.isValueChanging()) {
					MediaPlayer mp = SoundPlayer.currentPlayer;
					mp.setVolume(volumeSlider.getValue() / 100.0);
				}
			}
		});

		volumeSlider.setValue(Main.settings.soundVolume);

		mediaBar.getChildren().add(volumeSlider);

	}


	protected void updateValues() {
		if(SoundPlayer.currentPlayer != null){
			MediaPlayer mp = SoundPlayer.currentPlayer;
			if (volumeSlider != null) {
				Platform.runLater(new Runnable() {

					public void run() {
						if (!volumeSlider.isValueChanging()) {
							volumeSlider.setValue((int) Math.round(mp.getVolume() * 100));
						}
					}
				});
			}
		}
	}


}
