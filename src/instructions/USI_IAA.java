package instructions;

import static simulanator.Deformatter.breakDownOther;
import simulanator.Machine;
import simulanator.Deformatter.OpcodeBreakdownOther;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The IAA instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IA2
 */
public class USI_IAA extends UIG_Arithmetic {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "IAA";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x0A; // 0b001010

	/** The static instance for this instruction. */
	static USI_IAA staticInstance = new USI_IAA(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}


	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {
		OpcodeBreakdownOther brkDwn = breakDownOther(instruction);
		int srcValue = brkDwn.readFromSource(machine);
		int abs = Math.abs(srcValue);
		int destValue = brkDwn.readFromDest(machine);
		brkDwn.putToDest(abs+destValue, machine);
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
		return new USI_IAA();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_IAA(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_IAA() {}
}

