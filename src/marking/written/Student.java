package marking.written;

import marking.written.gui.TextOutput;

public class Student {

	private String surname;
	private String initials;
	private int studentNumber;
	private double mark;
	private boolean hasMark;
	public static TextOutput outText;
	
	private int rowNumber;
	
	public Student(String surname, String initials, int studentNumber) {
		super();
		this.surname = surname.replaceAll("\\s+",""); // Removes white spaces
		this.initials = initials.replaceAll("\\s+","");
		this.studentNumber = studentNumber;
		mark=0.;
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

	public double getMark() {
		return mark;
	}
	
	public boolean hasMArk(){
		return hasMark;
	}
	
	public void addMark(double add){
		mark+= add;
		System.out.println("Mark set as "+mark+" for "+this.getStudentNumberFirstString());
	}

	public void setMark(double mark) {
		this.mark = mark;
		hasMark=true;
		outText.displayText("Mark set as "+mark+" for "+this.getStudentNumberFirstString(),false);
		System.out.println("Mark set as "+mark+" for "+this.getStudentNumberFirstString());
	}
	
	public String getSurnameFirstString(){
		return this.getSurname()+" "+this.getInitials()+" "+this.getStudentNumber();
	}
	
	public String getStudentNumberFirstString(){
		return this.getStudentNumber()+" "+this.getSurname()+" "+this.getInitials();
	}


	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowIndex) {
		this.rowNumber = rowIndex;
	}
	
	
	
}
