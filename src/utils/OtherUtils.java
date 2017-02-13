package utils;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class OtherUtils {

	/**
	 * Checks wehter a String is numerical
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

	/**
	 * Searches for and retrieves a specific {@link Menu} from a provided {@link MenuBar}. 
	 * 
	 * @param bar
	 * @param text The text/title of the menu
	 * @return
	 */
	public static Menu getMenuFromBar(MenuBar bar, String text){

		for(Menu m: bar.getMenus()){
			if(m.getText().equals(text))
				return m;
		}
		return null;
	}
}
