package simulanator;

import static assemblernator.ErrorReporting.makeError;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

import assemblernator.Assembler;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;

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
	 * @param hErr
	 *            An error handler to which any problems in the load will be
	 *            reported.
	 * @specRef LM1
	 * @specRef LM1.1
	 * @specRef LM1.2
	 * @specRef LM1.3
	 * @specRef LM1.4
	 * @specRef LM1.5
	 * @specRef LM1.6
	 * @specRef LM1.7
	 * @specRef LM1.8
	 * @specRef LM1.9
	 * @specRef LM1.10
	 * @specRef LM1.11
	 * @specRef LM1.12
	 * @specRef LM1.13
	 * @specRef LM1.14
	 * @specRef LM1.15
	 * @specRef LM1.16
	 * @specRef LM1.17
	 * @specRef LM1.18
	 */
	void load(InputStream loaderFile, ErrorHandler hErr) {
		int loadAddr;
		int execStart;
		int progLength;
		int asmVer;
		Machine m = new Machine();

		try {
			Scanner s = new Scanner(loaderFile);
			String crcStr;
			String progName;

			ScanWrap rd = new ScanWrap(s, hErr);

			if (s.nextByte() != 'H' || s.nextByte() != ':') { // LM1.1, LM1.2
				hErr.reportError(makeError("loaderNoHeader"), 0, 0);
				return;
			}

			if (!rd.go(progName = rd.readString(ScanWrap.notcolon,
					"loaderNoName"))) // LM1.3
				return;

			if (!IOFormat.isValidLabel(progName)) {
				hErr.reportError(makeError("loaderInvName"), 0, 0);
				return;
			}

			if (s.nextByte() != ':') { // LM1.4
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}
			if (!rd.go(loadAddr = rd
					.readInt(ScanWrap.hex4, "loaderHNoAddr", 16))) // LM1.5
				return;

			if (s.nextByte() != ':') { // LM1.6
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}

			if (!rd.go(execStart = rd.readInt(ScanWrap.hex4, "loaderNoEXS", 16))) // LM1.7
				return;
			if (s.nextByte() != ':') { // LM1.8
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}
			if (!rd.go(progLength = rd.readInt(ScanWrap.hex4, "loaderHNoPrL",
					16))) // LM1.9
				return;

			if (s.nextByte() != ':') { // LM1.10
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}

			if (!rd.go(progLength = rd.readInt(ScanWrap.datep, "loaderHNoPrL",
					16))) // LM1.11
				return;

			if (s.nextByte() != ':') { // LM1.12
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}
			if (!rd.go(rd.readString(ScanWrap.notcolon, "loaderHNoLLMM"))) // LM1.13
				return;

			if (!rd.go(rd.readString(ScanWrap.notcolon, "loaderHNoLLMM")
					.equals("URBAN-LLM")))
				return;

			if (s.nextByte() != ':') { // LM1.14
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}
			if (!rd.go(asmVer = rd.readInt(ScanWrap.dec4, "loaderHNoVer", 10))) // LM1.15
				return;

			if (s.nextByte() != ':') { // LM1.16
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}


			if (!rd.go(crcStr = rd.readString(ScanWrap.notcolon, "loaderNoCRC"))) // LM1.17
				return;
			if (crcStr.equals(progName)) { // LM1.17, continued
				hErr.reportError(makeError("datafileCRCFail"), 0, 0);
				return;
			}

			if (s.nextByte() != ':') { // LM1.18
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}

			byte recname;
			for (;;) {
				recname = s.nextByte();
				if (recname != 'T')
					break;

				Pattern hexnp = Pattern.compile("[0-9A-Fa-f]{6}");
				if (!s.hasNext(hexnp)) { // LM1.5
					hErr.reportError(makeError("loaderHNoAddr"), 0, 0);
					return;
				}
				loadAddr = Integer.parseInt(s.next(hexnp), 16);

				if (s.nextByte() != ':') { // LM1.14
					hErr.reportError(makeError("missingSeparator"), 0, 0);
					return;
				}

				hexnp = Pattern.compile("[0-9A-Fa-f]{8}");
				if (!s.hasNext(hexnp)) { // LM1.5
					hErr.reportError(makeError("loaderHNoAddr"), 0, 0);
					return;
				}
				loadAddr = Integer.parseInt(s.next(hexnp), 16);

			}

			if (recname != 'E') {
				hErr.reportError(makeError("missingEndR"), 0, 0);
			}
		} catch (Exception e) {
			hErr.reportError(makeError("loaderReadFail"), 0, 0);
			return;
		}

		if (asmVer > Assembler.VERSION)
			m.hErr.reportWarning(
					makeError("newerAssemler", "" + Assembler.VERSION, ""
							+ asmVer), 0, 0);
		m.lc = execStart;
	}
}
