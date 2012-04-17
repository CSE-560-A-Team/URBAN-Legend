package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.AbstractDirective;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.OperandChecker;

/**
 * The SKIPS instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef D9
 */
public class USI_SKIPS extends AbstractDirective {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "SKIPS";

	/** The static instance for this instruction. */
	static USI_SKIPS staticInstance = new USI_SKIPS(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+mod.evaluate(getOperand("FC"));
	}
	
	String src = "";
	
	/** @see assemblernator.Instruction#check(ErrorHandler) */
	@Override public boolean check(ErrorHandler hErr) {
		boolean isValid = true;
		//less than 1 operand error
		if(this.operands.size() < 1){
			isValid=false;
			hErr.reportError(makeError("directiveMissingOp", this.getOpId(), "FC"), this.lineNum, -1);
			//checks for FC
		}else if (this.operands.size() == 1){
			if(this.hasOperand("FC")){
				src = "FC";
				//range check THIS MAY HAVE TO BE CHANGED
				isValid = OperandChecker.isValidMem(this.getOperand("FC"));
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "FC", this.getOpId()), this.lineNum, -1);
			}else{
				isValid = false;
				hErr.reportError(
						makeError("operandDirWrong", this.getOpId(), "any operand other than FC"),
						this.lineNum, -1);
			}
			//more than 1 operand error
		}else{
			isValid =false;
			hErr.reportError(makeError("extraOperandsDir", this.getOpId()), this.lineNum, -1);
		}
		return isValid; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return null; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler) */
	@Override public boolean immediateCheck(ErrorHandler hErr) {
		// TODO Auto-generated method stub
		return false;
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
		return new USI_SKIPS();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_SKIPS(boolean ignored) {
		super(opId);
	}

	/** Default constructor; does nothing. */
	private USI_SKIPS() {}
}

