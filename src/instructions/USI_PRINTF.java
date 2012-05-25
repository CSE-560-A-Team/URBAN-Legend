package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.Module.Value.BitLocation.Address;
import static assemblernator.Module.Value.BitLocation.Other;
import static assemblernator.OperandChecker.isValidMem;
import static assemblernator.OperandChecker.isValidNumWords;
import simulanator.Deformatter;
import simulanator.Deformatter.OpcodeBreakdown;
import simulanator.Machine;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.InstructionFormatter;
import assemblernator.Module;
import assemblernator.Module.Value;

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
	/** The address to read from instead */
	int addr = -1;
	/** Number of words, or -1. */
	int numWords = -1;

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return givnStr == null ? lc + 1 : lc + 1
				+ USI_CHAR.padWord(givnStr.getBytes().length);
	}

	/**
	 * @see assemblernator.AbstractInstruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler,
	 *      assemblernator.Module)
	 */
	@Override public boolean immediateCheck(ErrorHandler hErr, Module module) {
		Operand st = getOperandData("ST");
		if (st != null) {
			givnStr = IOFormat.escapeString(st.expression, lineNum,
					st.valueStartPosition, hErr);
			System.out.println(givnStr);
			st.value = new Value(0, 'A');
			if (operands.size() != 1)
				hErr.reportWarning(makeError("extraParamsIgF", opId, "ST"),
						lineNum, 0);
			return true;
		}
		if (!hasOperand("FM")) {
			hErr.reportError(
					makeError("instructionMissingOp", this.getOpId(), "FM"),
					this.lineNum, -1);
			return false;
		}

		Operand nw = getOperandData("NW");
		if (nw != null) {
			Value value = module.evaluate(nw.expression, false, Other, hErr,
					this, this.getOperandData("NW").valueStartPosition);
			if (!isValidNumWords(value.value)) {
				hErr.reportError(makeError("OORnw", this.getOpId()),
						this.lineNum, -1);
				return false;
			}
			nw.value = value;
			numWords = nw.value.value;
		}

		return true;
	}

	/**
	 * @see assemblernator.Instruction#check(assemblernator.ErrorReporting.ErrorHandler,
	 *      assemblernator.Module)
	 */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		if (givnStr == null) {
			Operand dm = getOperandData("DM");
			Value v = module.evaluate(dm.expression, true, Address, hErr, this,
					this.getOperandData("DM").valueStartPosition);
			if (!isValidMem(v.value)) {
				hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()),
						this.lineNum, -1);
				return false;
			}
			addr = v.value;
		}
		else {
			int nwa = USI_CHAR.padWord(givnStr.getBytes().length);
			Operand fnw = new Operand("NW", "" + nwa, 0, 0);
			fnw.value = new Value(nwa, 'A');
			operands.add(fnw);

			Operand ffl = new Operand("FL", Integer.toString(lc + 1), 0, 0);
			ffl.value = new Value(lc + 1, 'R');
			operands.add(ffl);
		}
		return true;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		int[] assembly;
		if (givnStr != null) {
			int[] subassembly = USI_CHAR.strToIntArray(givnStr.getBytes());
			assembly = new int[subassembly.length + 1];
			assembly[0] = InstructionFormatter.formatSrcRange(this)[0];
			for (int i = 0; i < subassembly.length; ++i)
				assembly[i + 1] = subassembly[i];
		}
		else {
			assembly = new int[1];
			assembly[0] = InstructionFormatter.formatSrcRange(this)[0];
		}
		return assembly;
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
		StringBuilder raw = new StringBuilder(new String(b));
		for (int i = 0; i < raw.length() - 1; ++i) {
			if (raw.charAt(i) == '%') {
				char c = raw.charAt(i + 1);
				if (c == '%')
					raw.delete(i, i + 1);
				else if (Character.isDigit(c) && Character.digit(c, 10) < 8)
					raw.replace(i, i + 2,
							"" + machine.getRegister(Character.digit(c, 10)));
			}
			else if (raw.charAt(i) == '$') {
				char c = raw.charAt(i + 1);
				if (c == '$')
					raw.delete(i, i + 1);
				else if (Character.isDigit(c) && Character.digit(c, 10) < 8)
					raw.replace(i, i + 2, Integer.toString(machine
							.getIndexRegister(Character.digit(c, 10))));
			}
		}
		machine.output.putString(raw.toString());
		if (breakDown.literal)
			machine.setLC(machine.getLC() + nw);
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
