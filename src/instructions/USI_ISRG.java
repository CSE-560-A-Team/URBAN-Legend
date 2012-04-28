package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.OperandChecker.isValidNumWords;
import static assemblernator.OperandChecker.isValidReg;
import static assemblernator.OperandChecker.isValidIndex;
import static assemblernator.OperandChecker.isValidMem;
import static assemblernator.OperandChecker.isValidLiteral;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The ISRG instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IA3
 */
public class USI_ISRG extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "ISRG";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x0000000B; // 0b00101100000000000000000000000000

	/** The static instance for this instruction. */
	static USI_ISRG staticInstance = new USI_ISRG(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}

	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		int value;
		
		//check for operand combos.
		if(this.hasOperand("NW")) {
			if(this.operands.size() == 4) {
				if(!(this.hasOperand("FM") && this.hasOperand("FX") && this.hasOperand("DX"))) {
					isValid = false;
					hErr.reportError(makeError("instructionMissingOp3", this.getOpId(), "FM", "FX", "DX"), this.lineNum, -1);
				}
			} else if(this.operands.size() == 3) {
				if(!(this.hasOperand("DR") || this.hasOperand("DX"))) {
					isValid = false;
					hErr.reportError(makeError("instructionMissingOp2", this.getOpId(), "DR", "DX"), this.lineNum, -1);
				} else if(!(this.hasOperand("FM") || this.hasOperand("FL"))) {
					isValid = false;
					hErr.reportError(makeError("instructionMissingOp2", this.getOpId(), "FM", "FL"), this.lineNum, -1);
				}
			} else {
				isValid = false;
				if(this.operands.size() > 4) {
					hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
				} else {
					hErr.reportError(makeError("tooFewOperandsIns", this.getOpId()), this.lineNum, -1);
				}
			}
		} else {
			isValid = false;
			hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "NW"), this.lineNum, -1);
		}
		
		//check for operand values.
		if(isValid) {
			value = module.evaluate(this.getOperand("NW"), false, hErr, this, this.getOperandData("NW").keywordStartPosition);
			isValid = isValidNumWords(value);
			if(!isValid) hErr.reportError(makeError("OORnw", this.getOpId()), this.lineNum, -1);
			this.getOperandData("NW").value = value;
			
			if(this.hasOperand("FM") && isValid) {
				value = module.evaluate(this.getOperand("FM"), true, hErr, this, this.getOperandData("FM").keywordStartPosition);
				isValid = isValidMem(value);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "FM", this.getOpId()), this.lineNum, -1);
				this.getOperandData("FM").value = value;
			} else if(this.hasOperand("FL")) {
				value = module.evaluate(this.getOperand("FL"), false, hErr, this, this.getOperandData("FL").keywordStartPosition);
				isValid = isValidLiteral(value, ConstantRange.RANGE_ADDR);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "FL", this.getOpId()), this.lineNum, -1);
				this.getOperandData("FL").value = value;
			}
			
			if(this.hasOperand("FX") && isValid) {
				value = module.evaluate(this.getOperand("FX"), false, hErr, this, this.getOperandData("FX").keywordStartPosition);
				isValid = isValidIndex(value);
				if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
				this.getOperandData("FX").value = value;
			}
			
			if(this.hasOperand("DR") && isValid) {
				value = module.evaluate(this.getOperand("DR"), false, hErr, this, this.getOperandData("DR").keywordStartPosition);
				isValid = isValidReg(value);
				if(!isValid) hErr.reportError(makeError("OORarithReg", "DR", this.getOpId()), this.lineNum, -1);
				this.getOperandData("DR").value = value;
			} else if(this.hasOperand("DX")) {
				value = module.evaluate(this.getOperand("DX"), false, hErr, this, this.getOperandData("DX").keywordStartPosition);
				isValid = isValidIndex(value);
				if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
				this.getOperandData("DX").value = value;
			}
		}
		
		return isValid; 
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
		return new USI_ISRG();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_ISRG(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_ISRG() {}
}

