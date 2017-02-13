package marking.javaCode.gui;

import java.io.File;

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
	void chooseFilePressed(ActionEvent event) {

		File f = ExcelFileHandler.getFile("zip");
		chooseFileButton.setText(f.getName());

		String filePath = f.getAbsolutePath();
		System.out.println(filePath);
		markingHelper = new MarkingHelper(filePath);

		infoText.appendText("\nDropped file "+filePath);
	}

	@FXML
	void unzipAppendOpen(ActionEvent event) {
		/*	SoundPlayer.startLoopSound();
		this.unZip(null);
		this.append(null);
		this.open(null);
		SoundPlayer.stopPlayingSound();*/
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy wityh tasks");
		alert.setHeaderText("Please wait...");

		//Image image1 = new Image(ResourceLoader.getFileInputStream("477.GIF"));
		//ImageView imageView = new ImageView(image1);
		//alert.setGraphic(imageView);

		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				if(Main.settings.playLoadingSound){
					System.err.println("Starting play....... unZip");
					//		SoundPlayer.startLoopSound(ResourceLoader.getMusicFile("loading.mp3"));
					//	SoundPlayer.startLoopSound();
				}
				alert.show();
				System.out.println("Started");
				markingHelper.unZip();
				String append = appendText.getText();
				markingHelper.append(append);
				markingHelper.justOpen();


				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				infoText.appendText("\n Finished ");
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
	void unZip(ActionEvent event) {

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy unZipping");
		alert.setHeaderText("Please wait...");

		//Image image1 = new Image(ResourceLoader.getFileInputStream("477.GIF"));
		//ImageView imageView = new ImageView(image1);
		//alert.setGraphic(imageView);

		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				alert.show();
				System.out.println("Started");
				markingHelper.unZip();


				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				infoText.appendText("\nUnzipped ");
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
	void append(ActionEvent event) {

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy appending files");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				alert.show();
				System.out.println("Started");
				String append = appendText.getText();
				markingHelper.append(append);
				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				infoText.appendText("\nAppended ");
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
	void open(ActionEvent event) {
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy Opening");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				alert.show();
				System.out.println("Started");
				markingHelper.justOpen();

				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				infoText.appendText("\nOpened ");
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
	void unzipAppend(ActionEvent event) {
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy wityh tasks");
		alert.setHeaderText("Please wait...");

		//Image image1 = new Image(ResourceLoader.getFileInputStream("477.GIF"));
		//ImageView imageView = new ImageView(image1);
		//alert.setGraphic(imageView);

		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				if(Main.settings.playLoadingSound){
					System.err.println("Starting play....... unZip");
					//		SoundPlayer.startLoopSound(ResourceLoader.getMusicFile("loading.mp3"));
					//	SoundPlayer.startLoopSound();
				}
				alert.show();
				System.out.println("Started");
				markingHelper.unZip();
				String append = appendText.getText();
				markingHelper.append(append);

				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
				infoText.appendText("\n Finished ");
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
	void check(ActionEvent event) {
		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy checking");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			String res;
			@Override protected Integer call() throws Exception {

				alert.show();
				System.out.println("Started");
				String check = checkString.getText();
				res =markingHelper.checkForString(check);
				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");
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
	void read(ActionEvent event) {

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy reading");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			String res;
			@Override protected Integer call() throws Exception {

				try {

					alert.show();
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
				infoText.appendText("\n "+res);
				infoText.appendText("\nDone reading the marks ");
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
	void zipUp(ActionEvent event) {

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Zipping Up");
		alert.setHeaderText("Please wait...");
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {

				alert.show();
				System.out.println("Started");			
				markingHelper.zipFolder(suffix.getText());
				return new Integer(2);
			}

			@Override protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
				System.out.println("Done!");


				infoText.appendText("\nDone zipping ");
				alert.close();
				SoundPlayer.stopPlayingSound();
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
