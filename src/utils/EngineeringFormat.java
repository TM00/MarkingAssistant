package utils;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * A number formatter for engineering notation. i.e. with 3 6 9 exponents
 * @author theom
 *
 */
public class EngineeringFormat extends NumberFormat {

	private int dp = 2;
	private final int PREFIX_OFFSET = 5;
	private final String[] PREFIX_ARRAY = {"E-15", "E-12", "E-9", "E-6", "E-3", "", "E3", "E6", "E9", "E12"};

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EngineeringFormat() {
	}

	public EngineeringFormat(int decimalPlaces) {
		dp = decimalPlaces;
	}


	/**
	 * Formats the specified number as a engineering string.
	 *
	 * @param number  the number to format.
	 * @param toAppendTo  the buffer to append to (ignored here).
	 * @param pos  the field position (ignored here).
	 *
	 * @return The string buffer.
	 */
	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {

		StringBuffer formattedString = new StringBuffer("");
		// If the value is zero, then simply return 0 with the correct number of dp
		if (number ==0.){
			formattedString.append(String.format("%." + dp + "f", 0.0));
			return formattedString;
		}

		// If the value is negative, make it positive so the log10 works
		double posVal = (number<0) ? -number : number;
		double log10 = Math.log10(posVal);

		// Determine how many orders of 3 magnitudes the value is
		int count = (int) Math.floor(log10/3);

		// Calculate the index of the prefix symbol
		int index = count + PREFIX_OFFSET;

		// Scale the value into the range 1<=val<1000
		number /= Math.pow(10, count * 3);

		if (index >= 0 && index < PREFIX_ARRAY.length)
		{
			// If a prefix exists use it to create the correct string
			formattedString.append(String.format("%." + dp + "f%s", number, PREFIX_ARRAY[index]));
		}
		else
		{
			// If no prefix exists just make a string of the form 000e000
			formattedString.append(String.format("%." + dp + "fe%d", number, count * 3));
		}

		return formattedString;
	}

	@Override
	/** {@inheritDoc}
	 * Formats the specified number as a engineering string.
	 *
	 * @param number  the number to format.
	 * @param toAppendTo  the buffer to append to (ignored here).
	 * @param pos  the field position (ignored here).
	 *
	 * @return The string buffer.
	 */
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		return format(number, toAppendTo, pos);
	}

	/**
	 * Parsing is not implemented, so this method always returns
	 * <code>null</code>.
	 *
	 * @param source  ignored.
	 * @param parsePosition  ignored.
	 *
	 * @return Always <code>null</code>.
	 */
	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		return null;
	}

}
