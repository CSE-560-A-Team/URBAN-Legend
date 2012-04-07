package instructions;

import assemblernator.Instruction;

/**
 * The NOP instruction.
 * 
 * @author Generate.java
 * @date Apr 07, 2012; 04:41:01
 * @specRef C0
 */
public class USI_NOP extends Instruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "NOP";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x0000003D; // 0b11110100000000000000000000000000

	/** The static instance for this instruction. */
	static USI_NOP staticInstance = new USI_NOP(true);

	/** @see assemblernator.Instruction#getWordCount() */
	@Override public int getWordCount() {
		return 1;
	}

	/** @see assemblernator.Instruction#check() */
	@Override public boolean check() {
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
		return new USI_NOP();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_NOP(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_NOP() {}
}
