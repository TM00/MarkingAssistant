package utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Main;
import marking.SoundPlayer;
import marking.written.gui.ExcelFileHandler;
import res.ResourceLoader;

/**
 * This class allows for the displaying of excel files within a window by
 * utilizing SpreadsheetView from controlsFX and the reading capabilities of the POI library.
 * 
 * Only usable for .xlsx files
 *  
 * @author Theo
 *
 */
public class ExcelView {

	/**
	 * Path to Excel
	 */
	private String filePath;

	private int sheetIndex;

	private boolean editible;

	private FileInputStream inStream;
	private XSSFWorkbook poiWorkbook;
	private XSSFSheet poiSheet;

	private SpreadsheetView theView;

	private Stage stage;

	public ExcelView(String path, int sheetIndex ,boolean editable){
		filePath  =path;
		this.editible  =editable;
		this.sheetIndex  =sheetIndex;
	}

	public ExcelView(String path, int sheetIndex){
		filePath  =path;
		this.editible  =false;
		this.sheetIndex = sheetIndex;
	}

	private void initializeView() throws Exception{
		GridBase grid = excelToGrid();

		theView = new SpreadsheetView(grid);
		theView.setEditable(editible);
	}

	public SpreadsheetView getView() throws Exception{
		initializeView();
		return theView;
	}

	public void showInNewWindow(){

		if(Main.settings.playLoadingSound)
			SoundPlayer.startLoopSound();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Busy Reading");
		alert.setHeaderText("Please wait...");

		Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
		stage1.getIcons().addAll(ResourceLoader.getIcons("check_mark.ico"));	

		alert.setX(Main.primaryStage.getX());
		alert.setY(Main.primaryStage.getY()+Main.primaryStage.getWidth()/2);


		try {

			Task<Integer> task = new Task<Integer>() {
				@Override protected Integer call() throws Exception {
					try {
						alert.show();
						initializeView();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}


					return new Integer(2);
				}

				@Override protected void succeeded() {

					super.succeeded();
					updateMessage("Done!");
					System.out.println("Done!");

					try{

						Parent root;
						root = theView;
						stage = new Stage();
						Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

						// stage positioning
						stage.setX(primaryScreenBounds.getMinX());
						stage.setY(primaryScreenBounds.getMinY());
						stage.setWidth(primaryScreenBounds.getWidth()/2);
						stage.setHeight(primaryScreenBounds.getHeight());

						stage.setTitle(new File(filePath).getName());
						stage.setScene(new Scene(root));

						stage.getIcons().addAll(ResourceLoader.getIcons("Excel.ico"));		

						stage.initModality(Modality.NONE);
						stage.show();

						SoundPlayer.stopPlayingSound();
						alert.close();

					}catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override protected void cancelled() {
					super.cancelled();
					updateMessage("Cancelled!");
					System.out.println("Cancelled");
					SoundPlayer.stopPlayingSound();
					alert.close();
				}

				@Override protected void failed() {
					super.failed();
					updateMessage("Failed!");
					System.out.println("Failed");
					SoundPlayer.stopPlayingSound();
					alert.close();

				}
			};
			task.run();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the values in the view. This may happen after the Excel file has been 
	 * modified after the initial reading.
	 * @throws Exception 
	 */
	public void updateView() throws Exception{
		GridBase newgrid = excelToGrid();

		theView.setGrid(newgrid);
	}

	public Grid getGrid(){
		return theView.getGrid();
	}

	/**
	 * Creates a {@link GridBase} object from the excel file located at the path
	 * @return
	 * @throws Exception - when opening the file
	 */
	private GridBase excelToGrid() throws Exception{

		// Read the Excel document and collect the rows
		openBook();
		poiSheet = poiWorkbook.getSheetAt(sheetIndex);

		int[] size =  getSize();
		GridBase grid = new GridBase(size[0], size[1]);

		ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

		Row poiRow;
		Cell cell;
		String value;
		FormulaEvaluator evaluator = poiWorkbook.getCreationHelper().createFormulaEvaluator();

		for (int row = 0; row < grid.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			poiRow = poiSheet.getRow(row);

			if(poiRow == null){
				for (int column = 0; column < grid.getColumnCount(); ++column) {

					value = "";
					list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,value));
				}
			}
			else{
				for (int column = 0; column < grid.getColumnCount(); ++column) {

					cell = poiRow.getCell(column);
					value = ExcelUtils.cellStringValue(evaluator,cell).trim();

					list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,value));
				}
			}
			rows.add(list);
		}
		grid.setRows(rows);

		closeBook();

		return grid;
	}

	/**
	 * Calculates the number of rows and columns in the sheet by looping 
	 * and reading all the things :) 
	 * @return the size as int[{rows, cols}]
	 */
	private int[] getSize(){

		int numRows = 0;
		int numCols =0;

		int nullRowCounter = 0;
		int nullColCounter = 0;

		int maxNullRows = 6;
		int maxNullCols = 6;

		Row row;
		Cell cell;
		int localColCounter;

		while(true){

			row= poiSheet.getRow(numRows);
			numRows++;

			// Check row...
			if(row == null){
				nullRowCounter++;
			}
			else{
				nullRowCounter = 0;
				// If row not null, check columns...
				localColCounter = 0;
				while(true){
					cell = row.getCell(localColCounter);
					localColCounter++;
					if(cell==null){
						nullColCounter++;
					}else{
						nullColCounter = 0;
					}

					if(nullColCounter == maxNullCols){
						// reached max null cells
						localColCounter -= maxNullCols;

						if(localColCounter >  numCols)
							numCols = localColCounter;

						break;
						// go to next row...
					}

				}
			}

			if(nullRowCounter == maxNullRows){
				// reached max null rows
				numRows -= maxNullRows;

				break;

			}
		}
		return new int[]{numRows, numCols};

	}

	private void openBook() throws Exception{
		try {
			File myFile = new File(filePath);
			inStream = new FileInputStream(myFile);

			poiWorkbook = new XSSFWorkbook (inStream);

		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void closeBook() throws Exception{


		try {
			poiWorkbook.close();
			inStream.close();

		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Stage getStage(){
		return stage;
	}


}
