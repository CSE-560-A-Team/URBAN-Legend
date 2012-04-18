package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction.ConstantRange;
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.Module;
import static assemblernator.OperandChecker.isValidConstant;

/**
 * The HLT instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef C2
 */
public class USI_HLT extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "HLT";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x0000003F; // 0b11111100000000000000000000000000

	/** The static instance for this instruction. */
	static USI_HLT staticInstance = new USI_HLT(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}

	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		if(!this.hasOperand("FC") || this.operands.size() > 1) {
			hErr.reportError(this.getOpId() + " should have exactly one operand: \"FC\"", this.lineNum, -1);
			isValid = false;
		} else {
			int value = module.evaluate(this.getOperand("FC"), false, hErr, this, this.getOperandData("FC").keywordStartPosition); 
			if(!isValidConstant(value, ConstantRange.RANGE_13_TC)) {
				hErr.reportError(makeError("OORconstant", "FC", this.getOpId(), 
						Integer.toString(ConstantRange.RANGE_13_TC.min), Integer.toString(ConstantRange.RANGE_13_TC.max)), this.lineNum, -1);
				isValid = false;
			}
			this.operands.get(0).value = value;
		}
		
		return isValid;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		int[] assembled = new int[1];
		String code = IOFormat.formatBinInteger(this.getOpcode(), 6); //"111111"
		code = code + "0000000000000"; //13 unused bits.  "111111 0000000000000"
		//13 bits of constant in memory.  "111111 000000000000 0000000011111"
		code = code + IOFormat.formatBinInteger(Integer.parseInt(this.getOperand("FC")), 13); 
		
		assembled[0] = Integer.parseInt(code);
		
		return assembled;
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
		return new USI_HLT();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_HLT(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_HLT() {}
}

