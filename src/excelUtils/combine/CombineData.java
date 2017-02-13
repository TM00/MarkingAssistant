package excelUtils.combine;

import java.util.ArrayList;

public class CombineData {


	private String searchValue;
	private ArrayList<String> values;


	public CombineData(String searchValue) {
		super();
		this.searchValue = searchValue;
		values = new ArrayList<>();
	}

	public void addValue(String value){
		values.add(value);
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "CombineData [searchValue=" + searchValue + ", values=" + values + "]";
	}




}
