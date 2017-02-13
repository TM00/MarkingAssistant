package marking.exam;

import java.util.HashMap;
import java.util.Map;

import marking.written.gui.TextOutput;

public class ExamStudent {


	private String surname;
	private String initials;
	private int studentNumber;
	private Map<String,Double> marks;
	private boolean hasMark;
	public static TextOutput outText;

	public ExamStudent(String surname, String initials, int studentNumber) {
		super();
		this.surname = surname.replaceAll("\\s+","");
		this.initials = initials.replaceAll("\\s+","");
		this.studentNumber = studentNumber;
		marks = new HashMap<>();
		hasMark=false;
	}


	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

	public double getTotalMark(){
		double m =0.;
		double mi;
		for (String s : marks.keySet()) {
			mi = marks.get(s).doubleValue();
			m+=mi;
		}
		return m;
	}

	public Map<String,Double> getMarks() {
		return marks;
	}

	public boolean hasMArks(){
		return hasMark;
	}

	public void addMark(String question, double add){

		if(marks.containsKey(question)){
			double old = marks.get(question).doubleValue();
			double newMark = old+add;

			marks.put(question, newMark);

		}
		else{
			marks.put(question, add);
		}
		hasMark=true;
		String s = "Mark for "+question+" set as "+marks.get(question).doubleValue()+" for "+this.getStudentNumberFirstString();
		System.out.println(s);
		outText.displayText(s, false);
	}

	public void setMark(String question, double add) {


		marks.put(question, add);
		hasMark=true;
		outText.displayText("Mark set as "+marks.get(question).doubleValue()+" for "+this.getStudentNumberFirstString(),false);
		System.out.println("Mark set as "+marks.get(question).doubleValue()+" for "+this.getStudentNumberFirstString());
	}

	public String getSurnameFirstString(){
		return this.getSurname()+" "+this.getInitials()+" "+this.getStudentNumber();
	}

	public String getStudentNumberFirstString(){
		return this.getStudentNumber()+" "+this.getSurname()+" "+this.getInitials();
	}


	public boolean hasMark() {
		// TODO Auto-generated method stub
		return hasMark;
	}

}
