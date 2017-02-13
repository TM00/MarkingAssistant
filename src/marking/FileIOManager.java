package marking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.Main;
import marking.exam.gui.ConfigData;

public class FileIOManager {

	
	private static String fileDirectory=System.getProperty("user.dir");
	private static final String settingsFileName = "NewSettings.set";
	private static final String configFileName =  "configurations.config";


	public static void writeSettings(){
		try{

			File f = new File(fileDirectory+"\\"+settingsFileName);
			System.out.println(f.getPath());
			FileOutputStream fout = new FileOutputStream(f);

			ObjectOutputStream oos = new ObjectOutputStream(fout); 

			//Create new database without historic data...
			NewSettings newdata = Main.settings;
						
			oos.writeObject(newdata);

			oos.close();
			System.out.println("Done");

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void readSettings(){
		NewSettings ingelees;
		try{
			File f = new File(fileDirectory+"/"+settingsFileName);
			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fin);
			ingelees = (NewSettings) ois.readObject();

			Main.settings = ingelees;

			ois.close();

		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Settings not found, revertin to default");
			
		} 
	}
	
	public static void writeConfigData(){
		try{

			File f = new File(fileDirectory+"\\"+configFileName);
			System.out.println(f.getPath());
			FileOutputStream fout = new FileOutputStream(f);

			ObjectOutputStream oos = new ObjectOutputStream(fout); 

			//Create new database without historic data...
			ConfigData newdata = Main.configData;
						
			oos.writeObject(newdata);

			oos.close();
			System.out.println("Done");

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void readConfigData(){
		ConfigData ingelees;
		try{
			File f = new File(fileDirectory+"/"+configFileName);
			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fin);
			ingelees = (ConfigData) ois.readObject();

			Main.configData = ingelees;

			ois.close();

		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Settings not found, revertin to default");
			
		} 
	}
	/*
	public static void main(String[] args) {

		Main.settings = new NewSettings();
		Main.settings.setDefaults();
		
		writeSettings();
		
		
		readSettings();	System.out.println(Main.settings.toString());
		
		
		
	}*/
}
