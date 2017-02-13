package marking.exam.gui;

import java.io.Serializable;

public class ConfigListData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int number;
	private String name;
	private String excelColumn;

	public ConfigListData(int number, String name, String excelColumn) {
		super();
		this.number = number;
		this.name = name;
		this.excelColumn = excelColumn;
	}

	public ConfigListData(){
		number = -1;
		name = "";
		excelColumn = "";

	}

	public ConfigListData(int number){
		this.number = number;
		name = "";
		excelColumn = "";
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getExcelColumn() {
		return excelColumn;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExcelColumn(String excelColumn) {
		this.excelColumn = excelColumn;
	}



}
