package marking.javaCode;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class FileDeleter {

	/**
	 * Appends the given test in a new line at the end of a text file
	 * @param append - Text to be added
	 * @param filePath - path to the file
	 */
	public static boolean deleteFileWithExtension(String extension, String filePath){
			
			File f = new File(filePath);
			if(getFileExtension(f).equals(extension)){
				f.delete();
				return true;
			}
	
		return false;

	}

	/**Method to append all the files within a certain folder with given text
	 * 
	 * @param text - Text to be appended
	 * @param filePath - Path to folder
	 */
	public static void deleteFileWithExtensionFromFolder(String text, String filePath){

		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){
					if(deleteFileWithExtension(text, f.getPath())){ // Deleted
						
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

	/**
	 * Method to append the files from subfolders within a parent folder. ex a folder containing folders with with student numbers.
	 * 
	 * @param text - Text to be appended
	 * @param filePath - Path to parent folder
	 */
	public static void deleteFileWithExtensionFromFolderWitSubFolders(String text, String filePath){
		File folder = new File(filePath);

		if(folder.exists()){
			if(folder.isDirectory()){
				for(File f:folder.listFiles()){

					deleteFileWithExtensionFromFolder(text, f.getPath());

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
	
	
	private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
}
