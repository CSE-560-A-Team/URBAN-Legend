package simulanator;

import static assemblernator.ErrorReporting.makeError;

import java.util.Scanner;
import java.util.regex.Pattern;

import assemblernator.ErrorReporting.ErrorHandler;

/**
 * @author Josh Ventura
 * @date May 15, 2012; 12:27:40 AM
 */
public class ScanWrap {
	/** Whether the last read was successful. */
	private boolean success = false;
	/** An error handler which will receive error messages. */
	private ErrorHandler hErr;
	/** The scanner to poll. */
	private Scanner scan;

	/** Pattern matching four hex nybbles */
	public static final Pattern hex4 = Pattern.compile("[0-9A-Fa-f]{4}");
	/** Pattern matching six hex nybbles */
	public static final Pattern hex6 = Pattern.compile("[0-9A-Fa-f]{6}");
	/** Pattern matching eight hex nybbles */
	public static final Pattern hex8 = Pattern.compile("[0-9A-Fa-f]{8}");
	/** Pattern matching four decimal digits */
	public static final Pattern dec4 = Pattern.compile("[0-9]{4}");
	/** Pattern matching anything that isn't a colon */
	public static final Pattern notcolon = Pattern.compile("[^:]+");
	/** Pattern matching one of Al's dates */
	public static final Pattern datep = Pattern
			.compile("[0-9]{7},[0-9][0-9]:[0-9][0-9]:[0-9][0-9]");

	/**
	 * @author Josh Ventura
	 * @date May 15, 2012; 12:29:46 AM
	 * @param p
	 *            The pattern to read.
	 * @param err
	 *            The ID of the error message to report in case of failure.
	 * @param base
	 *            The base of the integer matched by the pattern.
	 * @return The integer value, or -1 in the event of failure. Use go() to
	 *         test for success.
	 * @specRef N/A
	 */
	public int readInt(Pattern p, String err, int base) {
		if (!scan.hasNext(p)) { // LM1.5
			hErr.reportError(makeError("loaderHNoAddr"), 0, 0);
			success = false;
			return -1;
		}
		success = true;
		return Integer.parseInt(scan.next(p), base);
	}

	/**
	 * @author Josh Ventura
	 * @date May 15, 2012; 12:30:48 AM
	 * @param p
	 *            The pattern to read.
	 * @param err
	 *            The ID of the error message to report in case of failure.
	 * @return The String that was read, or null in the event of failure. Use
	 *         go() to test for success.
	 * @specRef N/A
	 */
	public String readString(Pattern p, String err) {
		if (!scan.hasNext(p)) { // LM1.5
			hErr.reportError(makeError("loaderHNoAddr"), 0, 0);
			success = false;
			return null;
		}
		success = true;
		return scan.next(p);
	}

	/**
	 * @param disregard
	 *            Disregarded.
	 * @return Returns true if the last read succeeded, false otherwise.
	 */
	public boolean go(Object disregard) {
		return success;
	}

	/**
	 * @param And
	 *            An additional success conditional expression.
	 * @return Returns true if the last read succeeded, and the passed
	 *         expression is true; returns false otherwise.
	 */
	boolean go(boolean And) {
		return success && And;
	}

	/**
	 * @param s
	 *            The Scanner which will be polled for data.
	 * @param herr
	 *            The error handler which will receive error messages.
	 */
	public ScanWrap(Scanner s, ErrorHandler herr) {
		scan = s;
		hErr = herr;
	}
}