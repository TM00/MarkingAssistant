package excelUtils.combine.gui;

import java.io.File;

import excelUtils.combine.CombineFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class CombineListCell extends ListCell<CombineFile> {  

	private static final String STYLE = "combine-list";

	private CombineFile combineFile;

	private HBox root;
	private Label name;
	private Button configureButton;
	private TextField infoText;




	public CombineListCell() {
		//combineFile = new CombineFile(filePath);
		initialize();


	}

	private void initialize(){
		root = new HBox();
		name  = new Label("Name here");

		configureButton = new Button("Configure");
		configureButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				configurePressed();				
			}
		});
		infoText = new TextField();

		//root.getChildren().addAll(name, configureButton, infoText);
		root.getChildren().addAll(name);

		root.getStyleClass().add(STYLE);
		name.getStyleClass().add(STYLE);
		configureButton.getStyleClass().add(STYLE);
		infoText.getStyleClass().add(STYLE);

		setGraphic(root);
	}


	private void configurePressed(){
		// TODO
	}


	@Override
	protected void updateItem(CombineFile file, boolean empty) {
		super.updateItem(file, empty);

		if (empty) {
			clearContent();
		} else {
			addContent(file);
		}
	}

	private void clearContent() {
		setText(null);
		setGraphic(null);
	}

	private void addContent(CombineFile file) {
		setText(null);
		combineFile  =file;
		
		String fileName = new File(combineFile.getPath()).getName();
		String nameT = fileName.substring(0, fileName.lastIndexOf("."));
		name.setText(nameT);
		setGraphic(root);

	}



}
