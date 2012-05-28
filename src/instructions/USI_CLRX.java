package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.InstructionFormatter.formatOPOnly;
import static simulanator.Deformatter.breakDownOther;
import simulanator.Deformatter;
import simulanator.Machine;
import simulanator.Deformatter.OpcodeBreakdown;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The CLRX instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IS2
 */
public class USI_CLRX extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "CLRX";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x12; // 0b010010

	/** The static instance for this instruction. */
	static USI_CLRX staticInstance = new USI_CLRX(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}

	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		if(this.operands.size() > 0) {
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
			return false;
		} else {
			return true;
		}
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return formatOPOnly(this);
	}

	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {

		OpcodeBreakdown brkdwn = Deformatter.breakDownOther(machine.instruction);
		int word = 0;
		for(int i = 0; i<7 ; i++){ 
			machine.setIndexRegister(i, word);
		}	
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
		return new USI_CLRX();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_CLRX(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_CLRX() {}
}

