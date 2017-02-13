package excelUtils.combine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.ExcelUtils;
import utils.OtherUtils;

public class ExcelCombiner {


	/*
	// NOTES 
	 * 
	 *
	 * 
	 */

	private CombineFile mainFile;
	private ArrayList<CombineFile> otherFiles;

	public ExcelCombiner(CombineFile mainFile) {
		this.mainFile = mainFile;
		otherFiles = new ArrayList<>();
	}

	public void addFile(CombineFile f){
		otherFiles.add(f);
	}

	public String combine(){

		File theMainFile = new File(mainFile.getPath());
		System.out.println("Combining into "+theMainFile.getAbsolutePath());


		try {
			FileInputStream fis = new FileInputStream(theMainFile);

			XSSFWorkbook mainWorkBook = new XSSFWorkbook (fis);

			XSSFSheet maimSheet = mainWorkBook.getSheetAt(mainFile.getSheetIndex()); // Get iterator to all the rows in current sheet Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file while (rowIterator.hasNext()) { Row row = rowIterator.next();

			// Open other streams..
			//			otherFiles.forEach(e -> e.open());
			// Gather all the file info...
			otherFiles.forEach(e->e.getFileInfo());

			int startrow = mainFile.getStartRow();
			int cellNum = mainFile.getSearchColumnIndex();

			Row row;
			Cell searchCell;
			int counter=0;
			int nullCounter = 0;
			while(true){
				// Loop through main file... and add info from other files...
				row=maimSheet.getRow(startrow);
				System.out.println("Looking @ row "+startrow);

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

				String cellValue = "";
				switch (searchCell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					cellValue = searchCell.getNumericCellValue()+"";
					break;
				case Cell.CELL_TYPE_STRING:
					cellValue = searchCell.getStringCellValue();
					break;

				default:
					System.out.println("L98 Cell value not number or string????");
					System.out.println(searchCell.getStringCellValue());
					break;
				}

				// Now find information from other files, if applicable
				for(CombineFile f: otherFiles){
					ArrayList<String> valuesi = f.getSearchValuesByName(cellValue);
					System.out.println("cv= "+cellValue+" vs:");
					valuesi.forEach(e-> System.out.println(e));
					if(valuesi.size()>0){ // if something was found

						Cell c = null;
						int count = 0;

						for(Integer i: mainFile.getMergeIndices()){

							c = row.getCell(i);
							String addVall=valuesi.get(count);
							System.out.println("addVall = "+addVall);
							if(c==null){
								System.out.println("c==0");
								c = row.createCell(i);
								if(OtherUtils.isNumeric(addVall)){
									System.out.println("isNumeric");
									c.setCellValue(Double.parseDouble(addVall));
								}else
									c.setCellValue(addVall);
							}
							else{
								try{
									System.out.println("else");
									if(OtherUtils.isNumeric(addVall)){
										System.out.println("isNumeric 2");

										String cellVal = ExcelUtils.cellToString(c);
										System.out.println("CELVAL = "+cellVal);
										if(OtherUtils.isNumeric(cellVal)){
											c.setCellValue(Double.parseDouble(cellVal)+Double.parseDouble(addVall));
										}else if(cellVal.isEmpty()){
											c.setCellType(Cell.CELL_TYPE_NUMERIC);
											c.setCellValue(Double.parseDouble(addVall));
										}else{
											System.out.println("doing nothing");
											// Do nothing
										}


									}
									else{
										if(!addVall.equals("")){
											c.setCellValue(addVall);
										}
									}

									/*	switch (c.getCellType()) {
									case Cell.CELL_TYPE_NUMERIC:
										c.setCellValue(c.getNumericCellValue()+Double.parseDouble(addVall));
										break;
									case Cell.CELL_TYPE_STRING:
										c.setCellValue(Double.parseDouble(c.getStringCellValue())+Double.parseDouble(addVall));
										break;

									default:
										System.out.println("L132 Cell value not number or string????");
										System.out.println(c.getStringCellValue());
										//break;
									}*/	
								}catch(NumberFormatException e){
									System.out.println("Value not a double, keeping string");
									c.setCellValue(addVall);
								}
							}
							count++;
						}
					}
				}

				counter++;
				startrow++;
			}


			//otherFiles.forEach(e -> e.close());
			fis.close();
			FileOutputStream fileOutput;
			fileOutput = new FileOutputStream(theMainFile);
			mainWorkBook.write(fileOutput);
			fileOutput.close();
			mainWorkBook.close();
			System.out.println("num = "+counter+"\n\n");
			return "success";


		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "A FileNotfoundException occurred!!";
		} // Finds the workbook instance for XLSX file XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); // Return first sheet from the XLSX workbook XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		catch (IOException e) {
			e.printStackTrace();
			return "An IOException occurred!!";
		}

	}


	public static void main(String[] args) {

		JFileChooser fileopen = new JFileChooser(System.getProperty("user.dir"));
		FileFilter filter = new FileNameExtensionFilter("xlsx files", "xlsx");
		fileopen.setFileFilter(filter);

		fileopen.showDialog(null, "Open file");

		File main = fileopen.getSelectedFile();

		ArrayList<Integer> indices = new ArrayList<>();
		indices.add(4);

		System.err.println("Starting combineFile");
		CombineFile mainC = new CombineFile(main.getPath(), 0, indices);
		ExcelCombiner combiner = new ExcelCombiner(mainC);

		fileopen.setMultiSelectionEnabled(true);
		fileopen.showDialog(null, "Open files");

		File[] fs  =fileopen.getSelectedFiles();
		System.err.println("adding other combineFiles");
		for(File f: fs){
			CombineFile newC = new CombineFile(f.getPath(), 0, indices);
			combiner.addFile(newC);

		}

		System.out.println("Starting to join...");
		combiner.combine();
	}

}

