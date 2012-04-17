package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.OperandChecker;

/**
 * The PST instruction.
 * 
 * @author Ratul Khosla
 * @date Apr 08, 2012; 08:26:19
 * @specRef S2
 */
public class USI_PST extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "PST";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x00000032; // 0b11001000000000000000000000000000

	/** The static instance for this instruction. */
	static USI_PST staticInstance = new USI_PST(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}


	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
	
		boolean isValid = true;
		if(this.operands.size() > 1 || this.operands.size() == 0) {
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
			isValid =  false;
		} else if(this.hasOperand("DR")) {
			//range checking
			isValid = OperandChecker.isValidReg(this.getOperand("DR"));
			isValid = OperandChecker.isValidMem(this.getOperand("DR"));
			if(!isValid) hErr.reportError(makeError("OORidxReg", "DR", this.getOpId()), this.lineNum, -1);
		}  else if(this.hasOperand("FM")) {
			//range checking
			isValid = OperandChecker.isValidMem(this.getOperand("FM"));
			hErr.reportError(makeError("operandInsWrong", "FM", this.getOpId()), this.lineNum, -1);
			if(this.hasOperand("FX")) {
				//range checking - FX only possible if FM appears before it.
				isValid = OperandChecker.isValidIndex(this.getOperand("FX"));
				isValid = OperandChecker.isValidIndex(this.getOperand("FX"));
				if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
			}
		}  else if(this.hasOperand("FC")) {
			//range checking
			isValid = OperandChecker.isValidMem(this.getOperand("FC"));
		}  else if(this.hasOperand("FL")){
			//range checking
			isValid = OperandChecker.isValidMem(this.getOperand("FL"));
			if(!isValid) hErr.reportError(makeError("OOR13tc", "FL", this.getOpId()), this.lineNum, -1);
		} else{
			isValid = false;
			hErr.reportError(makeError("operandInsWrong", "EX", this.getOpId()), this.lineNum, -1);
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
		return new USI_PST();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_PST(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_PST() {}


}

