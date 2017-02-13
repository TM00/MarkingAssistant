package excelUtils.combine.gui;

public class CombineConfigData {


	private int number;
	private String column;

	public CombineConfigData() {
		// TODO Auto-generated constructor stub
	}

	public CombineConfigData(int i) {
		number=i;
		column="";
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}


}
