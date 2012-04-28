package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.OperandChecker.isValidIndex;
import static assemblernator.OperandChecker.isValidConstant;
import static assemblernator.OperandChecker.isValidReg;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The PWR instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IA7
 */
public class USI_PWR extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "PWR";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x0000000F; // 0b00111100000000000000000000000000

	/** The static instance for this instruction. */
	static USI_PWR staticInstance = new USI_PWR(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}

	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override 
	public boolean check(ErrorHandler hErr, Module module) {
		int value;
		boolean isValid;
		if(this.operands.size() == 2) {
			if(this.hasOperand("FC")) {
				value = module.evaluate(this.getOperand("FC"), false, hErr, this, this.getOperandData("FC").keywordStartPosition);
				isValid = isValidConstant(value, ConstantRange.RANGE_ADDR);
				if(!isValid) hErr.reportError(makeError("OORconstant", "FL", this.getOpId(), 
						Integer.toString(ConstantRange.RANGE_13_TC.min), Integer.toString(ConstantRange.RANGE_13_TC.max)), this.lineNum, -1);
				this.getOperandData("FC").value = value;
				
				if(isValid) {
					if(this.hasOperand("DR")) {
						value = module.evaluate(this.getOperand("DR"), false, hErr, this, this.getOperandData("DR").keywordStartPosition);
						isValid = isValidReg(value);
						if(!isValid) hErr.reportError(makeError("OORarithReg", "DR", this.getOpId()), this.lineNum, -1);
						this.getOperandData("DR").value = value;
					} else if(this.hasOperand("DX")) {
						value = module.evaluate(this.getOperand("DX"), false, hErr, this, this.getOperandData("DX").keywordStartPosition); 
						isValid = isValidIndex(value);
						if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
						this.getOperandData("DX").value = value;
					} else {
						isValid = false;
						hErr.reportError(makeError("instructionMissingOp2", this.getOpId(), "DR", "DX"), this.lineNum, -1);
					}
				}
			} else {
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "FC"), this.lineNum, -1);
			}
		} else if(this.operands.size() > 2) {
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
		} else {
			hErr.reportError(makeError("tooFewOperandsIns", this.getOpId()), this.lineNum, -1);
		}
		
		return false; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return null; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#execute(int) */
	@Override public void execute(int instruction) {
		// TODO: IMPLEMENT
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
		return new USI_PWR();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_PWR(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_PWR() {}
}

