package assemblernator;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Formating class to format objects and check the formatting of objects.
 * @date Apr 3, 2012; 2:32:00 PM
 * @author Josh Ventura
 */
public class IOFormat {
	/**
	 * Check a label for validity against the URBAN specification, V1.0.
	 * 
	 * @date Apr 3, 2012; 2:33:00 PM
	 * @modifications UNMODIFIED
	 * @tested UNTESTED
	 * @errors This function facilitates reporting errors. It does not generate
	 *         them.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param label
	 *            The label to check for validity.
	 * @return Returns whether this label is valid according to the
	 *         specification.
	 * @specRef S2.1
	 */
	public static boolean isValidLabel(String label) {
		for (int i = 0; i < label.length(); ++i)
			switch (label.charAt(i)) {
			case ':':
				return false;
			case ';':
				return false;
			case ' ':
				return false;
			case '\t':
				return false;
			}
		return true;
	}

	/**
	 * Format an integer to a zero-padded decimal representation.
	 * 
	 * @author Josh Ventura
	 * @modifications UNMODIFIED
	 * @tested Apr 3, 2012; 3:10:00 PM: Tested the decimal number 1337,
	 *         printed with 10, 5, 4, 2, 1, and 0 digits of allowance.
	 * 
	 *         2012-04-03 Tuesday 05:54PM: Tested the decimal number 9001, to
	 *         verify there was no issue with 0-result modulus.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param number
	 *            A number between 0 and (10^(digits+1)-1).
	 * @param digits
	 *            The number of digits to return.
	 * @return A byte[] containing the integer in decimal notation.
	 */
	public static byte[] formatInteger(int number, int digits) {
		byte res[] = new byte[digits];
		for (int i = 0; i < digits; ++i)
			res[i] = '0';
		for (int i = digits - 1; i >= 0 && number > 0; --i) {
			res[i] = (byte) ('0' + (number % 10));
			number /= 10;
		}
		return res;
	}

	/**
	 * An array of hex nybbles for quick construction of hex string
	 * representations.
	 */
	private static byte[] hexNybbles = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Format an integer to a zero-padded hexadecimal representation.
	 * 
	 * @author Josh Ventura
	 * @modifications UNMODIFIED
	 * @tested Apr 3, 2012; 3:10:30 PM: Tested the decimal number 1337,
	 *         printed with 10, 4, 3, 2, 1, and 0 digits of allowance.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param number
	 *            A number between 0 and (10^(digits+1)-1).
	 * @param digits
	 *            The number of digits to return.
	 * @return A byte[] containing the integer in decimal notation.
	 */
	public static byte[] formatHexInteger(int number, int digits) {
		byte res[] = new byte[digits];
		for (int i = 0; i < digits; ++i)
			res[i] = '0';
		for (int i = digits - 1; i >= 0 && number > 0; --i) {
			res[i] = hexNybbles[number % 16];
			number /= 16;
		}
		return res;
	}

	/**
	 * Format a date as "YYYYDDD,HH:MM:SS".
	 * 
	 * @author Josh Ventura
	 * @modifications UNMODIFIED
	 * @tested Apr 3, 2012; 5:39:00 PM: Tested with a new Date object
	 *         representing the current date and time.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param date
	 *            The date to format.
	 * @return A byte[] containing a representation of the date, according to
	 *         specification.
	 * @specRef OB1.7
	 */
	public static byte[] formatDate(Date date) {
		byte[] res = new byte[16];
		byte[] append;
		int appat = 0;
		GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar
				.getInstance();
		calendar.setTime(date);
		append = formatInteger(calendar.get(Calendar.YEAR), 4);
		for (int i = 0; i < 4; ++i)
			res[appat++] = append[i];
		append = formatInteger(calendar.get(Calendar.DAY_OF_YEAR), 3);
		for (int i = 0; i < 3; ++i)
			res[appat++] = append[i];
		append = formatInteger(calendar.get(Calendar.HOUR_OF_DAY), 2);
		res[appat++] = ',';
		for (int i = 0; i < 2; ++i)
			res[appat++] = append[i];
		res[appat++] = ':';
		append = formatInteger(calendar.get(Calendar.MINUTE), 2);
		for (int i = 0; i < 2; ++i)
			res[appat++] = append[i];
		res[appat++] = ':';
		append = formatInteger(calendar.get(Calendar.SECOND), 2);
		for (int i = 0; i < 2; ++i)
			res[appat++] = append[i];
		return res;
	}
}
