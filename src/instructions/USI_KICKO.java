package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.OperandChecker.isValidConstant;
import assemblernator.AbstractDirective;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.Module.Value;

/**
 * The KICKO instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef D1
 */
public class USI_KICKO extends AbstractDirective {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "KICKO";

	/** The static instance for this instruction. */
	static USI_KICKO staticInstance = new USI_KICKO(true);

	/** User defined program start address */
	int ownLC;
	
	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		this.lc = ownLC;
		return ownLC;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return null; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler, Module) 
	 * @modified 12:43:13 changed lineNum check to != 1. first line = 1.
	 * 			 11:02:09PM;  moved check to immediateCheck - Noah
	 * 			 11:16:32 PM; changed isValidConstant.
	 */
	@Override public boolean immediateCheck(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		
		if (this.lineNum != 1) { //KICKO's should only be found at beginning of source.
			isValid = false;
			hErr.reportError(makeError("KICKOlineNum"), this.lineNum, -1);
		} else if (!this.hasOperand("FC")) { //KICKO must have FC operand.
			isValid = false;
			hErr.reportError(makeError("directiveMissingOp", this.getOpId(), "FC"), this.lineNum, -1);
		} else if (this.operands.size() > 1) { //KICKO can only have FC operand.
			isValid = false;
			hErr.reportError(makeError("extraOperandsDir", this.getOpId()), this.lineNum, -1);
		} else {
			// We know at this point that operands.get(0) is our one and only operand, FC.
			 Value tempv = module.evaluate(operands.get(0).expression, false, hErr, this, operands.get(0).valueStartPosition);
			 if (tempv.arec != 'A')
				 hErr.reportError(makeError("nonConstExpr", operands.get(0).expression), lineNum, operands.get(0).valueStartPosition);
			 ownLC = tempv.value;
			if (!isValidConstant(ownLC, ConstantRange.RANGE_ADDR)) {
				isValid = false;
				hErr.reportError(makeError("OORmemAddr", "FC", this.getOpId()), this.lineNum, -1);
			}
		}

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
		return new USI_KICKO();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_KICKO(boolean ignored) {
		super(opId);
		usage = Usage.PROGNAME;
	}

	/** Default constructor; does nothing. */
	private USI_KICKO() {}
}

