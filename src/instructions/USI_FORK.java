package instructions;

import static simulanator.Deformatter.breakDownOther;
import simulanator.Deformatter.OpcodeBreakdown;
import simulanator.Machine;
import assemblernator.Instruction;

/**
 * The TR instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef JT3
 */
public class USI_FORK extends USI_TR {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "FORK";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x3C; // 0b111100

	/** The static instance for this instruction. */
	static USI_FORK staticInstance = new USI_FORK(true);

	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {
		OpcodeBreakdown brkDwn = breakDownOther(instruction);
		int addr = brkDwn.getEffectiveDestAddress(machine);
		Machine fork = new Machine(machine, addr);
		fork.runThread(addr);
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
		return new USI_FORK();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_FORK(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_FORK() {}
}
