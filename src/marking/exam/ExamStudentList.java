package marking.exam;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExamStudentList extends ArrayList<ExamStudent>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public ArrayList<String> toSurnameFirstList(){
		ArrayList<String> list = new ArrayList<String>();

		for(ExamStudent s: this){
			list.add(s.getSurnameFirstString());
		}

		return list;
	}

	public ArrayList<String> toStudentNumberfirstList(){
		ArrayList<String> list = new ArrayList<String>();

		for(ExamStudent s: this){
			list.add(s.getStudentNumberFirstString());
		}

		return list;
	}

	public ObservableList<String> getObservableList(){
		ObservableList<String> data = FXCollections.observableArrayList();

		data.addAll(this.toStudentNumberfirstList());
		data.addAll(this.toSurnameFirstList());

		return data;
	}

	/**
	 * Returns the student associated with the provided string surname or
	 * number first
	 * 
	 * @param s
	 * @return
	 */
	public ExamStudent getStudent(String string){
		for(ExamStudent s: this){
			if(s.getStudentNumberFirstString().equals(string) || s.getSurnameFirstString().equals(string)){
				return s;
			}
		}
		System.err.println("NO STUDENT FOUND");
		return null;
	}
	
	public int numMarked(){
		int c =0;
		for(ExamStudent s: this){
			if(s.hasMark()){
				c++;
			}
		}
		return c;
	}
	
	public ExamStudent getStudentByNumber(int stdNumber){
		
		for(ExamStudent s: this){
			if(s.getStudentNumber()==stdNumber){
				return s;
			}
		}
		System.err.println("NO STUDENT FOUND");
		return null;
	}
	
	public boolean hasStudent(int stdNumber){
		for(ExamStudent s: this){
			if(s.getStudentNumber()==stdNumber){
				return true;
			}
		}
		return false;
	}
	
	public void addStudentMark(int stnum, String q, double mark){
		if(!hasStudent(stnum)){
			ExamStudent s = new ExamStudent("", "", stnum);
			s.addMark(q,mark);
			this.add(s);
		}
		else{
			getStudentByNumber(stnum).addMark(q,mark);
		}
	}

	public void printList(){
		System.out.println("BEGIN: Students in list:");
		for(ExamStudent s: this){
			System.out.println(s.getStudentNumberFirstString());
		}
		System.out.println("END: Students in list:");
	}
}
