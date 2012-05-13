package simulanator;

import static assemblernator.ErrorReporting.makeError;

import java.io.InputStream;
import java.util.Scanner;

import assemblernator.ErrorReporting.ErrorHandler;

/**
 * @author Josh Ventura
 * @date May 12, 2012; 4:53:16 PM
 */
public class Simulator {
	/** The machine on which we are running */
	Machine machine;

	/**
	 * @author Josh Ventura
	 * @date May 12, 2012; 4:53:37 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param loaderFile
	 *            Stream containing the loader file of the program to run.
	 * @param hErr An error handler to which any problems in the load will be reported.
	 * @specRef N/A
	 */
	void load(InputStream loaderFile, ErrorHandler hErr) {
		try {
			Scanner s = new Scanner(loaderFile);
			if (s.nextByte() != 'H' || s.nextByte() != ':') {
				hErr.reportError(makeError("loaderNoHeader"), 0, 0);
				return;
			}
			
			
		} catch (Exception e) {
			hErr.reportError(makeError("loaderReadFail"), 0, 0);
		}
	}
}
