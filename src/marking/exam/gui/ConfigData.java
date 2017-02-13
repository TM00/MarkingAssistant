package marking.exam.gui;

import java.io.Serializable;
import java.util.ArrayList;

public class ConfigData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public  String initialsColumnLetter;
	public  String surnameColumnLetter; 
	public  String studentNumberColumnLetter;
	public  int startRow;
	public ArrayList<ConfigListData> questionData;
	public int numQuestions;

}
