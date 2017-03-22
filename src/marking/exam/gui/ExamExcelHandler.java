package marking.exam.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Main;
import marking.exam.ExamStudent;
import marking.exam.ExamStudentList;
import marking.written.Student;
import marking.written.StudentList;
import marking.written.gui.ExcelFileHandler;
import utils.ExcelUtils;

public class ExamExcelHandler {

	private static int studentNumberColumnIndex;
	private static int surnameColumnIndex;
	private static int initialsColumnIndex;
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
	 */
	public static ExamStudentList readExamStudentListFromFile(){
		//		String filepath="E:\\Demi\\Inf314\\Klaslys_SVN.xlsx";
		int startrow=startRowIndex;
		String filepath=filePath_to_excel;
		ExamStudentList list = new ExamStudentList(); 


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

					ExamStudent stud = new ExamStudent(sur,in, stdNum);
					stud.setRowNumber(row.getRowNum()); // row num is 0 based (index)
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
			ExamStudent.outText.displayText("Succesfully read the file, a total of "+counter+" students were recorded.",true);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} // Finds the workbook instance for XLSX file XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); // Return first sheet from the XLSX workbook XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		catch (IOException e) {
			e.printStackTrace();
		}
		return list;

	}


	/**Loops throught provided StudentList and assigns corrosponding marks
	 * @param writeTotal if true, the total is written in its allocated column, false nothing is done
	 * @param writeQuestions if true, the questions are written in their allocated columns, false nothing is done
	 * 
	 * @param filePath_to_excel
	 * @param studentNumberColumnIndex
	 * @param markColumnIndex
	 * @param startrow - Row index where to start looping
	 */
	public static void writeExamStudentMarksToFile(ExamStudentList list, boolean writeQuestions, boolean writeTotal){
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

			ArrayList<Cell> markCells = new ArrayList<>();
			Cell totalCell = null; // cell for the total
			int numQuestions  = Main.configData.questionData.size();

			int counter=0;
			while(true){
				markCells.clear(); // Start with new Cells :) 
				row=mySheet.getRow(startrow);

				if(row==null){
					System.out.println("breaking...");	
					break;
				}

				stdnumberCell=row.getCell(studentNumberColumnIndex);

				if(Main.configData.writeQuestions){ // Only load question cells if required

					ConfigListData dat;
					for (int i = 0; i < numQuestions; i++) {

						dat = Main.configData.questionData.get(i);
						if(!dat.getName().equals("TOTAL")){ // if not the total cell
							markCells.add(i, row.getCell(getExcelColumnIndex(dat.getExcelColumn())));

							if(markCells.get(i)==null){
								markCells.set(i, row.createCell(getExcelColumnIndex(dat.getExcelColumn())));
							}
						}
					}
				}
				if(Main.configData.writeTotal){ // Only load question cells if required
					ConfigListData dat;
					for (int i = 0; i < numQuestions; i++) {

						dat = Main.configData.questionData.get(i);

						if(dat.getName().equals("TOTAL")){ // if  the total cell
							totalCell = row.getCell(getExcelColumnIndex(dat.getExcelColumn()));

							if(totalCell==null){
								totalCell =  row.createCell(getExcelColumnIndex(dat.getExcelColumn()));
							}
							break; // stop
						}
					}
				}


				if(stdnumberCell==null){
					System.out.println("breaking...");	
					break;
				}
				else{
					int stdNum=(int) stdnumberCell.getNumericCellValue();

					ExamStudent stud = list.getStudentByNumber(stdNum);

					if(stud!=null){ // student in list
						// TODO clean code...
						if(stud.hasMark()){

							Map<String,Double> marks = stud.getMarks();
							//	marks.keySet().forEach(e -> System.out.println(e));
							ArrayList<ConfigListData> questionData = Main.configData.questionData;

							Cell mark;
							//ConfigListData dat;
							for (int i = 0; i < markCells.size(); i++) { // Will be empty if not writeQuestion = false
								mark = markCells.get(i);
//								System.out.println("n: "+questionData.get(i).getNumber());
//								System.out.println(marks);
								mark.setCellValue(marks.get(questionData.get(i).getNumber()+""));
							}

							// Write total, if needed
							if(totalCell != null){
								totalCell.setCellValue(stud.getTotalMark());
							}
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


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} // Finds the workbook instance for XLSX file XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); // Return first sheet from the XLSX workbook XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
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
		ExamExcelHandler.studentNumberColumnIndex = studentNumberColumnIndex;
	}

	public static void setSurnameColumnIndex(int surnameColumnIndex) {
		ExamExcelHandler.surnameColumnIndex = surnameColumnIndex;
	}

	public static void setInitialsColumnIndex(int initialsColumnIndex) {
		ExamExcelHandler.initialsColumnIndex = initialsColumnIndex;
	}

	public static void setStudentNumberColumnIndex(String letter) {
		ExamExcelHandler.studentNumberColumnIndex = getExcelColumnIndex(letter);
	}

	public static void setSurnameColumnIndex(String letter) {
		ExamExcelHandler.surnameColumnIndex = getExcelColumnIndex(letter);
	}

	public static void setInitialsColumnIndex(String letter) {
		ExamExcelHandler.initialsColumnIndex = getExcelColumnIndex(letter);
	}

	public static void setStartRowIndex(int startRowIndex) {
		ExamExcelHandler.startRowIndex = startRowIndex;
	}

	public static void setFilePath_to_excel(String filePath_to_excel) {
		ExamExcelHandler.filePath_to_excel = filePath_to_excel;
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

	public static void loadConstantsFromConfigData(){
		setInitialsColumnIndex(Main.configData.initialsColumnLetter);
		setSurnameColumnIndex(Main.configData.surnameColumnLetter);
		setStudentNumberColumnIndex(Main.configData.studentNumberColumnLetter);

		setStartRowIndex(Main.configData.startRow-1);
	}

}
