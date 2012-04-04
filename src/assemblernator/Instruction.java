package assemblernator;

/**
 * @author Josh Ventura
 * @date Apr 4, 2012 1:39:03 AM
 */
public class Instruction {
	String id;
	int opcode;

	/**
	 * Parse a string containing the operands of an instruction, storing the
	 * operands locally.
	 * 
	 * @author Josh Ventura
	 * @date Apr 4, 2012; 1:40:21 AM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param operands
	 *            The string of parameters to parse.
	 */
	public final void parse(String operands) {
		// TODO: IMPLEMENT
	}

	/**
	 * Check if the token is semantically correct. This means that it has the
	 * correct number of operands and the correct kinds of operands.
	 * 
	 * @author Josh Ventura
	 * @return Returns whether the instruction is semantically correct.
	 * @date Apr 4, 2012; 01:40:29AM
	 */
	public boolean check() {
		throw new NullPointerException("No method");
	}

	/**
	 * Assemble this instruction after it has been checked.
	 * 
	 * @author Josh Ventura
	 * @date Apr 4, 2012; 1:40:52 AM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @return Returns an integer containing the instruction's byte code.
	 */
	public int assemble() {
		throw new NullPointerException("No method");
	}

	/**
	 * @author Josh Ventura
	 * @date Apr 4, 2012; 9:11:51 AM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 */
	public void execute() {
		throw new NullPointerException("No method");
	}
}
