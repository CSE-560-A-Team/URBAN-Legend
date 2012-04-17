package instructions;

import assemblernator.AbstractDirective;
import static assemblernator.ErrorReporting.makeError;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.OperandChecker;

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
	String src = "";
	/** @see assemblernator.Instruction#check(ErrorHandler) */
	@Override public boolean check(ErrorHandler hErr) {
		boolean isValid = true;
		if (this.operands.size() < 1) {
			isValid = false;
			hErr.reportError(
					makeError("directiveMissingOp", this.getOpId(), "LR"),
					this.lineNum, -1);
		} else if (this.operands.size() == 1) {
			if (this.hasOperand("LR")) {
				src = "LR";
				isValid = OperandChecker.isValidLabel(this.getOperand("LR"));
				if (!isValid)
					hErr.reportError(
							makeError("OORlabel", "LR", this.getOpId()),
							this.lineNum, -1);
			} else {
				isValid = false;
				hErr.reportError(
						makeError("operandDirWrong", this.getOpId(), "any operand other than LR"),
						this.lineNum, -1);
			}
		} else if (this.operands.size() == 2) {
			if (this.countOperand("LR") == 2) {
				src = "LRLR";
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 0));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 1));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);

			}else{
				isValid = false;
				hErr.reportError(
						makeError("operandDirWrong", this.getOpId(), "any operand other than LR"),
						this.lineNum, -1);
			}
		} else if (this.operands.size() == 3) {
			if (this.countOperand("LR") == 3) {
				src = "LRLRLR";
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 0));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 1));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 2));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);

			}else{
				isValid = false;
				hErr.reportError(
						makeError("operandDirWrong", this.getOpId(), "any operand other than LR"),
						this.lineNum, -1);
			}
		} else if (this.operands.size() == 4) {
			if (this.countOperand("LR") == 4) {
				src = "LRLRLRLR";
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 0));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 1));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 2));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);
				isValid = OperandChecker.isValidLabel(this.getOperand("LR", 3));
				if (!isValid)hErr.reportError(makeError("OORlabel", "LR", this.getOpId()),this.lineNum, -1);

			}else{
				isValid = false;
				hErr.reportError(
						makeError("operandDirWrong", this.getOpId(), "any operand other than LR"),
						this.lineNum, -1);
			}
		} else {
			isValid = false;
			hErr.reportError(makeError("extraOperandsDir", this.getOpId()),
					this.lineNum, -1);
		}
		return isValid; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return null; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler) */
	@Override public boolean immediateCheck(ErrorHandler hErr) {
		// TODO Auto-generated method stub
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

