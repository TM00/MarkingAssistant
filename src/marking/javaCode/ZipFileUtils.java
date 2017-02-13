package marking.javaCode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileUtils {


	//------------------
	//Un Zipping
	//----------------

	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Unzips a .zip file to a folder in the same directory with the same name as the original zip file
	 * 
	 * @param zipFilePath - Path to the zip file
	 * @return The path to the destination folder
	 */
	public static String unzipInSameFolder(String zipFilePath){
		String destPath = zipFilePath.substring(0,zipFilePath.lastIndexOf("."));

		//System.out.println(destPath);


		try {
			unzip(zipFilePath,destPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destPath;
	}

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified by
	 * destDirectory (will be created if does not exists)
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public static void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		else if(!destDir.isDirectory()){
			throw new RuntimeException("Supplied path is not a folder??");
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				File parent = new File(filePath).getParentFile();
				if(!parent.exists()) {
					parent.mkdirs();
				}
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
	}
	/**
	 * Extracts a zip entry (file entry)
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	//------------------
	// Zipping
	//----------------

	/**
	 * compresses a given folder to a zip file...
	 * @param srcFolder - the folder (filepath) to zip
	 * @param destZipFile - The path to the zip file ending with .zim
	 * @throws Exception
	 */
	public static void zipFolder(String srcFolder, String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("/", srcFolder, zip);
		zip.flush();
		zip.close();
	}

	public static void zipFolderNoSubfolder(String srcFolder, String destZipFile) throws Exception{
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		File folder = new File(srcFolder);

		for (File f : folder.listFiles()) {

			if(f.isDirectory()){
				addFolderToZip("", f.getPath(), zip);
			}
			else{
				addFileToZip("", f.getPath(), zip);
			}

		}


		zip.flush();
		zip.close();
	}

	private static void addFileToZip(String path, String srcFile, ZipOutputStream zip)
			throws Exception {
		System.out.println("Adding file to zip, path: "+path+" src:"+srcFile);
		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			if(path.equals("")){
				zip.putNextEntry(new ZipEntry(folder.getName()));
			}
			else{
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			}

			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}

			in.close();
		}
	}

	private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
			throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			}
			else if (path.equals("/")) {
				addFileToZip("", srcFolder + "/" + fileName, zip);
			}else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}

	public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
		Path p = Files.createFile(Paths.get(zipFilePath));

		ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p));
		try {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp)
			.filter(path -> !Files.isDirectory(path))
			.forEach(path -> {
				String sp = path.toAbsolutePath().toString().replace(pp.toAbsolutePath().toString(), "").replace(path.getFileName().toString(), "");
				ZipEntry zipEntry = new ZipEntry(sp + "/" + path.getFileName().toString());
				try {
					zs.putNextEntry(zipEntry);
					zs.write(Files.readAllBytes(path));
					zs.closeEntry();
				} catch (Exception e) {
					System.err.println(e);
				}
			});
		} finally {
			zs.close();
		}
	}

	public static void main(String[] args) {

	}
}

