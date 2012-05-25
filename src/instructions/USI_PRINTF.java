package instructions;

import static assemblernator.ErrorReporting.makeError;
import simulanator.Deformatter;
import simulanator.Deformatter.OpcodeBreakdown;
import simulanator.Machine;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction.Operand;
import assemblernator.Module.Value;
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.Module;

/**
 * The CWSR instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IO2
 */
public class USI_PRINTF extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "PRINTF";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x38; // 0b111000

	/** The static instance for this instruction. */
	static USI_PRINTF staticInstance = new USI_PRINTF(true);

	/** The value given to ST, or null. */
	private String givnStr = null;

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return givnStr == null ? lc + 1 : USI_CHAR
				.padWord(givnStr.getBytes().length);
	}
	
	/** @see assemblernator.AbstractInstruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler, assemblernator.Module) */
	@Override public boolean immediateCheck(ErrorHandler hErr, Module module) {
		Operand st = getOperandData("ST");
		if (st != null) {
			givnStr = IOFormat.escapeString(st.expression, lineNum,
					st.valueStartPosition, hErr);
			st.value = new Value(0, 'A');
			if (operands.size() != 1)
				hErr.reportWarning(makeError("extraParamsIgF", opId, "ST"),
						lineNum, 0);
			return true;
		}
		if (!hasOperand("FM")) {
			//hErr.reportError(err, line, pos)
		}
		return true;
	}
	
	/** @see assemblernator.Instruction#check(assemblernator.ErrorReporting.ErrorHandler, assemblernator.Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		return true;
	}

	@Override public int[] assemble() {
		// TODO Auto-generated method stub
		return null;
	}

	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {
		OpcodeBreakdown breakDown = Deformatter.breakDownSrcRange(instruction);
		int addr = breakDown.getEffectiveSrcAddress(machine);
		int nw = breakDown.numWords;
		byte[] b = new byte[nw << 2];
		for (int i = 0; i < nw; ++i) {
			int a = machine.getMemory(addr + i);
			b[(i << 2) + 0] = ((byte) ((a & 0xFF000000) >>> 24));
			b[(i << 2) + 1] = ((byte) ((a & 0x00FF0000) >>> 16));
			b[(i << 2) + 2] = ((byte) ((a & 0x0000FF00) >>> 8));
			b[(i << 2) + 3] = ((byte) ((a & 0x000000FF) >>> 0));
		}
		machine.output.putString(new String(b));
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
		return new USI_PRINTF();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_PRINTF(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_PRINTF() {}
}
