package marking.written;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentList extends ArrayList<Student>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public ArrayList<String> toSurnameFirstList(){
		ArrayList<String> list = new ArrayList<String>();

		for(Student s: this){
			list.add(s.getSurnameFirstString());
		}

		return list;
	}

	public ArrayList<String> toStudentNumberfirstList(){
		ArrayList<String> list = new ArrayList<String>();

		for(Student s: this){
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
	public Student getStudent(String string){
		for(Student s: this){
			if(s.getStudentNumberFirstString().equals(string) || s.getSurnameFirstString().equals(string)){
				return s;
			}
		}
		System.err.println("NO STUDENT FOUND");
		return null;
	}
	
	public Student getStudentByNumber(int stdNumber){
		
		for(Student s: this){
			if(s.getStudentNumber()==stdNumber){
				return s;
			}
		}
		System.err.println("NO STUDENT FOUND");
		return null;
	}
	
	public boolean hasStudent(int stdNumber){
		for(Student s: this){
			if(s.getStudentNumber()==stdNumber){
				return true;
			}
		}
		return false;
	}
	
	public void addStudentMark(int stnum, double mark){
		if(!hasStudent(stnum)){
			Student s = new Student("", "", stnum);
			s.addMark(mark);
			this.add(s);
		}
		else{
			getStudentByNumber(stnum).addMark(mark);
		}
	}

	public void printList(){
		System.out.println("BEGIN: Students in list:");
		for(Student s: this){
			System.out.println(s.getStudentNumberFirstString());
		}
		System.out.println("END: Students in list:");
	}
}
