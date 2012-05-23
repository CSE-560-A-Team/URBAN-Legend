package simulanator;

import static assemblernator.ErrorReporting.makeError;

import java.io.InputStream;
import java.util.Scanner;

import assemblernator.Assembler;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;

/**
 * @author Josh Ventura
 * @date May 12, 2012; 4:53:16 PM
 */
public class Simulator {
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
	 * @param machine
	 *            The URBAN Machine into which memory is loaded.
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
	public static void load(InputStream loaderFile, ErrorHandler hErr, Machine machine) {
		int loadAddr;
		int execStart;
		int progLen;
		int asmVer;

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

			if (!rd.go(progLen = rd.readInt(ScanWrap.hex4, "loaderHNoPrL", 16))) // LM1.9
				return;
			if (progLen > 4096) {
				hErr.reportError(makeError("loaderPrLnOOR", "" + progLen), 0, 0);
				return;
			}

			if (s.nextByte() != ':') { // LM1.10
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}

			if (!rd.go(rd.readString(ScanWrap.datep, "loaderHNoPrL"))) // LM1.11
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
			int numRecords = 0;
			for (;;) { // LM2
				recname = s.nextByte();
				if (recname != 'T') // LM2.1
					break;

				if (s.nextByte() != ':') { // LM2.2
					hErr.reportError(makeError("missingSeparator"), 0, 0);
					return;
				}

				int addr; // LM2.3
				if (!rd.go(addr = rd
						.readInt(ScanWrap.hex6, "loaderTNoAddr", 16)))
					return;
				if (addr > 4095)
					hErr.reportError(
							makeError("loaderTAddOOR",
									Integer.toString(addr, 16)), 0, 0);

				if (s.nextByte() != ':') { // LM2.4
					hErr.reportError(makeError("missingSeparator"), 0, 0);
					return;
				}

				int word; // LM2.5
				if (!rd.go(word = rd
						.readInt(ScanWrap.hex8, "loaderTNoWord", 16)))
					return;

				machine.memory[addr] = word;

				if (s.nextByte() != ':') { // LM2.6
					hErr.reportError(makeError("missingSeparator"), 0, 0);
					return;
				}

				if (!rd.go(crcStr = rd.readString(ScanWrap.notcolon,
						"loaderNoCRC"))) // LM2.7
					return;
				if (crcStr.equals(progName)) { // LM2.7, continued
					hErr.reportError(makeError("datafileCRCFail"), 0, 0);
					return;
				}

				if (s.nextByte() != ':') { // LM2.8
					hErr.reportError(makeError("missingSeparator"), 0, 0);
					return;
				}

				++numRecords;
			}

			if (recname != 'E') { // LM3.1
				hErr.reportError(makeError("missingEndR"), 0, 0);
				return;
			}

			if (s.nextByte() != ':') { // LM3.2
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}

			int predNR; // LM3.3
			if (!rd.go(predNR = rd.readInt(ScanWrap.hex4, "loaderTNoAddr", 16)))
				return;
			if (predNR != numRecords + 2)
				hErr.reportError(
						makeError("loaderInvRecC", "" + (numRecords + 2), ""
								+ predNR), 0, 0);

			if (s.nextByte() != ':') { // LM3.4
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}

			// LM3.5
			if (!rd.go(predNR = rd.readInt(ScanWrap.hex4, "loaderTNoAddr", 16)))
				return;
			if (predNR != numRecords)
				hErr.reportError(
						makeError("loaderInvRecC", "" + numRecords, "" + predNR),
						0, 0);

			if (s.nextByte() != ':') { // LM3.6
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}

			if (!rd.go(crcStr = rd.readString(ScanWrap.notcolon, "loaderNoCRC"))) // LM2.7
				return;
			if (crcStr.equals(progName)) { // LM2.7, continued
				hErr.reportError(makeError("datafileCRCFail"), 0, 0);
				return;
			}

			if (s.nextByte() != ':') { // LM3.8
				hErr.reportError(makeError("missingSeparator"), 0, 0);
				return;
			}
		} catch (Exception e) {
			hErr.reportError(makeError("loaderReadFail"), 0, 0);
			return;
		}

		if (asmVer > Assembler.VERSION)
			machine.hErr.reportWarning(
					makeError("newerAssemler", "" + Assembler.VERSION, ""
							+ asmVer), 0, 0);
		machine.lc = loadAddr;
		machine.lc = execStart;
	}
}
