package marking.exam.gui;

import java.io.Serializable;

public class StudentExamListData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String description;
	private int questionNumber;
	private double mark;
	
	public StudentExamListData( int questionNumber, String description) {
		super();
		this.description = description;
		this.questionNumber = questionNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public double getMark() {
		return mark;
	}

	public void setMark(double mark) {
		this.mark = mark;
	}
	
	
	
	
}
