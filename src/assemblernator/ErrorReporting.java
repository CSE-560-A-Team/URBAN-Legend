package assemblernator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Java name space for any error reporting mechanisms or utility
 * functions/facilitators thereof.
 * 
 * @author Josh Ventura
 * @date Apr 11, 2012; 10:14:41 AM
 */
public class ErrorReporting {
	/**
	 * Class used for reporting fatal errors via Java's exception mechanism.
	 * 
	 * @author Josh Ventura
	 * @date Apr 8, 2012; 11:16:57 AM
	 */
	public static class URBANSyntaxException extends Throwable {
		/** Make Eclipse's compiler shut up */
		private static final long serialVersionUID = 1L;
		/** The index on the line at which this error occurred */
		public int index;

		/**
		 * @param message
		 *            The error to report
		 * @param idx
		 *            The index from which the error is being reported.
		 */
		public URBANSyntaxException(String message, int idx) {
			super(message);
			index = idx;
		}
	}

	/**
	 * An interface that can handle reporting errors and warnings.
	 * 
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 10:15:16 AM
	 */
	public static interface ErrorHandler {
		/**
		 * Report an error to the user.
		 * 
		 * @author Josh Ventura
		 * @date Apr 11, 2012; 10:16:02 AM
		 * @modified UNMODIFIED
		 * @tested As this is an abstract method, its success depends on the
		 *         implementation.
		 * @errors This is the error reporting mechanism. All errors that the
		 *         user sees go through it.
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param err
		 *            The text of the error to report.
		 * @param line
		 *            The line from which the error was thrown.
		 * @param pos
		 *            The position in the line at which the error was thrown,
		 *            or -1 if the error is related to the line as a whole.
		 * @specRef N/A: The specifications upheld depend on the caller.
		 */
		void reportError(String err, int line, int pos);

		/**
		 * Report a warning to the user.
		 * 
		 * @author Josh Ventura
		 * @date Apr 11, 2012; 10:16:42 AM
		 * @modified UNMODIFIED
		 * @tested As this is an abstract method, its success depends on the
		 *         implementation.
		 * @errors This is the warning reporting mechanism; all warnings that
		 *         the user sees go through it.
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param warn
		 *            The text of the warning to report.
		 * @param line
		 *            The line from which the warning was reported.
		 * @param pos
		 *            The position in the line at which the warning was
		 *            reported, or -1 if the warning is related to the line
		 *            as a whole.
		 * @specRef N/A: The specifications upheld depend on the caller.
		 */
		void reportWarning(String warn, int line, int pos);
	}

	/**
	 * Default error handler; print any problems to STDERR.
	 * 
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 3:57:53 PM
	 */
	static class DefaultErrorHandler implements ErrorHandler {

		/**
		 * Print the warning to STDERR.
		 * 
		 * @see assemblernator.ErrorReporting.ErrorHandler#reportError(java.lang.String,int,int)
		 */
		@Override public void reportError(String err, int line, int pos) {
			System.err.println("URBAN: ERROR: Line " + line
					+ (pos == -1 ? ": " : ", position " + pos + ": ") + err);
		}

		/**
		 * Print the warning to STDERR.
		 * 
		 * @see assemblernator.ErrorReporting.ErrorHandler#reportWarning(java.lang.String,int,int)
		 */
		@Override public void reportWarning(String warn, int line, int pos) {
			System.err.println("URBAN: Warning: Line " + line
					+ (pos == -1 ? ": " : ", position " + pos + ": ") + warn);
		}
	}

	/** The properties file from which error messages will be read. */
	static Properties errPropFile = null;

	/**
	 * Forge an error string from an error ID string and any identifiers that
	 * are to be inserted.
	 * 
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 10:22:15 AM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param errId
	 *            The string-ID of the error message to use.
	 * @param idents
	 *            Any strings that will be inserted into the error message.
	 * @return Returns the complete error message.
	 * @specRef N/A
	 */
	public static final String makeError(String errId, String... idents) {
		if (errPropFile == null) {
			try {
				errPropFile.load(new FileInputStream("errors.properties"));

			} catch (IOException ex) {
				return "Error[" + errId
						+ "] occurred, but errors file does not exist.";
			}
		}
		String rawErrStr = errPropFile.getProperty(errId), outputStr;
		if (rawErrStr == null)
			return "Error[" + errId + "] occurred, but lookup of error failed.";
		outputStr = rawErrStr;
		for (int i = 0; i < idents.length; i++) {
			final String b4str = outputStr;
			outputStr = outputStr.replace("%" + i, idents[i]);
			if (outputStr == b4str) {
				System.err.println("ARGUMENT " + i
						+ " IS UNUSED IN ERROR TEXT!");
				outputStr += "[EUUP:" + i + "]";
			}
		}
		return null;
	}
}