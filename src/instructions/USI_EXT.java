package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.AbstractDirective;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The EXT instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef D6
 */
public class USI_EXT extends AbstractDirective {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "EXT";

	/** The static instance for this instruction. */
	static USI_EXT staticInstance = new USI_EXT(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc;
	}

	/**
	 * The type of operand specifying the source for this operation.
	 */
	String src = "";
	
	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		return true;
	}

	/**
	 * @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler,
	 *      Module)
	 */
	@Override public boolean immediateCheck(ErrorHandler hErr, Module module) {
		if (this.operands.size() < 1) {
			hErr.reportError(
					makeError("directiveMissingOp", this.getOpId(), "LR"),
					this.lineNum, -1);
			return false;
		}
		else {
			for (int i = 0; i < operands.size(); i++) {
				if (!operands.get(i).operand.equals("LR")) {
					hErr.reportError(
							makeError("operandDirWrong", this.getOpId(),
									operands.get(i).operand), this.lineNum, -1);
					return false;
				}
				if (!IOFormat.isValidLabel(operands.get(i).expression.trim())) {
					hErr.reportError(makeError("singLabelExp","LR"), lineNum, operands.get(i).valueStartPosition);
					return false;
				}
			}
		}
		return true;
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
		return new USI_EXT();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_EXT(boolean ignored) {
		super(opId);
		usage = Usage.EXTERNAL;
	}

	/** Default constructor; does nothing. */
	private USI_EXT() {}
}
