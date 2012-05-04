package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.InstructionFormatter.formatHaltDump;
import static assemblernator.OperandChecker.isValidConstant;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The DMP instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef C1
 */
public class USI_DMP extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "DMP";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x0000003E; // 0b11111000000000000000000000000000

	/** The static instance for this instruction. */
	static USI_DMP staticInstance = new USI_DMP(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override 
	public int getNewLC(int lc, Module mod) {
		return lc+1;
	}

	/** @see assemblernator.Instruction#check(ErrorHandler, Module) 
	 *  @modified Apr 18, 2012; 11:38:12 AM Changed error type generated.*/
	@Override 
	public boolean check(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		if((!(this.hasOperand("FC") || this.hasOperand("EX"))) || this.operands.size() > 1) {
			hErr.reportError(makeError("instructionMissingOp2", this.getOpId(), "FC", "EX"), this.lineNum, -1);
			isValid = false;
		} else {
			int value;
			String errOperand;
			if(this.hasOperand("FC")) {
				value = module.evaluate(this.getOperand("FC"), false, hErr, this, this.getOperandData("FC").keywordStartPosition);
				errOperand = "FC";
			} else {
				value = module.evaluate(this.getOperand("EX"), true, hErr, this, this.getOperandData("EX").keywordStartPosition);
				errOperand = "EX";
			}

			if(!isValidConstant(value, ConstantRange.RANGE_DMP)) {
				hErr.reportError(makeError("OORconstant", errOperand, this.getOpId(), 
						Integer.toString(ConstantRange.RANGE_DMP.min), Integer.toString(ConstantRange.RANGE_DMP.max)), this.lineNum, -1);
				isValid = false;
			}
			this.operands.get(0).value = value;
		}
		
		return isValid;
	}

	/** @see assemblernator.Instruction#assemble() 
	 *  @modified Apr 27, 2012; 7:59:50 use InstructionFormatter.
	 */
	@Override public int[] assemble() {
		return formatHaltDump(this);
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
		return new USI_DMP();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_DMP(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_DMP() {}
}

