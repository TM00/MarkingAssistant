package marking.javaCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import marking.written.Student;
import marking.written.StudentList;
import utils.MarkException;
import utils.StudentNumberException;

public class JavaMarkReader {

	private StudentList list;
	private static String markIndicator = "Mark~";

	public JavaMarkReader() {
		list = new StudentList();
	}

	public void readTheMarks (String folderPath)  throws MarkException, StudentNumberException{

		File folder = new File(folderPath);

		if(!folder.isDirectory()){
			throw new RuntimeException("Path supplied is not a directory");
		}

		for(File f : folder.listFiles()){
			if(f.isDirectory()){
				readTheMarks(f.getPath());
			}
			else{
				try {
					double[] res = getMarksFromFile(f.getPath());

					if((int) res[0] != 0)
						list.addStudentMark((int) res[0], res[1]);

				} catch (MarkException e) {
					e.printStackTrace();
					throw e;
				}
				catch (StudentNumberException e) {
					e.printStackTrace();
					throw e;
				}

			}
		}
	}

	public String classList(){

		NumberFormat f = new DecimalFormat("#0.0#");     

		String s = " Student marks for test: \n";

		s+= " -------------------------------------\n";
		s+= "| 	 Student # 	 |  	Mark:	|\n";

		for (Student student : list) {
			s+= "| 	 "+student.getStudentNumber() + " 	 | 	 "+f.format(student.getMark())+"		|\n";
		}


		s+= " -------------------------------------\n";
		s+= "  Average = "+ f.format(getAverage())+"\n";
		s+= "  NumStudents = "+ list.size();

		return s;	
	}

	private double getAverage(){
		double sum=0.;

		for (Student student : list) {
			sum+= student.getMark();
		}
		return sum/list.size();
	}


	/**
	 * Reads the student number and total marks from a file
	 * 
	 * @param filePath
	 * @return [0] - student {@link #clone()}, [1] - mark of that file
	 */
	public double[] getMarksFromFile(String filePath) throws MarkException, StudentNumberException{
		double studnr = 0;
		double mark = 0.;

		// Read student number

		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(filePath));

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if(line.contains("package")){

					int maxNum = 0;
					String studS = "";

					for (int i = 0; i < line.length(); i++) {
						if(Character.isDigit(line.charAt(i))){
							studS+=line.charAt(i);
							maxNum++;
						}
						if(maxNum==8){
							System.out.println("std number = "+studS);
							studnr = Double.parseDouble(studS);
							break;
						}
					}

					if(studS.equals("")){
						reader.close();
						throw new StudentNumberException("Empty student# found in "+filePath);
					}

					reader.close();
					break;
				}
			}

			reader.close();

			// Read mark

			reader = new BufferedReader(new FileReader(filePath));

			while ((line = reader.readLine()) != null) {
				//	System.out.println(line);
				if(line.contains(markIndicator)){

					int index = line.indexOf(markIndicator);
					String markS = "";

					for (int i = index-1; i < line.length(); i++) {
						if(Character.isDigit(line.charAt(i))){
							markS+=line.charAt(i);

						}
						else if(line.charAt(i)=='.'){
							markS+=".";
						}
					}

					if(markS.equals("")){
						reader.close();
						throw new MarkException("Mark without value detected in "+filePath);
					}
					mark+=Double.parseDouble(markS);
				}
			}
			reader.close();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new double[] {studnr, mark};
	}

	public static void main(String[] args) {
		// Test mark reading 
		JavaMarkReader jmr = new JavaMarkReader();

		jmr.readTheMarks("E:\\Marking_Assistant\\SVN_testcase\\oatest1");

		//	double[] res = jmr.getMarksFromFile("F:\\Demi\\Inf244\\oatest2\\_17317649\\Primes100.java");

		System.out.println(jmr.classList());
	}
}
