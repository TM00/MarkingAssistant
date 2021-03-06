package excelUtils.nameReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.ExcelUtils;

public class NameReader {

	/**
	 * 
	 * @param filePath_to_excel
	 * @param sheetIndex
	 * @param columnIndex
	 * @param startRowIndex
	 * @param append
	 * @return
	 */
	public static String readNamesEmail(String filePath_to_excel, int sheetIndex, int columnIndex,int startRowIndex, String append){
		//		String filepath="E:\\Demi\\Inf314\\Klaslys_SVN.xlsx";

		String filepath=filePath_to_excel;
		File myFile = new File(filepath);

		try {
			FileInputStream fis = new FileInputStream(myFile);

			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
			
			XSSFFormulaEvaluator eval = new XSSFFormulaEvaluator(myWorkBook);

			XSSFSheet mySheet = myWorkBook.getSheetAt(sheetIndex); // Get iterator to all the rows in current sheet Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file while (rowIterator.hasNext()) { Row row = rowIterator.next();

			int startrow = startRowIndex;
			int cellNum = 0;


			String finalS="";

			Row row;
			Cell c;
			Cell flag;
			int counter=0;
			
			int nullCounter  =0;
			int nullLimit  = 20;
			while(nullCounter< nullLimit){

				row=mySheet.getRow(startrow);

				if(row==null){
					System.out.println("breaking...");	
					break;

				}
				c=row.getCell(columnIndex);
				if(c==null){
					nullCounter++;
//					System.out.println("breaking...");	
//					break;

				}
				else{
					nullCounter=0;
				}
				flag=row.getCell(columnIndex);
				//				if(flag!=null){
				//					if(flag.getNumericCellValue()==1.){
				counter++;
				String val = ExcelUtils.cellStringValue(eval,c);
				System.out.println("val: "+val);
				if(!val.isEmpty())
					finalS+=( (int)Double.parseDouble(val)+append);
				//					}
				//				}

				startrow++;
			}

			myWorkBook.close();
			System.out.println("num = "+counter+"\n\n");
			System.out.println(finalS);
			return finalS;


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "A FileNotfoundException occurred!!";
		} // Finds the workbook instance for XLSX file XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); // Return first sheet from the XLSX workbook XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "An IOException occurred!!";
		}

	}
}
