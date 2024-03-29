package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The MOVDN instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef MV1
 */
public class USI_MOVDN extends UIG_Arithmetic {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "MOVDN";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x01; // 0b000001

	/** The static instance for this instruction. */
	static USI_MOVDN staticInstance = new USI_MOVDN(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc + 1;
	}


	/** Returns value to arithmetic */
	@Override int operate(int destValue, int srcValue, ErrorHandler hErr) {
		long x = (long)-srcValue;
		if(x > Integer.MAX_VALUE){
			hErr.reportError(makeError("runOverflow"),0,0);
			return 0;
		}else{
			return -srcValue;
		}
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
		return new USI_MOVDN();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_MOVDN(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_MOVDN() {}
}
