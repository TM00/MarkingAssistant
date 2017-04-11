package marking.javaCode;

import java.io.File;

import res.ResourceLoader;

public class MarkingHelper {

	private String destFolder,zipfolder, append, notePadpp;


	public MarkingHelper(String zipfolder) {
		setDefaults();

		File f = new File(zipfolder);
		if(FileDeleter.getFileExtension(f).equals("zip") 
				|| FileDeleter.getFileExtension(f).equals("rar") 
				|| FileDeleter.getFileExtension(f).equals("7z")){
			// File is a zip file
			this.zipfolder = zipfolder;
			destFolder = zipfolder.substring(0,zipfolder.lastIndexOf("."));
		}else{ // not zip
			if(f.isDirectory()){
				// file is a folder
				destFolder =  zipfolder;

			}
			else{
				throw new UnsupportedOperationException("File not zip or folder??");
			}

		}
	}

	public MarkingHelper(String zipfolder, String append) {
		setDefaults();
		this.append = append;

		File f = new File(zipfolder);
		if(FileDeleter.getFileExtension(f).equals("zip") 
				|| FileDeleter.getFileExtension(f).equals("rar") 
				|| FileDeleter.getFileExtension(f).equals("7z")){
			// File is a zip file
			this.zipfolder = zipfolder;
			destFolder = zipfolder.substring(0,zipfolder.lastIndexOf("."));
		}else{ // not zip
			if(f.isDirectory()){
				// file is a folder
				destFolder =  zipfolder;

			}
			else{
				throw new UnsupportedOperationException("File not zip or folder??");
			}

		}
	}

	private void setDefaults(){
		append = "// Theo";
		//notePadpp = "C:\\Program Files (x86)\\Notepad++\\notepad++.exe";
		notePadpp =ResourceLoader.getNotePadPPpath();
	}

	/**
	 * Deletes all the files in the folder which doesn't have the given extension
	 * @param extention
	 */
	public void deleteFilesNotExtension(String extension){
		FileDeleter.deleteFileNOTWithExtensionFromFolderWitSubFolders(extension, destFolder);
	}

	/**
	 * Appends the files
	 */
	public void append(String append2) {
		try {
			this.setAppend(append2);
			FileOpener.appendFiles(append, destFolder);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Unzips the files
	 */
	public void unZip() {
		// Unzip file
		try {
			ZipFileUtils.unzip(zipfolder, destFolder);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Opens the files with notepaddpp
	 */
	public void justOpen(){

		try {
			FileOpener.openFilesWithNotepadPP(destFolder, notePadpp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Zips the folder with the files with the gicven file suffix
	 * @param suffix
	 */
	public void zipFolder(String suffix){

		System.out.println("Zipping up...");
		System.out.println("   From "+destFolder);
		System.out.println("To "+destFolder+"_"+suffix+".zip");

		try {
			ZipFileUtils.zipFolderNoSubfolder(destFolder, destFolder+"_"+suffix+".zip");
			//	ZipFileUtils.pack(destFolder, destFolder+"_"+suffix+".zip");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Searches through all the files for a certain string, for example a mark allocation
	 * @return - if the string was found in all the files
	 */
	public String checkForString(String search, boolean openNotFound){

		FileOpener.notePadppPath = notePadpp;

		String s = "FOUND "+search+ " in ALL FILES";

		String p = FileOpener.checkFiles(search, destFolder,openNotFound);

		if(p.equals(""))
			return s;
		else
			return p;
	}

	public void setAppend(String append) {
		this.append = append;
	}

	public void setNotePadpp(String notePadpp) {
		this.notePadpp = notePadpp;
	}

	public String readMarks(){
		// Test mark reading 
		JavaMarkReader jmr = new JavaMarkReader();

		jmr.readTheMarks(this.destFolder);
		return jmr.classList();
	}

	public void readAndPrintMarks(){


		System.out.println(readMarks());
	}

	public boolean isZipFolderNull(){
		return zipfolder == null;
	}

	public static void main(String[] args) {
		MarkingHelper mh = new MarkingHelper("G:\\Demi\\Inf244\\OA3\\oatest1.zip");
		mh.setAppend("// Theo");

		//mh.unZipAppendAndOpen();

		//	mh.unZipAndOpen();
		//	mh.justOpen();

		//System.out.println("All marked: "+mh.checkForString("// Mark"));  

		mh.readAndPrintMarks();

		mh.zipFolder("_marked");



	}




}
