package assemblernator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * I/O class for generated object files.
 * 
 * @author Josh Ventura
 */
public class ObjectWriter {
	/**
	 * @date 2012-04-03 Tuesday 02:33PM Check a label for validity against the
	 *       URBAN specification, V1.0.
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors This function facilitates reporting errors. It does not
	 *                  generate them.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param out
	 *            The stream to which the header will be written.
	 * @param progname
	 *            The name of the program being assembled, which must be a valid
	 *            label.
	 * @param programAddr
	 *            Assembler assigned program load address, per #OBJ1.3.
	 * @param moduleLength
	 *            The total length of the module.
	 * @param execStart
	 *            The starting address of execution.
	 * @param asmblrVersion
	 *            The version number of the assembler.
	 * @throws IOException
	 *             Throws an IOException if data cannot be written.
	 * @throws Exception
	 *             Throws Exception if the program name is not a valid label.
	 * @specRef OB1
	 * @specRef OB1.1
	 * @specRef OB1.2
	 * @specRef OB1.3
	 * @specRef OB1.4
	 * @specRef OB1.5
	 * @specRef OB1.6
	 * @specRef OB1.7
	 * @specRef OB1.8
	 * @specRef OB1.9
	 * @specRef OB1.18
	 * @specRef OB1.19
	 * @specRef OB1.20
	 * @specRef OB1.21
	 * @specRef S2.1
	 */
	public static void writeHeaderRecord(OutputStream out, String progname,
			int programAddr, int moduleLength, int execStart, int asmblrVersion)
			throws IOException, Exception {
		// ===================================================================
		// ==== Check formatting. ============================================
		// ===================================================================

		if (progname.length() > 32 || IOFormat.isValidLabel(progname))
			throw new Exception("Program name is not in valid label format");

		// ===================================================================
		// ==== Write header. ================================================
		// ===================================================================

		// OB1.1: 'H' - Single character
		out.write('H');
		// OB1.2: ':' - Single character
		out.write(':');
		// OB1.3: `Program name` - String of 1 to 32 characters and numbers
		// meeting syntax rules for a label
		out.write(progname.getBytes());
		// OB1.2: ':' - Single character
		out.write(':');
		// OB1.3: `Assembler-assigned program load address` - 4 digit hex number HHHH
		out.write(IOFormat.formatIntegerWithRadix(programAddr, 16, 4));
		// OB1.2: ':' - Single character
		out.write(':');
		// OB1.3: `Total Module length` - 4 nybble hex number (0 to 03FF)
		out.write(IOFormat.formatIntegerWithRadix(moduleLength, 16, 4));
		// OB1.4: ':' - Single character
		out.write(':');
		// OB1.5: `Execution start address` - 4 nybble hex number (0 to 03FF)
		out.write(IOFormat.formatIntegerWithRadix(execStart, 16, 4));
		// OB1.6: ':' - Single character
		out.write(':');
		// OB1.7: `Date/time of Assembly` - "YYYYDDD,HH:MM:SS"
		out.write(IOFormat.formatDate(new Date()));
		// OB1.8: ':' - Single character
		out.write(':');
		// OB1.9: `Assembler version number` - 4 digit integer
		out.write(IOFormat.formatIntegerWithRadix(asmblrVersion, 16, 4));
		// OB1.18: ':' - Single character
		out.write(':');
		// OB1.19: "URBAN-ASM" - 9 character string
		out.write("URBAN-ASM".getBytes());
		// OB1.20: ':' - Single character
		out.write(':');
		// OB1.21: `Program name` - See OB1.3
		out.write(progname.getBytes());
	}
	
	/**
	 * Writes a linking record to OutputStream.
	 * @author Noah
	 * @date Apr 28, 2012; 6:03:39 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param out output stream.
	 * @param label label of entry.
	 * @param progName name of program.
	 * @param lc address of entry.
	 * @throws IOException throws if data cannot be written.
	 * @specRef N/A
	 */
	public static void writeLinkingRecord(OutputStream out, String label, String progName, int lc) throws IOException {
		out.write('L');
		out.write(':');
		out.write(label.getBytes()); //label
		out.write(':');
		out.write(IOFormat.formatIntegerWithRadix(lc, 16, 4)); //address of label.
		out.write(':');
		out.write(progName.getBytes()); //program name
	}
	
	/**
	 * 
	 * @author Noah
	 * @date Apr 28, 2012; 6:17:13 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param out output stream.
	 * @param progName name of program.
	 * @param lc address of instruction/directive.
	 * @param code code for the instruction/directive.
	 * @param mods required M adjustements for instruction/directive.
	 * @param relocFlag specifies type of address.
	 * @throws IOException throws if data cannot be written.
	 * @specRef N/A
	 */
	public static void writeTextRecord(OutputStream out, String progName, 
			int lc, int code, int mods, char relocFlag) throws IOException {
		out.write('T');
		out.write(':');
		out.write(IOFormat.formatIntegerWithRadix(lc, 16, 4)); //address of instruction/directive
		out.write(':');
		out.write(IOFormat.formatIntegerWithRadix(code, 16, 8)); //code for instruction/directive
		out.write(':');
		out.write(IOFormat.formatIntegerWithRadix(mods, 16, 1)); //modification count for instruction/directive.
		out.write(':');
		out.write(progName.getBytes());
	}
	
}
