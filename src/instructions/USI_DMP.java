package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.OperandChecker;

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

	/** @see assemblernator.Instruction#check(ErrorHandler) */
	@Override 
	public boolean check(ErrorHandler hErr) {
		boolean isValid = true;
		
		if(!this.hasOperand("FC") || this.operands.size() > 1) {
			hErr.reportError(this.getOpId() + " should have exactly one operand: \"FC\"", this.lineNum, -1);
			isValid = false;
		} else if(!OperandChecker.isValidConstant(this.getOperand("FC"))) {
			hErr.reportError(makeError("OOR13tc", "FC", this.getOpId()), this.lineNum, -1);
			isValid = false;
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

