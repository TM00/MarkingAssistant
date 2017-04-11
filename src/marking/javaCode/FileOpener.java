package marking.javaCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

public class FileOpener {

	public static String notePadppPath;

	/**
	 * Loops through all the files in a folder and appends them with the given text
	 * @param text
	 * @param folderPath
	 */
	public static void appendFiles(String text, String filePath){
		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){ // Is a folder
				for(File f:folder.listFiles()){
					appendFiles(text, f.getPath());
					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				//Is a file
				appendFileWithString(text, folder.getPath());

			}
		}
		else{
			System.err.println("File doesn't exist.. path: "+filePath);
		}
	}

	/**
	 * Loops through all the files in a folder and opens them with notepad++
	 * @param folderPath
	 */
	public static void openFilesWithNotepadPP(String filePath){
		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){ // Is a folder
				for(File f:folder.listFiles()){
					openFilesWithNotepadPP(f.getPath());
					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				//Is a file
				openFileWithNotepadPP(folder.getPath());

			}
		}
		else{
			System.err.println("File doesn't exist.. path: "+filePath);
		}
	}

	/**
	 * Loops through all the files in a folder and opens them with notepad++
	 * @param filePath - Path to the folder
	 * @param pathToNotepadppExe - Path to the notepadpp exe
	 */
	public static void openFilesWithNotepadPP(String filePath, String pathToNotepadppExe){
		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){ // Is a folder
				for(File f:folder.listFiles()){
					openFilesWithNotepadPP(f.getPath(),pathToNotepadppExe);
					try {
						TimeUnit.MILLISECONDS.sleep(750); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				//Is a file
				openFileWithNotepadPP(folder.getPath(),pathToNotepadppExe);

			}
		}
		else{
			System.err.println("File doesn't exist.. path: "+filePath);
		}
	}

	/**
	 * Loops through all the files in a folder and checks if they contain a string
	 * @param folderPath
	 */
	public static String checkFiles(String text, String filePath, boolean openNotFound){

		File f = new File(filePath);
		String total = "";

		if(f.isDirectory()){ // Directory

			File subf;
			File[] list = f.listFiles();
			for (int i = 0; i < list.length; i++) {
				subf = list[i];
				String res = checkFiles(text,subf.getPath(),openNotFound);
				if(res.equals("")){

				}
				else{
					System.err.println("String: "+text+" NOT FOUND in folder"+f.getPath());
					total += "\n"+res;
					//return res;
				}	


			}
			return total;

		}
		else{
			String s = checkFileForString(text, filePath,openNotFound);

			if(!s.equals(""))
				total += "\n"+s;

			return total;
		}


	}



	@Deprecated
	/**
	 * Appends the files in a given folder with subfolders with the given text, and then opens them 
	 * with Notepad++ (if installed)
	 * @param text
	 * @param folderpath
	 */
	public static void appendAndOpenFilesFromFolderWithSubfolders(String text, String folderpath){

		appendFilesFromFolderWitSubFolders(text, folderpath);
		System.out.println("Done appending...");
		try {
			TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		openFilesFromFolderWitSubFoldersWithNotepadPP(folderpath);

	}

	/**
	 * Method that opens the file from the provided path with notepad++ (if installed)
	 * @param filePath
	 */
	public static void openFileWithNotepadPP(String filePath){

		//Path to notepad++.exe
		String notePadppexe = "C:\\Program Files (x86)\\Notepad++\\notepad++.exe";

		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(notePadppexe+" "+filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Method that opens the file from the provided path with notepad++ (path provided)
	 * @param filePath - Path to the file that must be opened
	 * @param notePadppexe - Path to the notepadpp.exe file
	 */
	public static void openFileWithNotepadPP(String filePath, String notePadppexe){

		//Path to notepad++.exe
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(notePadppexe+" "+filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Deprecated
	/**Method to open all the files within a certain folder with notepad++
	 * 
	 * @param filePath - Path to folder
	 */
	public static void openFilesFromFolderWithNotepadPP(String filePath){

		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){
					openFileWithNotepadPP(f.getPath());

					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				System.err.println("Not a directory..., opening anyway path: "+filePath);
				openFileWithNotepadPP(folder.getPath());

			}
		}
		else{
			System.err.println("Folder doesn't exist.. path: "+filePath);
		}
	}

	@Deprecated
	/**
	 * Method to open the files from subfolders within a parent folder. ex a folder containing folders with with student numbers.
	 * @param filePath - Path to parent folder
	 */
	public static void openFilesFromFolderWitSubFoldersWithNotepadPP(String filePath){
		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){
					openFilesFromFolderWithNotepadPP(f.getPath());

					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				System.err.println("Not a directory... path: "+filePath);
			}
		}
		else{
			System.err.println("Folder doesn't exist.. path: "+filePath);
		}
	}

	/**
	 * Appends the given test in a new line at the end of a text file
	 * @param append - Text to be added
	 * @param filePath - path to the file
	 */
	public static void appendFileWithString(String append, String filePath){

		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(filePath, true));
			output.append(append);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Deprecated
	/**Method to append all the files within a certain folder with given text
	 * 
	 * @param text - Text to be appended
	 * @param filePath - Path to folder
	 */
	public static void appendFilesFromFolder(String text, String filePath){

		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){
					appendFileWithString(text, f.getPath());

					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				System.err.println("Not a directory... path: "+filePath);
			}
		}
		else{
			System.err.println("Folder doesn't exist.. path: "+filePath);
		}
	}

	@Deprecated
	/**
	 * Method to append the files from subfolders within a parent folder. ex a folder containing folders with with student numbers.
	 * 
	 * @param text - Text to be appended
	 * @param filePath - Path to parent folder
	 */
	public static void appendFilesFromFolderWitSubFolders(String text, String filePath){
		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){

					appendFilesFromFolder(text, f.getPath());

					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				System.err.println("Not a directory... path: "+filePath);
			}
		}
		else{
			System.err.println("Folder doesn't exist.. path: "+filePath);
		}
	}

	/**
	 * Appends the given test in a new line at the end of a text file
	 * @param append - Text to be added
	 * @param filePath - path to the file
	 */
	public static String checkFileForString(String text, String filePath, boolean openNotFound){
		System.out.println("checkFileForString: "+filePath);
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(filePath));

			String line;
			while ((line = reader.readLine()) != null) {
				//	System.out.println(line);
				if(line.contains(text)){
					reader.close();
					System.out.println("Returning true");
					return "";

				}
			}

			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("String not found,  returning false!!");
		System.err.println("File: "+ filePath);
		// open file
		if (openNotFound) {
			if(notePadppPath !=null)
				openFileWithNotepadPP(filePath,notePadppPath);
		}


		return "String: "+text+" NOT FOUND in "+filePath;

	}


	@Deprecated
	/**Method to append all the files within a certain folder with given text
	 * 
	 * @param text - Text to be appended
	 * @param filePath - Path to folder
	 */
	public static void checkFileForStringFromFolder(String text, String filePath){

		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){
					if(checkFileForString(text, f.getPath(), false).equals("")){ // Found string

					}

					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				System.err.println("Not a directory... path: "+filePath);
			}
		}
		else{
			System.err.println("Folder doesn't exist.. path: "+filePath);
		}
	}

	@Deprecated
	/**
	 * Method to append the files from subfolders within a parent folder. ex a folder containing folders with with student numbers.
	 * 
	 * @param text - Text to be appended
	 * @param filePath - Path to parent folder
	 */
	public static void checkFileForStringFromFolderWitSubFolders(String text, String filePath){
		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){

					checkFileForStringFromFolder(text, f.getPath());

					try {
						TimeUnit.MILLISECONDS.sleep(300); //Pause for 300 ms
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
			else{
				System.err.println("Not a directory... path: "+filePath);
			}
		}
		else{
			System.err.println("Folder doesn't exist.. path: "+filePath);
		}
	}

	public static String checkFilesAndOpen(String search, String destFolder) {
		// TODO Auto-generated method stub
		return null;
	}
}

