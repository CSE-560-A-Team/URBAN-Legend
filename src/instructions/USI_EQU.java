package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Module.Value.BitLocation;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The EQU instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef D3
 */
public class USI_EQU extends UIG_Equated {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "EQU";

	/** The static instance for this instruction. */
	static USI_EQU staticInstance = new USI_EQU(true);
	
	/** @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler, Module) */
	@Override public boolean immediateCheck(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		if (label == null) {
			hErr.reportError(makeError("noRQlabel", opId), lineNum, 0);
			isValid = false;
		}
		if (operands.size() > 1) {
			hErr.reportError(makeError("extraOperandsDir", opId), lineNum, -1);
			return false;
		}
		if (operands.size() < 1) {
			hErr.reportError(makeError("directiveMissingOp3", opId, "FC", "FL", "LR"), lineNum, -1);
			return false;
		}
		if (!"FC,FL,LR".contains(operands.get(0).operand)) {
			hErr.reportError(makeError("operandDirWrong", opId, operands.get(0).operand), lineNum, -1);
			hErr.reportError(makeError("directiveMissingOp3", opId, "FC", "FL", "LR"), lineNum, -1);
			return false;
		}
		value = module.evaluate(operands.get(0).expression, false, BitLocation.Literal, hErr, this, operands.get(0).valueStartPosition);
		return isValid;
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
		return new USI_EQU();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_EQU(boolean ignored) {
		super(opId);
		usage = Usage.EQUATE;
	}

	/** Default constructor; does nothing. */
	private USI_EQU() {}
}

