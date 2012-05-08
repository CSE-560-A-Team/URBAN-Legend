package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.InstructionFormatter.formatOther;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.Module.Value;
import assemblernator.Module.Value.BitLocation;
import assemblernator.OperandChecker;

/**
 * The CLR instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IS0
 */
public class USI_CLR extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "CLR";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x00000010; // 0b01000000000000000000000000000000

	/** The static instance for this instruction. */
	static USI_CLR staticInstance = new USI_CLR(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}

	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		Value value;
		if(this.operands.size() > 1) {
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
			isValid =  false;
		} else if(this.operands.size() < 1) {
			hErr.reportError(makeError("tooFewOperands", this.getOpId()), this.lineNum, -1);
			isValid =  false;
		}
		else if(this.hasOperand("DR")) {
			value = module.evaluate(this.getOperand("DR"), false,BitLocation.Other , hErr, this, this.getOperandData("DR").keywordStartPosition);
			//range checking
			isValid = OperandChecker.isValidReg(value.value);
			if(!isValid) hErr.reportError(makeError("OORidxReg", "DR", this.getOpId()), this.lineNum, -1);
			this.getOperandData("DR").value = value;
		} else if(this.hasOperand("DX")){
			//range checking
			value = module.evaluate(this.getOperand("DX"), false,BitLocation.Other , hErr, this, this.getOperandData("DX").keywordStartPosition);
			isValid = OperandChecker.isValidIndex(value.value);
			if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
			this.getOperandData("DX").value = value;
		} else{
			isValid = false;
			if(this.hasOperand("FR")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FR"), this.lineNum, -1);
			}  else if(this.hasOperand("DM")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "DM"), this.lineNum, -1);		
			} else if(this.hasOperand("FC")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FC"), this.lineNum, -1);			
			}  else if(this.hasOperand("FL")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FL"), this.lineNum, -1);			
			} else if(this.hasOperand("FS")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FS"), this.lineNum, -1);		
			} else if(this.hasOperand("LR")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "LR"), this.lineNum, -1);			
			} else if(this.hasOperand("FM")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FM"), this.lineNum, -1);			
			} else if(this.hasOperand("EX")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "EX"), this.lineNum, -1);		
			} else if(this.hasOperand("NW")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "NW"), this.lineNum, -1);			
			} else if(this.hasOperand("ST")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "ST"), this.lineNum, -1);			
			}
		}
			return isValid;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return formatOther(this);
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
		return new USI_CLR();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_CLR(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_CLR() {}
}

