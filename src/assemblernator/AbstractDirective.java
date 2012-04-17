package assemblernator;

import assemblernator.ErrorReporting.ErrorHandler;

/**
 * A class distinguishing directives from instructions.
 * 
 * @author Josh Ventura
 * @date Apr 16, 2012; 8:33:20 PM
 */
public abstract class AbstractDirective extends Instruction {
	/**
	 * Construct new.
	 * 
	 * @param opid
	 *            The operand ID to pass to Instruction().
	 */
	protected AbstractDirective(String opid) {
		super(opid, -1);
	}

	/** Default constructor. */
	protected AbstractDirective() {}

	/**
	 * Directives have no opcode.
	 * 
	 * @see assemblernator.Instruction#getOpcode()
	 */
	@Override public final int getOpcode() {
		return -1;
	}

	/**
	 * Most directives have their checking out of the way in pass one.
	 * 
	 * @see assemblernator.Instruction#check(assemblernator.ErrorReporting.ErrorHandler, Module)
	 */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		return true;
	}

	/**
	 * Always returns true.
	 * 
	 * @see assemblernator.Instruction#isDirective()
	 */
	@Override public final boolean isDirective() {
		return true;
	}

	/**
	 * Throws an error message. No directives should be executed at runtime.
	 * 
	 * @see assemblernator.Instruction#execute(int)
	 */
	@Override public final void execute(int opcode) {
		System.err.println("Attempt to invoke directive `" + getOpId()
				+ "' for execute!");
	}
}
