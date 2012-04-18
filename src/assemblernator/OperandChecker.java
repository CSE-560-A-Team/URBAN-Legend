package assemblernator;

import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction.ConstantRange;

/**
 * checks operands for correct values.
 * 
 * @author Noah
 * @date Apr 14, 2012; 4:54:44 PM
 */
public class OperandChecker {
	/**
	 * Checks if expression following "DX" or "FX" is a valid expression.
	 * 
	 * @author Noah
	 * @date Apr 14, 2012; 2:33:20 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param exp
	 *            value of index register.
	 * @return <pre>
	 * {@code if exp is not an integer, or if exp is an integer > 7 or < 1, then return false,
	 * else return true.}
	 * </pre>
	 * @specRef N/A
	 */
	public static boolean isValidIndex(int exp) {
		boolean valid = true;
		try {
			if (exp > 7 || exp < 1) {
				valid = false;
			}
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}

	/**
	 * Checks if expression of registers
	 * 
	 * @author ERIC
	 * @date Apr 14, 2012; 6:33:20 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param exp
	 *            value of register.
	 * @return <pre>
	 * {@code if exp is not an integer, or if exp is an integer > 7 or < 0, then return false,
	 * else return true.}
	 * </pre>
	 * @specRef N/A
	 */
	public static boolean isValidReg(int exp) {
		boolean valid = true;
		try {
			if (exp > 7 || exp < 0) {
				valid = false;
			}
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}


	/**
	 * 
	 * @author Noah
	 * @date Apr 14, 2012; 5:10:21 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param exp
	 * @return
	 * @specRef N/A
	 */
	public static boolean isValidNumWords(int exp) {
		boolean valid = true;

		try {
			if (exp > 15 || exp < 0) {
				valid = false;
			}
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}

	/**
	 * Checks whether a literal value falls within range.
	 * @author Noah
	 * @date Apr 14, 2012; 5:10:38 PM
	 * @modified Apr 18, 2012; 12:08 AM: added implementation and new arg.
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param lit the literal value to check.
	 * @param range range of valid constant values.
	 * @return if lit falls within range, then return true, else return false.
	 * @specRef N/A
	 */
	//literal for arithmetic
	public static boolean isValidLiteral(int lit, ConstantRange range) {
		return isValidConstant(lit, range);
	}

	/**
	 * Checks whether a constant value falls within range.
	 * @author Noah
	 * @date Apr 14, 2012; 5:10:41 PM
	 * @modified Apr 17, 2012; 10:44 PM changed to accomodate new ConstantRange enum. - Noah
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param cnst the constant value to check.
	 * @param range range of valid constant values.
	 * @return if cnst falls within range, then return true, else return false.
	 * @specRef N/A
	 */
	public static boolean isValidConstant(int cnst, ConstantRange range) {
		return (cnst <= range.max && cnst >= range.min);
	}

	public static boolean isValidExpression(int exp) {
		return true;
	}
	public static boolean isValidLabel(int exp) {
		return true;
	}


	/**
	 * 
	 * @author Noah
	 * @date Apr 14, 2012; 5:10:44 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param addr
	 * @return
	 * @specRef N/A
	 */
	public static boolean isValidMem(int exp) {
		boolean valid = true;

		try {
			if (exp > 4095 || exp < 0) {
				valid = false;
			}
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}

	/**
	 * Checks that a given parameter comprises precisely one string.
	 * 
	 * @author Josh Ventura
	 * @date Apr 14, 2012; 5:10:51 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param str
	 *            The string parameter to check.
	 * @return Whether this parameter is exactly one string.
	 * @specRef N/A
	 */
	public static boolean isValidString(String str) {
		str = str.trim();
		if (str.length() < 1)
			return false;
		if (str.charAt(0) != '\'')
			return false;
		for (int p = 1; p < str.length(); ++p)
			if (str.charAt(p) == '\\')
				++p;
			else if (str.charAt(p) == '\'') {
				System.out.println("(" + p + " + 1) >= '" + str
						+ "'.length() = " + (p + 1) + " >= " + str.length()
						+ " = " + (p + 1 >= str.length()));
				return p + 1 >= str.length();
			}
		return false;
	}
}
