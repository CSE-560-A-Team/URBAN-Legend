package instructions;

import assemblernator.Assembler;
import assemblernator.Instruction;

/**
 * The MOVD instruction.
 * 
 * @author Josh Ventura
 * @date Apr 6, 2012; 12:17:38 AM
 * @specRef MV0
 */
public class MOVD_Instruction extends Instruction {
	/** The operation identifier of this instruction */
	private static final String opId = "MOVD";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0; // 0b000000

	/** The static instance for this instruction. */
	static MOVD_Instruction staticInstance;
	static {
		staticInstance = new MOVD_Instruction(opId);
	}

	/**
	 * @see Instruction
	 * @return The static instance of this instruction.
	 */
	public static Instruction getInstance() {
		return staticInstance;
	}

	/** @see assemblernator.Instruction#getNewInstance() */
	@Override
	public Instruction getNewInstance() {
		return new MOVD_Instruction();
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

	/** @see assemblernator.Instruction#getWordCount() */
	@Override
	public int getWordCount() {
		return 1;
	}

	/** @see assemblernator.Instruction#check() */
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return false;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override
	public int assemble() {
		// TODO Auto-generated method stub
		return 0;
	}

	/** @see assemblernator.Instruction#execute(int) */
	@Override
	public void execute(int instruction) {
		// TODO Auto-generated method stub
	}

	/**
	 * Add our static instance to Assembler's list of instructions.
	 * 
	 * @param ignored
	 *            Unused parameter; the internal copy is used instead.
	 */
	private MOVD_Instruction(String ignored) {
		System.out.println("Registered `" + opId + "' instruction");
		Assembler.instructions.put(opId, this);
	}

	/** Default constructor; does nothing. */
	public MOVD_Instruction() {
	}
}
