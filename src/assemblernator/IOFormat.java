package assemblernator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.ErrorReporting.URBANSyntaxException;

/**
 * Formating class to format objects and check the formatting of objects.
 * 
 * @date Apr 3, 2012; 2:32:00 PM
 * @author Josh Ventura
 */
public class IOFormat {
	/**
	 * Check a label for validity against the URBAN specification, V1.0.
	 * 
	 * @date Apr 3, 2012; 2:33:00 PM
	 * @modified UNMODIFIED
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
		if (label.length() < 1)
			return false;
		if (!Character.isLetter(label.charAt(0)))
			return false;
		for (int i = 1; i < label.length(); ++i)
			if (!isValidLabelChar(label.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Checks whether a given character can be used in columns other than the
	 * first column in a label, according to specification.
	 * 
	 * @author Josh Ventura
	 * @date Apr 5, 2012; 10:23:08 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param c
	 *            The character to validate as a label character.
	 * @return Returns true if the character is valid in a label.
	 * @specRef S2.1
	 */
	private static boolean isValidLabelChar(char c) {
		if (Character.isWhitespace(c))
			return false;
		switch (c) {
		case ':':
			return false;
		case ';':
			return false;
		case ',':
			return false;
		case '+':
			return false;
		case '-':
			return false;
		case '*':
			return false;
		case '/':
			return false;
		}
		return true;
	}

	/**
	 * Copy a label from a given string at a given position. This method does
	 * not error at any non-label character so long as at least one character
	 * was read successfully.
	 * 
	 * Since the specification requires that all instructions are alpha, and
	 * that labels can be all alpha, this method can be used to read an
	 * instruction as well. If the instruction contains non-alpha characters,
	 * then it will not be found in the instructions table, and an error will be
	 * generated either way.
	 * 
	 * @author Josh Ventura
	 * @date Apr 5, 2012; 10:16:23 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors If invoked with position past the length of the string, an
	 *         exception will be thrown.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param from
	 *            The string from which the label will be copied.
	 * @param pos
	 *            The position of the first character of the label.
	 * @return A copy of the complete label.
	 * @throws URBANSyntaxException
	 *             If the label does not meet specification, an exception is
	 *             thrown with information on the violation.
	 * @specRef S2.1
	 */
	public static String readLabel(String from, int pos)
			throws URBANSyntaxException {
		final int spos = pos;
		if (!Character.isLetter(from.charAt(pos)))
			throw new URBANSyntaxException("Labels must start with a letter.",
					pos);
		// Skip to the first invalid label character.
		while (++pos < from.length() && isValidLabelChar(from.charAt(pos)));
		if (pos - spos > 32)
			throw new URBANSyntaxException(
					"Labels must be at most 32 characters in length; given label is "
							+ (pos - spos) + " characters.", spos + 32);
		return from.substring(spos, pos);
	}

	/**
	 * Check if a string contains all alpha characters (letters).
	 * 
	 * @author Josh Ventura
	 * @date Apr 5, 2012; 11:05:33 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param str
	 *            The string to check as alpha.
	 * @return True if the string is composed entirely of letters, false
	 *         otherwise.
	 * @specRef N/A
	 */
	public static boolean isAlpha(String str) {
		for (int i = 0; i < str.length(); i++)
			if (!Character.isLetter(str.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Check if a string contains all numerals.
	 * 
	 * @author Josh Ventura
	 * @date Apr 5, 2012; 11:05:33 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param str
	 *            The string to check as numeric.
	 * @return True if the string is composed entirely of numbers, false
	 *         otherwise.
	 * @specRef N/A
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++)
			if (!Character.isDigit(str.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Format an integer to a zero-padded decimal representation.
	 * 
	 * @date Apr 3, 2012; 3:09:00 PM
	 * @author Josh Ventura
	 * @modified Apr 7, 2012; 1:00:44 PM: Modified to simply call
	 *           formatIntegerWithRadix with 10 as the radix.
	 * @tested This function depends entirely on the output of
	 *         formatIntegerWithRadix.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param number
	 *            A number between 0 and (10^(digits+1)-1).
	 * @param digits
	 *            The number of digits to return.
	 * @return A byte[] containing the integer in decimal notation.
	 */
	public static String formatInteger(int number, int digits) {
		return new String(formatIntegerWithRadix(number, 10, digits));
	}

	/**
	 * Format an integer to a zero-padded hexadecimal representation.
	 * 
	 * @date Apr 3, 2012; 3:12:00 PM
	 * @author Josh Ventura
	 * @modified Apr 7, 2012; 1:00:44 PM: Modified to simply call
	 *           formatIntegerWithRadix with 16 as the radix.
	 * @tested This function depends entirely on the output of
	 *         formatIntegerWithRadix.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param number
	 *            A number between 0 and (10^(digits+1)-1).
	 * @param digits
	 *            The number of digits to return.
	 * @return A byte[] containing the integer in decimal notation.
	 */
	public static String formatHexInteger(int number, int digits) {
		return new String(formatIntegerWithRadix(number, 16, digits));
	}

	/**
	 * Format an integer to a zero-padded binary representation.
	 * 
	 * @date Apr 7, 2012; 1:00:44 PM
	 * @author Josh Ventura
	 * @modified UNMODIFIED
	 * @tested This function depends entirely on the output of
	 *         formatIntegerWithRadix.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param number
	 *            A number between 0 and (10^(digits+1)-1).
	 * @param digits
	 *            The number of digits to return.
	 * @return A byte[] containing the integer in decimal notation.
	 */
	public static String formatBinInteger(int number, int digits) {
		return new String(formatIntegerWithRadix(number, 2, digits));
	}

	/**
	 * An array of hex nybbles for quick construction of hex string
	 * representations.
	 */
	private static byte[] hexNybbles = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * @author Josh Ventura
	 * @date Apr 7, 2012; 12:09:39 PM
	 * @modified Apr 7, 2012; 9:31:52 PM: Cast value to long up front and
	 *           dropped bits of higher order than 31 to simulate unsigned.
	 * @tested Apr 7, 2012; 12:46:53 PM: Tested bases 2, 10, and 16 with the
	 *         integer 1337, requesting 20, 11, 5, 4, 3, 2, 1, and 0 digits.
	 * @errors An exception will be thrown if a string of negative size is
	 *         requested; an error is printed to stderr when the resulting
	 *         string does not contain sufficient digits to represent the
	 *         input number.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param number
	 *            The integer to format.
	 * @param radix
	 *            The radix with which to format, between 2 and 16.
	 * @param digits
	 *            The precise length of the string, or number of digits, to
	 *            return.
	 * @return A string representation of the given number in the given radix
	 *         with the given number of digits.
	 * @specRef N/A
	 */
	public static byte[] formatIntegerWithRadix(int number, int radix,
			int digits) {
		long fnum = (0L | number) & 0x00000000FFFFFFFFL;
		byte res[] = new byte[digits];
		for (int i = 0; i < digits; ++i)
			res[i] = '0';
		for (int i = digits - 1; i >= 0 && fnum > 0; --i) {
			res[i] = hexNybbles[(int) (fnum % radix)];
			fnum /= radix;
		}
		if (fnum > 0 && number > 0)
			System.err.println(digits + " digits is insufficient to store "
					+ number + " in base " + radix);
		return res;
	}

	/**
	 * Format a date as "YYYYDDD,HH:MM:SS".
	 * 
	 * @author Josh Ventura
	 * @modified UNMODIFIED
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
		append = formatIntegerWithRadix(calendar.get(Calendar.YEAR), 10, 4);
		for (int i = 0; i < 4; ++i)
			res[appat++] = append[i];
		append = formatIntegerWithRadix(calendar.get(Calendar.DAY_OF_YEAR), 10,
				3);
		for (int i = 0; i < 3; ++i)
			res[appat++] = append[i];
		append = formatIntegerWithRadix(calendar.get(Calendar.HOUR_OF_DAY), 10,
				2);
		res[appat++] = ',';
		for (int i = 0; i < 2; ++i)
			res[appat++] = append[i];
		res[appat++] = ':';
		append = formatIntegerWithRadix(calendar.get(Calendar.MINUTE), 10, 2);
		for (int i = 0; i < 2; ++i)
			res[appat++] = append[i];
		res[appat++] = ':';
		append = formatIntegerWithRadix(calendar.get(Calendar.SECOND), 10, 2);
		for (int i = 0; i < 2; ++i)
			res[appat++] = append[i];
		return res;
	}

	/**
	 * @author Josh Ventura
	 * @date Apr 14, 2012; 6:59:43 PM
	 * @modified This method will never need modified.
	 * @tested This method can not go wrong.
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param c
	 *            The character to check.
	 * @return Returns whether the given character is a hex nybble.
	 * @specRef N/A
	 */
	static boolean isHexNybble(char c) {
		switch (c) {// @formatter:off
		case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
		case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
		case '0': case '1': case '2': case '3': case '4': case '5':
		case '6': case '7': case '8': case '9':
			return true; // @formatter:on
		}
		return false;
	}

	/**
	 * Takes a string, such as "'Hello, world! I\'m a string!'" and returns
	 * the escaped version; here, "Hello, world! I'm a string!".
	 * 
	 * This method assumes that any strings have already been read in by a
	 * competent parser, and therefore, if a string is contained in the given
	 * parameter, it will begin and end with a quote.
	 * 
	 * @author Josh Ventura
	 * @date Apr 14, 2012; 6:43:28 PM
	 * @modified UNMODIFIED
	 * @tested Apr 14, 2012; 8:11:10 PM: Tested with a large string containing
	 *         many escape sequences.
	 * @errors Generates invalid string syntax errors. TODO-More info.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param str
	 *            The string to escape.
	 * @param line
	 *            A line number for error reporting purposes.
	 * @param pos
	 *            The position of this string in the line of code, for error
	 *            reporting purposes.
	 * @param hErr
	 *            An error handler to which any issues will be reported.
	 * @return The escaped version of the given string.
	 * @specRef N/A
	 */
	public static String escapeString(String str, int line, int pos,
			ErrorHandler hErr) {
		// @formatter:off
		StringBuilder sb = new StringBuilder(str.length());
		str = str.trim();
		if (str.length() == 0) {
			if (hErr != null)
				hErr.reportError("Invalid string given: string parameter is empty", line, pos);
			return "";
		}
		if (str.charAt(0) != '\'') {
			if (hErr != null)
				hErr.reportError("Invalid string parameter: strings are denoted by single quotes", line, pos);
			return "";
		}
			
		for (int p = 1; p < str.length(); ++p) {
			if (str.charAt(p) == '\\') {
				switch (str.charAt(++p)) {
				case 'n': sb.append('\n'); continue;
				case 'r': sb.append('\r'); continue;
				case 't': sb.append('\t'); continue;
				case 'x':
					if (isHexNybble(str.charAt(++p)))
						if (isHexNybble(str.charAt(++p)))
							sb.append((char) Integer.parseInt(str.substring(p - 1, p + 1), 16));
						else {
							sb.append((char) Integer.parseInt(str.substring(p - 1, p), 16));
							--p;
						}
					else {
						if (hErr != null)
							hErr.reportWarning("String escape sequence must contain at least one hex digit (not '" + str.charAt(p) + "'): escape omitted.",line,pos+p);
					}
					continue;
				case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7':
					if (isHexNybble(str.charAt(p)))
						if (isHexNybble(str.charAt(++p)))
							if (isHexNybble(str.charAt(++p)) && Integer.parseInt(str.substring(p - 1, p + 1)) < 256)
								sb.append((char) Integer.parseInt(str.substring(p - 2, p + 1), 8));
							else {
								sb.append((char) Integer.parseInt(str.substring(p - 1, p), 8));
								--p;
							}
						else {
							sb.append((char) Integer.parseInt(str.substring(p - 1, p), 8));
							--p;
						}
					else {
						if (hErr != null)
							hErr.reportWarning("String escape sequence must contain at least one octal digit.",line,pos+p);
					}
					continue;
				case '\\': sb.append('\\'); continue;
				case '\'': sb.append('\''); continue;
				case '\"': sb.append('\"'); continue;
				case '\n': case '\r':
					if (hErr != null)
						hErr.reportWarning("URBAN does not require escaping newlines in strings.", line, pos+p);
					pos--; continue;
				}
				hErr.reportWarning("Unknown escape sequence '\\" + str.charAt(p) + "': omitted.", line, pos+p);
			}
			if (str.charAt(p) == '\'') {
				if (hErr != null && p + 1 < str.length())
				  hErr.reportWarning("Unexpected end of string.", line, pos+p);
				return sb.toString();
			}
			sb.append(str.charAt(p));
		}
		// @formatter:on
		return sb.toString();
	}
}
