package marking.exam.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import marking.FileIOManager;
import main.Main;

public class ConfigureController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField numQuestions;

	@FXML
	private TextField surname;

	@FXML
	private TextField initials;

	@FXML
	private ListView<ConfigListData> questionsListView;

	@FXML
	private TextField rowIndex;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private TextField stNum;

	private ObservableList<ConfigListData> data;

	private int numQ;

	private Stage stage;
	private ExamMarksController parent;

	@FXML
	void saveAndReturPressed(ActionEvent event) {
		System.out.println("saveButton");

		Main.configData.questionData = new ArrayList<ConfigListData>(data);
		Main.configData.initialsColumnLetter = initials.getText();
		Main.configData.startRow = Integer.parseInt(rowIndex.getText());
		Main.configData.studentNumberColumnLetter = stNum.getText();
		Main.configData.surnameColumnLetter = surname.getText();

		FileIOManager.writeConfigData();
		parent.doneConfiguring();
		stage.close();

	}

	@FXML
	void initialize() {
		assert numQuestions != null : "fx:id=\"numQuestions\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert surname != null : "fx:id=\"surname\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert initials != null : "fx:id=\"initials\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert questionsListView != null : "fx:id=\"questionsListView\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert rowIndex != null : "fx:id=\"rowIndex\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'ConfigureGui.fxml'.";
		assert stNum != null : "fx:id=\"stNum\" was not injected: check your FXML file 'ConfigureGui.fxml'.";


		numQuestions.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				numQ = Integer.parseInt(numQuestions.getText());
				Main.configData.numQuestions = numQ;
				setupListView(true);
			}
		});

		// Remember old data
		if(Main.configData!=null){

			data = FXCollections.observableList(Main.configData.questionData);
			initials.setText(Main.configData.initialsColumnLetter);
			rowIndex.setText(Main.configData.startRow+"");
			stNum.setText(Main.configData.studentNumberColumnLetter);
			surname.setText(Main.configData.surnameColumnLetter);
			numQuestions.setText(Main.configData.numQuestions+"");

			setupListView(false);
		}
	}

	private void setupListView(boolean renewData){

		if(renewData || data == null)
			data = FXCollections.observableArrayList();

		for (int i = 1; i <= numQ; i++) {
			data.add(new ConfigListData(i));
		}

		questionsListView.setItems(data);
		questionsListView.setCellFactory(new Callback<ListView<ConfigListData>, ListCell<ConfigListData>>() {

			@Override
			public ListCell<ConfigListData> call(ListView<ConfigListData> arg0) {
				return new ListCell<ConfigListData>() {

					@Override
					protected void updateItem(ConfigListData item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {

							Label number = new Label(item.getNumber()+"");


							TextField name = new TextField(item.getName()+"");


							name.setOnKeyReleased(new EventHandler<Event>() {

								@Override
								public void handle(Event arg0) {

									item.setName(name.getText());

								}
							});

							TextField col = new TextField(item.getExcelColumn()+"");

							col.setOnKeyReleased(new EventHandler<Event>() {

								@Override
								public void handle(Event arg0) {

									item.setExcelColumn(col.getText());

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

	public void setStage(Stage dialog) {
		this.stage = dialog;		
	}

	public void setParent(ExamMarksController a){
		parent = a;
	}
}
