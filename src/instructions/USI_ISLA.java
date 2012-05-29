package instructions;

import static assemblernator.ErrorReporting.makeError;
import static simulanator.Deformatter.breakDownOther;
import simulanator.Deformatter.OpcodeBreakdown;
import simulanator.Machine;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The ISLA instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IM3
 */
public class USI_ISLA extends UIG_ShiftManipulate {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "ISLA";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x1B; // 0b011011

	/** The static instance for this instruction. */
	static USI_ISLA staticInstance = new USI_ISLA(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}


	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {
		
		OpcodeBreakdown brkdwn = breakDownOther(machine.instruction);
		int wordOrig = brkdwn.readFromDest(machine);
		int srcword = brkdwn.readFromSource(machine);
		if(srcword >=0 || srcword <= 31) {
			wordOrig <<= srcword; //left shift and assign.			
		} else {
			machine.hErr.reportError(makeError("runOverShift"), machine.getLC(), -1);
			srcword = 0;
		}
		brkdwn.putToDest(wordOrig, machine);
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
		return new USI_ISLA();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_ISLA(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_ISLA() {}
}

