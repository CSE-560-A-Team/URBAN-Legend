package assemblernator;

import assemblernator.ErrorReporting.ErrorHandler;

/**
 * A class distinguishing instructions from directives.
 * 
 * @author Josh Ventura
 * @date Apr 16, 2012; 8:21:45 PM
 */
public abstract class AbstractInstruction extends Instruction {
	/**
	 * @param opid The name of this instruction to pass to Instruction(), such as "MOVD".
	 * @param opcode The opcode of this instruction to pass to Instruction().
	 */
	protected AbstractInstruction(String opid, int opcode) {
		super(opid,opcode);
	}
	
	/**
	 * Default constructor.
	 */
	protected AbstractInstruction() {
	}

	/**
	 * Most instructions should be checked for validity during the second pass.
	 * 
	 * @see assemblernator.Instruction#immediateCheck(assemblernator.ErrorReporting.ErrorHandler)
	 */
	@Override public boolean immediateCheck(ErrorHandler hErr) {
		return true;
	}

	/**
	 * Always returns false.
	 * 
	 * @see assemblernator.Instruction#isDirective()
	 */
	@Override public final boolean isDirective() {
		return false;
	}
}
