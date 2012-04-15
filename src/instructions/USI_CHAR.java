package instructions;

import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.OperandChecker;

import static assemblernator.ErrorReporting.makeError;

/**
 * The CHAR instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef D10
 */
public class USI_CHAR extends Instruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "CHAR";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0xFFFFFFFF; // This instruction doesn't have an opcode.

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
	 * @param byteCount The number of bytes to pad to words.
	 * @return The number of words needed to hold byteCount bytes.
	 * @specRef N/A
	 */
	int padWord(int byteCount) {
		return Math.max(1, (byteCount + 3) / 4);
	}
	

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		String st = getOperand("ST");
		if (st == null)
			return lc;
		int stringWordSize = padWord(IOFormat.escapeString(st, 0, 0, null).getBytes().length);
		return lc + Math.max(1, stringWordSize);
	}

	/** The string given to us in the ST: operand. */
	private String content;

	/** @see assemblernator.Instruction#check(ErrorHandler) */
	@Override public boolean check(ErrorHandler hErr) {
		Operand st = getOperandData("ST");
		if (st == null)
			hErr.reportError(makeError("directiveMissingOp", "CHAR", "ST"),
					lineNum, -1);
		else if (OperandChecker.isValidString(st.operand))
			hErr.reportError(makeError("STstringCount"), lineNum, -1);
		else {
			content = IOFormat.escapeString(st.operand, lineNum, st.valueStartPosition, hErr);
			return true;
		}
		return false;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		byte[] resb = content.getBytes();
		int[] res = new int[padWord(resb.length)];
		for (int i = 0; i < res.length; i++)
			res[i] = resb[i] | (resb[i + 1] << 8) | (resb[i + 2] << 16)
					| (resb[i + 3] << 24);
		return res;
	}

	/** @see assemblernator.Instruction#execute(int) */
	@Override public void execute(int instruction) {
		throw new NullPointerException(
				"The CHAR instruction should not be invoked for execute!");
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

	/** @see assemblernator.Instruction#getOpcode() */
	@Override public int getOpcode() {
		return opCode;
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
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_CHAR() {}
}
