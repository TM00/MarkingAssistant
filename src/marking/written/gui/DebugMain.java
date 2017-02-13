package marking.written.gui;

import java.awt.GraphicsEnvironment;

import java.io.Console;
import java.io.IOException;
import java.net.URISyntaxException;

import main.Main;

public class DebugMain {
	public static void main (String [] args) throws IOException, InterruptedException, URISyntaxException{
		Console console = System.console();
		if(console == null && !GraphicsEnvironment.isHeadless()){
			String filename = DebugMain.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
			Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
		}else{
			Main.main(new String[0]);
			System.out.println("Program has ended, please type 'exit' to close the console");
		}
	}

}
