package instructions;

import static assemblernator.ErrorReporting.makeError;
import ulutil.IOFormat;
import assemblernator.AbstractDirective;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.Module.Value;
import assemblernator.OperandChecker;

/**
 * The CHAR instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef D10
 */
public class USI_CHAR extends AbstractDirective {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "CHAR";

	/** The static instance for this instruction. */
	static USI_CHAR staticInstance = new USI_CHAR(true);


	/**
	 * @author Josh Ventura
	 * @date Apr 15, 2012; 11:42:58 AM
	 * @modified UNMODIFIED
	 * @tested Apr 15, 2012; 11:43:13 AM: Used to calculate new FC correctly for
	 *         strings of various sizes.
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param byteCount
	 *            The number of bytes to pad to words.
	 * @return The number of words needed to hold byteCount bytes.
	 * @specRef N/A
	 */
	public static int padWord(int byteCount) {
		return Math.max(1, (byteCount + 3) / 4);
	}


	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		if (content == null)
			return lc + 1;
		int stringWordSize = padWord(content.getBytes().length);
		return lc + stringWordSize;
	}

	/** The string given to us in the ST: operand. */
	private String content;

	/**
	 * @author Josh Ventura
	 * @param bytes
	 *            The bytes to cast to int[].
	 * @return The given byte array, as an array of integers.
	 */
	public static int[] strToIntArray(byte[] bytes) {
		int[] res = new int[padWord(bytes.length)];
		int nw = 0;
		for (int i = 0; i < bytes.length; ++i) {
			int tw = bytes[i] << 24;
			if (++i < bytes.length) {
				tw |= bytes[i] << 16;
				if (++i < bytes.length) {
					tw += bytes[i] << 8;
					if (++i < bytes.length)
						tw |= bytes[i];
					else
						tw |= 0x00000020;
				}
				else
					tw |= 0x00002020;
			}
			else
				tw |= 0x00202020;
			res[nw++] = tw;
		}
		return res;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return strToIntArray(content.getBytes());
	}

	/**
	 * @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler,
	 *      Module)
	 */
	@Override public boolean immediateCheck(ErrorHandler hErr, Module module) {
		Operand st = getOperandData("ST");
		if (st == null)
			hErr.reportError(makeError("directiveMissingOp", "CHAR", "ST"),
					lineNum, -1);
		else if (!OperandChecker.isValidString(st.expression))
			hErr.reportError(makeError("STstringCount"), lineNum, -1);
		else {
			content = IOFormat.escapeString(st.expression, lineNum,
					st.valueStartPosition, hErr);
			st.value = new Value(0, 'A');
			if (operands.size() != 1)
				hErr.reportWarning(makeError("extraParamsIgF", opId, "ST"),
						lineNum, 0);
			return true;
		}
		return false;
	}

	// =========================================================
	// === Redundant code ======================================
	// =========================================================
	// === This code's the same in all instruction classes, ====
	// === But Java lacks the mechanism to allow stuffing it ===
	// === in super() where it belongs. ========================
	// =========================================================

	/**
	 * @see Instruction
	 * @return The static instance of this instruction.
	 */
	public static Instruction getInstance() {
		return staticInstance;
	}

	/** @see assemblernator.Instruction#getOpId() */
	@Override public String getOpId() {
		return opId;
	}

	/** @see assemblernator.Instruction#getNewInstance() */
	@Override public Instruction getNewInstance() {
		return new USI_CHAR();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_CHAR(boolean ignored) {
		super(opId);
	}

	/** Default constructor; does nothing. */
	private USI_CHAR() {}
}
