package simulanator;

import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;
import java.util.Stack;

/**
 * A class representing an entire machine state.
 * 
 * @author Josh Ventura
 * @date Apr 28, 2012; 1:09:45 AM
 */
public class Machine {
	/** The size of the memory of the URBAN machine. */
	public static final int memorySizeInWords = 4096;

	/** Our eight machine registers. */
	public int[] registers = new int[8];
	/** Our seven index registers: Index register 0 is unused. */
	public int[] indexRegisters = new int[8];
	/** Our entire memory; all 16 kibibytes of it. */
	public int[] memory = new int[memorySizeInWords];
	/** The current program counter. */
	public int lc;
	/** The current instruction. */
	public int instruction;

	/** Stack of integers. */
	public Stack<Integer> stack;

	/** An error channel to which any access violations can be reported. */
	ErrorHandler hErr;

	/**
	 * Creates a string dump of this machine state, according to spec.
	 * 
	 * @author Josh Ventura
	 * @date Apr 28, 2012; 1:23:42 AM
	 * @modified UNMODIFIED
	 * @tested Apr 28, 2012; 1:41:00 AM: Tested with empty memory.
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param level
	 *            The detail level for this dump; a value of one dumps all
	 *            registers, a value of two dumps the memory, and a value of
	 *            three dumps both.
	 * @return A String containing the dump of this machine state.
	 * @specRef C1
	 * @specRef DUMP Format
	 */
	public String dump(int level) {
		String res = "";
		if ((level & 1) != 0) {
			res += "LC " + lc + "  WORD="
					+ IOFormat.formatHexInteger(instruction, 8) + "\n";
			for (int i = 0; i < 8; ++i)
				res += "R" + i + "="
						+ IOFormat.formatHexInteger(registers[i], 8)
						+ (i % 4 == 3 ? "\n" : " ");
			res += "\n";
		}
		if ((level & 2) != 0) {
			for (int row = 0; row < memorySizeInWords; row += 8) {
				res += IOFormat.formatHexInteger(row, 4) + ": ";
				for (int col = row; col < row + 8 && col < memorySizeInWords; ++col)
					res += " " + IOFormat.formatHexInteger(memory[col], 8);
				res += "\n";
			}
		}
		return res;
	}

	/** @see java.lang.Object#toString() */
	@Override public String toString() {
		return dump(3);
	}
}
