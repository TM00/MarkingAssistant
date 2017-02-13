package marking;

import java.io.Serializable;

public class NewSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//Excel file Settings
	public  String initialsColumnLetter;
	public  String surnameColumnLetter; 
	public  String studentNumberColumnLetter;
	public  String markColumnLetter;
	public  int startRow;

	//Java Settings
	public  String appendString;
	public  String CheckString;
	public  String zipSuffixString;

	//Sound Settings
	public boolean playBackgroundSound;
	public boolean playLoadingSound;
	
	public double soundVolume;

	public String toString(){
		String s = "Settings as it stands:";
		s+= "\n Excel";
		s+="\n	 initial Letter = "+initialsColumnLetter;
		s+="\n	 surname Letter = "+surnameColumnLetter;
		s+="\n	 stdnumber Letter = "+studentNumberColumnLetter;
		s+="\n	 mark Letter = "+markColumnLetter;
		s+="\n	 Start Row number = "+startRow;
		s+="\n";

		s+= "\n Java";
		s+="\n	 Append = "+appendString;
		s+="\n	 Check = "+CheckString;
		s+="\n	 zip Suffix = "+zipSuffixString;
		s+="\n";

		s+= "\n Sound";
		s+="\n	 Background Sound = "+playBackgroundSound;
		s+="\n	 Loading Sound = "+playLoadingSound;
		s+="\n	 Sound volume = "+soundVolume;

		return s;
	}

	public void setDefaults(){
		initialsColumnLetter = "C";
		surnameColumnLetter = "B";
		studentNumberColumnLetter = "A";
		markColumnLetter = "P";
		startRow=3;

		appendString = "// Theo";
		CheckString = "// Mark~";
		zipSuffixString = "_marked";

		playBackgroundSound = true;
		playLoadingSound = true;
		soundVolume = 50.0;


	}

	
	

}
