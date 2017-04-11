package marking.javaCode.gui;

import java.io.File;
import java.net.MalformedURLException;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import marking.SoundPlayer;
import marking.javaCode.MarkingHelper;
import marking.written.gui.ExcelFileHandler;
import res.ResourceLoader;
import utils.MarkException;
import utils.StudentNumberException;

public class JavaGuiController {


	private static MarkingHelper markingHelper;

	@FXML
	private TextArea infoText;

	@FXML
	private Button zipUp;

	@FXML
	private TextField checkString;

	@FXML
	private Button chooseFileButton;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private MenuBar menu;

	@FXML
	private TextField appendText;

	@FXML
	private TextField suffix;

	@FXML
	private CheckBox appendBox;

	@FXML
	private CheckBox unzipBox;

	@FXML
	private CheckBox deleteBox;

	@FXML
	private TextField extensionText;

	@FXML
	private CheckBox openBox;

	@FXML
	private CheckBox openNotFoundFilesBox;

	@FXML
	void chooseFilePressed(ActionEvent event) {

		File f = ExcelFileHandler.getFile("zip");
		chooseFileButton.setText(f.getName());

		String filePath = f.getAbsolutePath();
		System.out.println(filePath);
		markingHelper = new MarkingHelper(filePath);

		infoText.appendText("\nDropped file "+filePath);
	}

	@FXML
	void perFromActions(ActionEvent event) {
		/*	SoundPlayer.startLoopSound();
		this.unZip(null);
		this.append(null);
		this.open(null);
		SoundPlayer.stopPlayingSound();*/
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy with tasks");
		alert.setHeaderText("Please wait...");

		File f = ResourceLoader.getRandomGif();
		Image image;
		try {
			image = new Image(f.toURI().toURL().toString());
			ImageView view = new ImageView(image);

			alert.setGraphic(view);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true); // Disable the button
		Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
		stage1.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));	

		alert.setX(Main.primaryStage.getX());
		alert.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);
		alert.show();

		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				if(Main.settings.playLoadingSound){
					System.err.println("Starting play....... unZip");
					//		SoundPlayer.startLoopSound(ResourceLoader.getMusicFile("loading.mp3"));
					//	SoundPlayer.startLoopSound();
				}
				//	System.out.println("Started");

				if(markingHelper.isZipFolderNull()){
					unzipBox.setSelected(false);
				}

				if(unzipBox.isSelected()){
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							alert.setTitle("Unzipping...");

						}
					});
					markingHelper.unZip();
				}

				if(deleteBox.isSelected()){
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							alert.setTitle("Deleting...");

						}
					});
					String extension = extensionText.getText();
					markingHelper.deleteFilesNotExtension(extension);
				}

				if(appendBox.isSelected()){
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							alert.setTitle("Appending...");

						}
					});
					String append = appendText.getText();
					markingHelper.append(append);
				}

				if(openBox.isSelected()){
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							alert.setTitle("Opening...");

						}
					});
					markingHelper.justOpen();
				}

				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");

				SoundPlayer.stopPlayingSound();
				Platform.runLater(() -> {
					infoText.appendText("\n Finished ");
					alert.close();
				});
			}

			@Override protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
				System.out.println("Cancelled");
				SoundPlayer.stopPlayingSound();
				Platform.runLater(() -> {
					alert.close();
				});
			}

			@Override protected void failed() {
				super.failed();
				updateMessage("Failed!");
				System.out.println("Failed");

				SoundPlayer.stopPlayingSound();
				Platform.runLater(() -> {
					infoText.appendText("\nAn error ocurred ");
					alert.close();
				});
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(false);
		th.start();
	}

	@FXML
	void check(ActionEvent event) {
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy checking");
		alert.setHeaderText("Please wait...");
		File f = ResourceLoader.getRandomGif();
		Image image;
		try {
			image = new Image(f.toURI().toURL().toString());
			ImageView view = new ImageView(image);

			alert.setGraphic(view);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true); // Disable the button
		Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
		stage1.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));	

		alert.setX(Main.primaryStage.getX());
		alert.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);
		alert.show();

		Task<Integer> task = new Task<Integer>() {
			String res;
			@Override protected Integer call() throws Exception {

				System.out.println("Started");
				String check = checkString.getText();
				res =markingHelper.checkForString(check,openNotFoundFilesBox.isSelected());
				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				Platform.runLater(() -> {
					infoText.appendText("\n Check for String result: \n"+res);

					if(!res.equals("FOUND "+checkString.getText()+ " in ALL FILES")){
						String str = res;
						String findStr = checkString.getText();
						int lastIndex = 0;
						int count = 0;

						while(lastIndex != -1){

							lastIndex = str.indexOf(findStr,lastIndex);

							if(lastIndex != -1){
								count ++;
								lastIndex += findStr.length();
							}
						}

						infoText.appendText("\nNot found in "+count+" files!!");

					}

					SoundPlayer.stopPlayingSound();
					infoText.appendText("\nDone checking ");
					alert.close();
				});
			}

			@Override protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
				System.out.println("Cancelled");
				Platform.runLater(() -> {
					infoText.appendText("\nAn error ocurred ");
					SoundPlayer.stopPlayingSound();
					alert.close();
				});
			}

			@Override protected void failed() {
				super.failed();
				updateMessage("Failed!");
				System.out.println("Failed");
				Platform.runLater(() -> {
					infoText.appendText("\nAn error ocurred ");
					SoundPlayer.stopPlayingSound();
					alert.close();
				});
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(false);
		th.start();


	}

	@FXML
	void read(ActionEvent event) {

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy reading");
		alert.setHeaderText("Please wait...");

		File f = ResourceLoader.getRandomGif();
		Image image;
		try {
			image = new Image(f.toURI().toURL().toString());
			ImageView view = new ImageView(image);

			alert.setGraphic(view);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true); // Disable the button
		Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
		stage1.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));	

		alert.setX(Main.primaryStage.getX());
		alert.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);
		alert.show();

		Task<Integer> task = new Task<Integer>() {
			String res;
			@Override protected Integer call() throws Exception {

				try {

					System.out.println("Started");
					res =markingHelper.readMarks();

				} catch (MarkException e) {

					infoText.appendText("\nA mark error occured");
					infoText.appendText("\n"+e.getMessage());
					this.cancel();
				}
				catch (StudentNumberException e) {

					infoText.appendText("\nA student number error occured");
					infoText.appendText("\n"+e.getMessage());
					this.cancel();
				}
				return new Integer(2);

			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				Platform.runLater(() -> {
					infoText.appendText("\n "+res);
					infoText.appendText("\nDone reading the marks ");
					SoundPlayer.stopPlayingSound();
					alert.close();
				});
			}

			@Override protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
				System.out.println("Cancelled");
				Platform.runLater(() -> {
					infoText.appendText("\nAn error ocurred ");
					SoundPlayer.stopPlayingSound();
					alert.close();
				});
			}

			@Override protected void failed() {
				super.failed();
				updateMessage("Failed!");
				System.out.println("Failed");
				Platform.runLater(() -> {
					infoText.appendText("\nAn error ocurred ");
					SoundPlayer.stopPlayingSound();
					alert.close();
				});
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(false);
		th.start();


	}

	@FXML
	void zipUp(ActionEvent event) {

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Zipping Up");
		alert.setHeaderText("Please wait...");
		File f = ResourceLoader.getRandomGif();
		Image image;
		try {
			image = new Image(f.toURI().toURL().toString());
			ImageView view = new ImageView(image);

			alert.setGraphic(view);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true); // Disable the button
		Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
		stage1.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));	

		alert.setX(Main.primaryStage.getX());
		alert.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);
		alert.show();

		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				System.out.println("Started");			
				markingHelper.zipFolder(suffix.getText());
				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				Platform.runLater(() -> {

					infoText.appendText("\nDone zipping ");
					alert.close();
					SoundPlayer.stopPlayingSound();
				});
			}

			@Override protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
				System.out.println("Cancelled");
				Platform.runLater(() -> {
					infoText.appendText("\nAn error ocurred ");
					SoundPlayer.stopPlayingSound();
					alert.close();
				});
			}

			@Override protected void failed() {
				super.failed();
				updateMessage("Failed!");
				System.out.println("Failed");
				Platform.runLater(() -> {
					infoText.appendText("\nAn error ocurred ");
					SoundPlayer.stopPlayingSound();
					alert.close();
				});
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(false);
		th.start();

	}

	@FXML
	void initialize() {

		assert infoText != null : "fx:id=\"infoText\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert appendBox != null : "fx:id=\"appendBox\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert unzipBox != null : "fx:id=\"unzipBox\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert extensionText != null : "fx:id=\"extensionText\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert openBox != null : "fx:id=\"openBox\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert checkString != null : "fx:id=\"checkString\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert chooseFileButton != null : "fx:id=\"chooseFileButton\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert appendText != null : "fx:id=\"appendText\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert suffix != null : "fx:id=\"suffix\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert menu != null : "fx:id=\"menu\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert deleteBox != null : "fx:id=\"deleteBox\" was not injected: check your FXML file 'JavaGui.fxml'.";
		assert openNotFoundFilesBox != null : "fx:id=\"openNotFoundFilesBox\" was not injected: check your FXML file 'JavaGui.fxml'.";

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
						String filePath= file.getAbsolutePath();
						System.out.println(filePath);
						markingHelper = new MarkingHelper(filePath);

						if(markingHelper.isZipFolderNull()){
							unzipBox.setSelected(false);
						}

						infoText.appendText("\nDropped file "+filePath);
					}

				}
				event.setDropCompleted(success);
				event.consume();
			}
		});



		//File Dragging functionality...  END

		this.infoText.setFocusTraversable(false);
		this.chooseFileButton.setFocusTraversable(false);


		String def  = "Welcome to the Java Code marking tab!!\n"
				+ "This program lets you unzip files and open them with NotePad++\n\n"
				+ "HOW IT WORKS: \n"
				+ "Drag and drop the zip file containing the code (.java files)\n"
				+ "Input the needed parameters and select one of the options\n"
				+ "to unzip the file. It is unzipped in the same folder you dragged it from.\n"
				+ "After marking, you may check if all the files have been marked\n"
				+ "By checking for the specific String in each file.\n"
				+ "You can see the marks each student received with the read and show marks button\n"
				+ "Finally you can re-zip the marked folder, with a specified suffix added (needed to avoid duplicate folder names)\n"
				+ "\n";
		this.infoText.setText(def);

		this.appendText.setText(Main.settings.appendString);
		this.checkString.setText(Main.settings.CheckString);
		this.suffix.setText(Main.settings.zipSuffixString);

	}

}
