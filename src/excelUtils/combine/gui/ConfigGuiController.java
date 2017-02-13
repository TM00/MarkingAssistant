package excelUtils.combine.gui;

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

public class ConfigGuiController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField numColumns;

	@FXML
	private TextField searchText;

	@FXML
	private TextField rowIndexText;

	@FXML
	private TextField sheetIndexText;


	@FXML
	private ListView<CombineConfigData> columnsListView;

	@FXML
	private AnchorPane mainPane;

	private int numC;

	private Stage stage;
	private CombineGuiController parent;

	@FXML
	void saveAndReturPressed(ActionEvent event) {
		System.out.println("saveButton");

		FileIOManager.writeConfigData();
		String searchCol = searchText.getText();
		int startrow = Integer.parseInt(rowIndexText.getText());
		int sheet = Integer.parseInt(sheetIndexText.getText());

		ArrayList<String> comCols = new ArrayList<>();

		for (CombineConfigData d : columnsListView.getItems()) {
			comCols.add(d.getColumn());
		}

		parent.doneConfiguring(searchCol, comCols, startrow, sheet);
		stage.close();
	}

	@FXML
	void initialize() {
		assert searchText != null : "fx:id=\"searchText\" was not injected: check your FXML file 'ConfigGui.fxml'.";
		assert sheetIndexText != null : "fx:id=\"sheetIndexText\" was not injected: check your FXML file 'ConfigGui.fxml'.";
		assert columnsListView != null : "fx:id=\"columnsListView\" was not injected: check your FXML file 'ConfigGui.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'ConfigGui.fxml'.";
		assert rowIndexText != null : "fx:id=\"rowIndexText\" was not injected: check your FXML file 'ConfigGui.fxml'.";
		assert numColumns != null : "fx:id=\"numColumns\" was not injected: check your FXML file 'ConfigGui.fxml'.";

		numColumns.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				numC = Integer.parseInt(numColumns.getText());

				setupListView();
			}
		});

	}

	private void setupListView(){

		ObservableList<CombineConfigData> data = FXCollections.observableArrayList();

		for (int i = 1; i <= numC; i++) {
			data.add(new CombineConfigData(i));
		}

		columnsListView.setItems(data);
		columnsListView.setCellFactory(new Callback<ListView<CombineConfigData>, ListCell<CombineConfigData>>() {

			@Override
			public ListCell<CombineConfigData> call(ListView<CombineConfigData> arg0) {
				return new ListCell<CombineConfigData>() {

					@Override
					protected void updateItem(CombineConfigData item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {

							Label number = new Label(item.getNumber()+"");




							TextField col = new TextField(item.getColumn()+"");

							col.setOnKeyReleased(new EventHandler<Event>() {

								@Override
								public void handle(Event arg0) {

									item.setColumn(col.getText());

								}
							});

							number.setPrefWidth(30);
							col.setPrefWidth(30);

							HBox hBox = new HBox(number,col);

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

	public void setParent(CombineGuiController a){
		parent = a;
	}
}