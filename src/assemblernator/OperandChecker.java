package assemblernator;

import assemblernator.ErrorReporting.ErrorHandler;

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
	public static boolean isValidIndex(String exp) {
		boolean valid = true;
		try {
			int indexReg = Integer.parseInt(exp);

			if (indexReg > 7 || indexReg < 1) {
				valid = false;
			}
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}

	/**
	 * Checks if the FC in Shift/Manipulate functions is within the range [0-31] or not.
	 * 
	 * @author Ratul Khosla
	 * @date Apr 15, 2012; 6:08:32 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param exp 
	 * 			value of the FC register
	 * @return check
	 * @specRef N/A
	 */
	public static boolean isValidShiftConstant(String exp){
		boolean check = true;
		try {
			int fcreg = Integer.parseInt(exp);

			if ( fcreg < 0  || fcreg > 31 ) {
				check = false;
			}
		} catch (NumberFormatException e) {
			check = false;
		}
		return check;
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
	public static boolean isValidNumWords(String exp) {
		boolean valid = true;

		try {
			int nw = Integer.parseInt(exp);

			if (nw > 15 || nw < 0) {
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
	 * @date Apr 14, 2012; 5:10:38 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param lit
	 * @return
	 * @specRef N/A
	 */
	public static boolean isValidLiteral(String lit) {
		return true;
	}

	/**
	 * 
	 * @author Noah
	 * @date Apr 14, 2012; 5:10:41 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param cnst
	 * @return
	 * @specRef N/A
	 */
	public static boolean isValidConstant(String cnst) {
		return true;
	}

	public static boolean isValidExpression(String exp) {
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
	public static boolean isValidMem(String exp) {
		boolean valid = true;
		
		try{
			int addr  = Integer.parseInt(exp);
			
			if(addr > 4095 || addr < 0) {
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
	 * @date Apr 14, 2012; 5:10:47 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param addr
	 * @return
	 * @specRef N/A
	 */
	public static boolean isValidMem(int addr) {
		return true;
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
		if (str.length() <= 0)
			return false;
		if (str.charAt(0) != '\'')
			return false;
		for (int p = 1; p < str.length(); ++p)
			if (str.charAt(p) == '\\')
				++p;
			else if (str.charAt(p) == '\'')
				return p + 1 >= str.length();
		return false;
	}
}
