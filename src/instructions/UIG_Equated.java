package instructions;

import assemblernator.AbstractDirective;
import assemblernator.Module;

/**
 * Parent class for equated types, introducing a constant value member.
 * 
 * @author Josh Ventura
 * @date Apr 16, 2012; 11:28:07 PM
 */
public abstract class UIG_Equated extends AbstractDirective {
	/** The equated value of this instruction. */
	public int value;

	/**
	 * Equated directives are basically macros, and do not affect the LC.
	 * 
	 * @see assemblernator.Instruction#getNewLC(int, assemblernator.Module)
	 */
	@Override public final int getNewLC(int lc, Module mod) {
		return lc;
	}


	/**
	 * Equated directives are basically macros, and have no presence in memory.
	 * 
	 * @see assemblernator.Instruction#assemble()
	 */
	@Override public final int[] assemble() {
		return new int[0];
	}

	/**
	 * Construct new.
	 * 
	 * @param opid
	 *            The operand ID to pass to AbstractDirective().
	 */
	protected UIG_Equated(String opid) {
		super(opid);
	}

	/** Default constructor. */
	protected UIG_Equated() {}
}
