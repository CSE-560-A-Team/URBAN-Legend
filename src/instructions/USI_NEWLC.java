package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.AbstractDirective;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.Module.Value;

/**
 * The NEWLC instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef D2
 */
public class USI_NEWLC extends AbstractDirective {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "NEWLC";

	/** The static instance for this instruction. */
	static USI_NEWLC staticInstance = new USI_NEWLC(true);

	/** The LC the user specified. */
	int ownLC;

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		this.lc = ownLC;
		return ownLC;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return new int[0];
	}

	/**
	 * @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler,
	 *      Module)
	 */
	@Override public boolean immediateCheck(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		Operand o = this.getOperandData("FC");
		if (o == null) {
			o = this.getOperandData("LR");
			if (o == null) {
				o = this.getOperandData("FL");
				if (o == null) {
					hErr.reportError(
							makeError("directiveMissingOp2", this.getOpId(),
									"FC", "LR"), lineNum, -1);
					isValid = false;
				}
			}
		}

		Value tempv = module.evaluate(o.expression, false, hErr, this,
				o.valueStartPosition);
		if (tempv.arec != 'A')
			hErr.reportError(makeError("nonConstExpr", o.expression), lineNum,
					o.valueStartPosition);
		ownLC = tempv.value;

		if (isValid && ownLC < lc) {
			hErr.reportError(makeError("lcBackward"), lineNum, -1);
			isValid = false;
		}
		return isValid;
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

	/** @see assemblernator.Instruction#getNewInstance() */
	@Override public Instruction getNewInstance() {
		return new USI_NEWLC();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_NEWLC(boolean ignored) {
		super(opId);
	}

	/** Default constructor; does nothing. */
	private USI_NEWLC() {}
}
