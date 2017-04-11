package res;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.sf.image4j.codec.ico.ICODecoder;

public class ResourceLoader {

	private static ResourceLoader rl = new ResourceLoader();
	private static final String MUSICFOLDERNAME = "sounds";
	private static final String LOADINGFOLDERNAME = "loadingSound";
	private static final String GIFFOLDERNAME = "gifs";

	/*public static Image getImage(String s){

		rl.getClass().
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource("/"+s));
	}*/

	public static InputStream getInputStream(String s){
		return rl.getClass().getResourceAsStream("/"+s);
	}

	public static String getFilePath(String s){
		System.out.println(rl.getClass().getResource("/"+s).getPath());
		return rl.getClass().getResource("/"+s).getFile();
	}

	public static String getFiledir(String s){
		String k= rl.getClass().getResource("/").getPath();//.replace('/', '\\');
		k+=s;
		if(k.startsWith("\\")){
			//k=k.substring(1);
		}
		System.out.println(k);
		return k;
	}


	/*private String swapslashes(String k){
		return k.replace('/', '\\');
	}*/

	public static BufferedReader getFileBufferedReader(String s){
		InputStreamReader stream = new InputStreamReader(rl.getClass().getResourceAsStream("/"+s));
		return new BufferedReader(stream);
	}

	public static InputStream getFileInputStream(String s){
		InputStream stream = (rl.getClass().getResourceAsStream("/"+s));
		return stream;
	}

	public static FileOutputStream getFileOuputStream(String s){
		Path p= Paths.get(s); 

		File f = new File(p.toString());
		System.out.println(f.getPath());
		FileOutputStream out;
		try {
			out = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return out;
	}

	/**
	 * Gets all the imatges from an .ico file stored in a list
	 * @param name - The name of to the .ico file
	 * @return
	 */
	public static List<Image> getIcons(String name){
		List<Image> image = new ArrayList<>();
		try {
			ICODecoder.read(getFileInputStream(name)).forEach(e -> image.add(SwingFXUtils.toFXImage(e, null)));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}

	public static File getFile(String s){
		String userDirectory = System.getProperty("user.dir");
		System.out.println(userDirectory); 

		return new File(userDirectory+"\\resources\\"+s);
	}

	public static File getRandomGif(){
		String userDirectory = System.getProperty("user.dir");

		String path = userDirectory+"\\"+GIFFOLDERNAME;
		System.out.println(path); 
		File folder = new File(path);

		File[] gifs = folder.listFiles();

		int randomNum = ThreadLocalRandom.current().nextInt(0, gifs.length-1 + 1);

		return gifs[randomNum];


	}

	public static File getMusicFolder(){
		String userDirectory = System.getProperty("user.dir");
		System.out.println(userDirectory+"\\"+MUSICFOLDERNAME); 

		return new File(userDirectory+"\\"+MUSICFOLDERNAME);
	}

	public static String getNotePadPPpath(){
		return System.getProperty("user.dir")+"\\Notepad++\\notepad++.exe";
	}


	public static void main(String[] args) {

		getMusicFolder();
	}

	public static File getMusicFile(String string) {
		String userDirectory = System.getProperty("user.dir");
		System.out.println(userDirectory+"\\"+MUSICFOLDERNAME); 

		return new File(userDirectory+"\\"+MUSICFOLDERNAME+"\\"+string);
	}

	public static File getLoadingFile() {
		String string = "loading.mp3";
		String userDirectory = System.getProperty("user.dir");
		System.out.println(userDirectory+"\\"+LOADINGFOLDERNAME); 

		return new File(userDirectory+"\\"+LOADINGFOLDERNAME+"\\"+string);
	}
}
