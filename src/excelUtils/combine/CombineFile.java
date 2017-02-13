package excelUtils.combine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CombineFile {

	private String path;
	private XSSFWorkbook workbook;
	private FileInputStream fis;

	private int searchColumnIndex;

	private ArrayList<Integer> mergeIndices;

	private int startRow, sheetIndex;

	private ArrayList<CombineData> data;

	public CombineFile(String path) {
		this.path=path;
		data = new ArrayList<>(); 
	}

	public CombineFile(String path, int searchColumnIndex, ArrayList<Integer> mergeIndices) {
		super();
		this.path = path;
		this.searchColumnIndex = searchColumnIndex;
		this.mergeIndices = mergeIndices;
		sheetIndex=0;
		startRow = 2;
		data = new ArrayList<>(); 

	}

	public void getFileInfo(){
		System.out.println("Getting file info for "+path);
		CombineFile comb = this;
		this.open();

		//ArrayList<String> values = new ArrayList<>();

		XSSFWorkbook mainWorkBook = comb.getWorkbook();

		XSSFSheet maimSheet = mainWorkBook.getSheetAt(comb.getSheetIndex()); // Get iterator to all the rows in current sheet Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file while (rowIterator.hasNext()) { Row row = rowIterator.next();

		int startrow = comb.getStartRow();
		int cellNum = comb.getSearchColumnIndex();
		Row row;
		Cell searchCell;
		int counter=0;
		int nullCounter = 0;
		while(true){
			// Loop through main file... and add info from other files...
			row=maimSheet.getRow(startrow);

			if(nullCounter > 3)
				break;

			if(row==null){
				System.out.println("row null, skipping...");	
				nullCounter++;
				continue;
				//break;

			}

			searchCell=row.getCell(cellNum);
			if(searchCell==null){
				System.out.println("Search cell null... skipping");
				nullCounter++;
				continue;
				//break;

			}

			String cellValue="jdfvhaejihvhfjkvbhasdkjvhbelgabsbsflkabhaekjbnhskvabd";
			switch (searchCell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				cellValue = searchCell.getNumericCellValue()+"";
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = searchCell.getStringCellValue();
				break;

			default:
				System.out.println("L99 Cell value not number or string????");
				break;
			}

			CombineData newD = new CombineData(cellValue);
			//if(cellValue.equals(searchVal)){
			// got the correct row!!
			Cell c;

			for(Integer i: comb.getMergeIndices()){
				c = row.getCell(i);
				String value = "";
				if(c!= null){
					switch (c.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						value = c.getNumericCellValue()+"";
						//	values.add(c.getNumericCellValue()+"");
						break;
					case Cell.CELL_TYPE_STRING:
						value = c.getStringCellValue()+"";
						//	values.add(c.getStringCellValue()+"");
						break;

					default:
						System.out.println("L125 Cell value not number or string???? row ="+(startrow+1));
						System.out.println(c.getStringCellValue());
						//break;
					}	


				}
				newD.addValue(value);
			}
			//System.out.println(newD);
			data.add(newD);
			//			}

			counter++;
			startrow++;
		}

		System.out.println("num = "+counter+"\n\n");

		//this.searchValues = values;
		this.close();
	}


	public void open(){
		try {
			fis = new FileInputStream(path);	

			workbook = new XSSFWorkbook (fis);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			workbook.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getSearchValuesByName(String value) {
		for (CombineData c : data) {
			if(c.getSearchValue().equals(value))
				return c.getValues();
		}

		throw new RuntimeException("Couldn't find name??");
	}


	public XSSFWorkbook getWorkbook(){
		return workbook;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}



	public int getStartRow() {
		return startRow;
	}



	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}




	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public int getSearchColumnIndex() {
		return searchColumnIndex;
	}



	public void setSearchColumnIndex(int searchColumnIndex) {
		this.searchColumnIndex = searchColumnIndex;
	}



	public ArrayList<Integer> getMergeIndices() {
		return mergeIndices;
	}



	public void setMergeIndices(ArrayList<Integer> mergeIndices) {
		this.mergeIndices = mergeIndices;
	}



}
