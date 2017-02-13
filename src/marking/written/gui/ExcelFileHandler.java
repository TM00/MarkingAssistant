package marking.written.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.Main;
import marking.exam.ExamStudent;
import marking.exam.ExamStudentList;
import marking.written.Student;
import marking.written.StudentList;
import utils.ExcelUtils;

public class ExcelFileHandler {

	private static int studentNumberColumnIndex;
	private static int surnameColumnIndex;
	private static int initialsColumnIndex;
	private static int markColumnIndex;
	private static int startRowIndex;
	private static String filePath_to_excel;


	/**
	 * 
	 * @param filePath_to_excel
	 * @param studentNumberColumnIndex
	 * @param surnameColumnIndex
	 * @param initialsColumnIndex
	 * @param startrow - Row index where to start looping
	 * @return
	 * @throws Exception in case of error
	 */
	public static StudentList readStudentListFromFile() throws Exception{
		//		String filepath="E:\\Demi\\Inf314\\Klaslys_SVN.xlsx";
		int startrow=startRowIndex;
		String filepath=filePath_to_excel;
		StudentList list = new StudentList(); 

		File myFile = new File(filepath);

		try {
			FileInputStream fis = new FileInputStream(myFile);

			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);

			XSSFSheet mySheet = myWorkBook.getSheetAt(0); // Get iterator to all the rows in current sheet Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file while (rowIterator.hasNext()) { Row row = rowIterator.next();

			String finalS="";

			Row row;
			Cell stdnumberCell;
			Cell surnameCell;
			Cell initialsCell;


			int counter=0;
			while(true){

				row=mySheet.getRow(startrow);

				if(row==null){
					System.out.println("row = null, counter = "+counter+"breaking...");	
					break;

				}

				stdnumberCell=row.getCell(studentNumberColumnIndex);
				surnameCell=row.getCell(surnameColumnIndex);
				initialsCell=row.getCell(initialsColumnIndex);

				if(stdnumberCell==null || surnameCell==null  || initialsCell==null ){
					System.out.println("breaking...");	
					break;
				}
				else if(ExcelUtils.cellToString(stdnumberCell).isEmpty() ||
						ExcelUtils.cellToString(surnameCell).isEmpty()  || 
						ExcelUtils.cellToString(initialsCell).isEmpty()  ){
					System.out.println("breaking...");	
					break;
				}
				else{

					int stdNum= ExcelUtils.cellToInt(stdnumberCell);
					String sur = ExcelUtils.cellToString(surnameCell);
					String in = ExcelUtils.cellToString(initialsCell);

					Student stud = new Student(sur,in, stdNum);
					stud.setRowNumber(row.getRowNum());

					list.add(stud);
				}



				/*
				flag=row.getCell(4);
				if(flag!=null){
					if(flag.getNumericCellValue()==1.){
						counter++;
						finalS+=( (int)c.getNumericCellValue())+"@sun.ac.za; ";
					}
				}
				 */


				startrow++;
				counter++;
			}

			myWorkBook.close();
			System.out.println("num = "+counter+"\n\n");
			System.out.println(finalS);
			Student.outText.displayText("Succesfully read the file, a total of "+counter+" students were recorded.",true);


		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} // Finds the workbook instance for XLSX file XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); // Return first sheet from the XLSX workbook XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		//catch (IOException e) {
		//e.printStackTrace();
		//		}
		return list;

	}


	/**Loops through provided StudentList and assigns corrosponding marks
	 * 
	 * @param filePath_to_excel
	 * @param studentNumberColumnIndex
	 * @param markColumnIndex
	 * @param startrow - Row index where to start looping
	 * @throws Exception in case of error
	 */
	public static void writeStudentMarksToFile(StudentList list) throws Exception{
		int startrow=startRowIndex;
		String filepath=filePath_to_excel;

		File myFile = new File(filepath);

		try {
			FileInputStream fis = new FileInputStream(myFile);

			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);

			XSSFSheet mySheet = myWorkBook.getSheetAt(0); // Get iterator to all the rows in current sheet Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file while (rowIterator.hasNext()) { Row row = rowIterator.next();

			String finalS="";

			Row row;
			Cell stdnumberCell;

			Cell markCell;

			Cell flag;
			int counter=0;
			while(true){

				row=mySheet.getRow(startrow);

				if(row==null){
					System.out.println("breaking...");	
					break;

				}

				stdnumberCell=row.getCell(studentNumberColumnIndex);
				markCell=row.getCell(markColumnIndex);

				if(markCell==null){
					markCell=row.createCell(markColumnIndex);
				}

				if(stdnumberCell==null){
					System.out.println("breaking...");	
					break;
				}
				else if(ExcelUtils.cellToString(stdnumberCell).isEmpty() ){
					System.out.println("breaking...");	
					break;
				}
				else{

					int stdNum=(int) Double.parseDouble(ExcelUtils.cellToString(stdnumberCell));

					Student stud = list.getStudentByNumber(stdNum);

					if(stud!=null){ // student in list
						if(stud.hasMArk()){
							markCell.setCellValue(stud.getMark());
						}
						else{
							System.err.println("Student has no MARK!! num = "+stdNum);
						}
					}

				}

				startrow++;
			}

			fis.close();
			FileOutputStream fileOutput;
			fileOutput = new FileOutputStream(myFile);
			myWorkBook.write(fileOutput);
			fileOutput.close();
			myWorkBook.close();

			System.out.println("num = "+counter+"\n\n");
			System.out.println(finalS);


		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}


	}


	public static File getFile() {
		/*
		// System.getProperties() documentation
		String userDirectory = System.getProperty("My Computer");
		// Display the file chooser dialog
		JFileChooser fc = new JFileChooser(userDirectory);
		//Only .xlsx files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel XLSX files", "xlsx");
		fc.setFileFilter(filter);

		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (!file.exists()) {
				System.err.println("The chosen file does not exist!??");
				return null;
			} else {

				return file;
			}
		} else {
			System.err.println("You did not choose a file!");
			return null;
		}
		 */
		FileChooser fcfx = new FileChooser();
		fcfx.setTitle("Choose input file");
		//fcfx.setInitialDirectory(new File(System.getProperty("user.dir")));
		fcfx.getExtensionFilters().add(new ExtensionFilter("Excel XLSX files", "*.xlsx"));

		return fcfx.showOpenDialog(new Stage());
	}

	public static File getFile(String extensionFilter) {

		// System.getProperties() documentation
		String userDirectory = System.getProperty("My Computer");
		// Display the file chooser dialog
		JFileChooser fc = new JFileChooser(userDirectory);
		//Only .xlsx files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("some file",extensionFilter);
		fc.setFileFilter(filter);

		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (!file.exists()) {
				System.err.println("The chosen file does not exist!??");
				return null;
			} else {

				return file;
			}
		} else {
			System.err.println("You did not choose a file!");
			return null;
		}
	}

	public String getFileName(){
		File f = new File(filePath_to_excel);
		return f.getName();
	}

	public static void setStudentNumberColumnIndex(int studentNumberColumnIndex) {
		ExcelFileHandler.studentNumberColumnIndex = studentNumberColumnIndex;
	}

	public static void setSurnameColumnIndex(int surnameColumnIndex) {
		ExcelFileHandler.surnameColumnIndex = surnameColumnIndex;
	}

	public static void setInitialsColumnIndex(int initialsColumnIndex) {
		ExcelFileHandler.initialsColumnIndex = initialsColumnIndex;
	}

	public static void setMarkColumnIndex(int markColumnIndex) {
		ExcelFileHandler.markColumnIndex = markColumnIndex;
	}

	public static void setStudentNumberColumnIndex(String letter) {
		ExcelFileHandler.studentNumberColumnIndex = getExcelColumnIndex(letter);
	}

	public static void setSurnameColumnIndex(String letter) {
		ExcelFileHandler.surnameColumnIndex = getExcelColumnIndex(letter);
	}

	public static void setInitialsColumnIndex(String letter) {
		ExcelFileHandler.initialsColumnIndex = getExcelColumnIndex(letter);
	}

	public static void setMarkColumnIndex(String letter) {
		ExcelFileHandler.markColumnIndex = getExcelColumnIndex(letter);
	}

	public static void setStartRowIndex(int startRowIndex) {
		ExcelFileHandler.startRowIndex = startRowIndex;
	}

	public static void setFilePath_to_excel(String filePath_to_excel) {
		ExcelFileHandler.filePath_to_excel = filePath_to_excel;
	}


	public static int getExcelColumnIndex(String column) {
		int result = 0;
		for (int i = 0; i < column.length(); i++) {
			result *= 26;
			result += column.charAt(i) - 'A' + 1;
		}
		return result-1;
	}

	public static String getExcelColumnName(int index) {
		final StringBuilder sb = new StringBuilder();

		int num = index;
		while (num >=  0) {
			int numChar = (num % 26)  + 65;
			sb.append((char)numChar);
			num = (num  / 26) - 1;
		}
		return sb.reverse().toString();
	}

	public static void loadConstantsFromSettings(){
		try{
			setInitialsColumnIndex(Main.settings.initialsColumnLetter);
			setSurnameColumnIndex(Main.settings.surnameColumnLetter);
			setStudentNumberColumnIndex(Main.settings.studentNumberColumnLetter);
			setMarkColumnIndex(Main.settings.markColumnLetter);
			setStartRowIndex(Main.settings.startRow-1);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public static void main(String[] args) {

		System.out.println("index  ="+getExcelColumnName(0));
	}*/
}
