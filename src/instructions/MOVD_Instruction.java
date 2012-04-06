package instructions;

import assemblernator.Instruction;

/**
 * The MOVD instruction.
 * 
 * @author Josh Ventura
 * @date Apr 6, 2012; 6:15:36 PM
 */
public class MOVD_Instruction extends Instruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "MOVD";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0; // 0b000000

	/** The static instance for this instruction. */
	static MOVD_Instruction staticInstance = new MOVD_Instruction(true);

	/** @see assemblernator.Instruction#getWordCount() */
	@Override
	public int getWordCount() {
		return 1;
	}

	/** @see assemblernator.Instruction#check() */
	@Override
	public boolean check() {
		return false; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override
	public int[] assemble() {
		return null; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#execute(int) */
	@Override
	public void execute(int instruction) {
		// TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#getNewInstance() */
	@Override
	public Instruction getNewInstance() {
		return new MOVD_Instruction();
	}

	// =========================================================
	// === Redundant code ======================================
	// =========================================================
	// === This code's the same in all instruction classes, ====
	// === But Java lacks the mechanism to allow stuffing it ===
	// === in super() where it belongs =========================
	// =========================================================

	/**
	 * @see Instruction
	 * @return The static instance of this instruction.
	 */
	public static Instruction getInstance() {
		return staticInstance;
	}

	/** @see assemblernator.Instruction#getOpId() */
	@Override
	public String getOpId() {
		return opId;
	}

	/** @see assemblernator.Instruction#getOpcode() */
	@Override
	public int getOpcode() {
		return opCode;
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private MOVD_Instruction(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private MOVD_Instruction() {
	}
}
