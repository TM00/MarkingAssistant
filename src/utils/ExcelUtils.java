package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {


	/**
	 * Returns a String representation of a Cell.
	 * 
	 * If the cell is null, an empty String is returned.
	 * 
	 * For formula cells, the formula is returned, if just the value is required, use
	 * method {@link #cellStringValue}
	 * 
	 * @param c
	 * @return A String representation of the given cell
	 */
	public static String cellToString(Cell c){
		String s = "";

		if(c == null){
			return s;
		}

		switch (c.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			s = c.getNumericCellValue()+"";
			break;
		case Cell.CELL_TYPE_STRING:
			s = c.getStringCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			s = c.getCellFormula();
			break;

		case Cell.CELL_TYPE_BLANK:
			s = "";
			break;


		default:
			System.err.println("Cell type unsupported row" +(c.getRowIndex()+1)+" col "+(c.getColumnIndex()+1));
			s="";
			break;
		}

		return s;
	}

	/**
	 * Returns a String representation of a Cell's value for all cells.
	 * 
	 * If the cell is null, an empty String is returned.
	 * 
	 * For formula cells, the result is returned by re-evaluating it with the {@link FormulaEvaluator}
	 * provided.
	 * 
	 * @param cell The Cell to be evaluated
	 * @param evaluator - The evaluator for the Workbook.
	 * 
	 * @return A String representation of the given cell's value
	 */
	public static String cellStringValue(FormulaEvaluator evaluator, Cell cell){
		String s = "";

		if(cell == null){
			return s;
		}

		switch (evaluator.evaluateFormulaCell(cell)) {
		case Cell.CELL_TYPE_BOOLEAN:
			s= ""+cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			s = ""+cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_STRING:
			s=cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			s="";
			break;
		case Cell.CELL_TYPE_ERROR:
			s="";
			break;

			// CELL_TYPE_FORMULA will never happen
		case Cell.CELL_TYPE_FORMULA: 
			break;
			
		case -1:
			// Cell was not a formula
			s = cellToString(cell);
			break;

		default:
			System.err.println("Cell type unsupported row" +(cell.getRowIndex()+1)+" col "+(cell.getColumnIndex()+1));
			s="";
			break;
		}

		return s;
	}
	
	/**
	 * This method recalculates all the formulas in the given workbook using the given 
	 * {@link FormulaEvaluator}
	 */
	public static void recalcFormulas(FormulaEvaluator evaluator, XSSFWorkbook wb){
		for (Sheet sheet : wb) {
		    for (Row r : sheet) {
		        for (Cell c : r) {
		            if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
		                evaluator.evaluateFormulaCell(c);
		            }
		        }
		    }
		}
	}

	/**
	 * Returns the double value of the cell by converting it to a String value and then a 
	 * double value. This avoids trying to retrieve numeric values from numbers stored in
	 * text cells
	 * @param c - The cell in question
	 * @return The double value of the cell
	 */
	public static double cellToDouble(Cell c){
		return Double.parseDouble(cellToString(c));
	}

	/**
	 * Converts a cell's value to a String, then double and then integer, which is returned.
	 * 
	 * @param c
	 * @return
	 */
	public static int cellToInt(Cell c){
		return (int) cellToDouble(c);
	}

	/**
	 * Gets the column index given the column name for example "A" = 0
	 * @param column
	 * @return
	 */
	public static int getExcelColumnIndex(String column) {
		int result = 0;
		for (int i = 0; i < column.length(); i++) {
			result *= 26;
			result += column.charAt(i) - 'A' + 1;
		}
		return result-1;
	}

	/**
	 * Gets the column name from the index for example 0= "A"
	 * @param index
	 * @return
	 */
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
}
