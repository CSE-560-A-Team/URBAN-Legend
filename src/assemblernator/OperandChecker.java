package assemblernator;

import assemblernator.ErrorReporting.ErrorHandler;

/**
 * checks operands for correct values.
 * @author Noah
 * @date Apr 14, 2012; 4:54:44 PM
 */
public class OperandChecker {
	/**
	 * Checks if expression following "DX" or "FX" is a valid expression.
	 * @author Noah
	 * @date Apr 14, 2012; 2:33:20 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param exp value of index register.
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
			
			if(indexReg > 7 || indexReg < 1) {
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
	public static boolean isValidNumWords(String exp) {
		boolean valid = true;
		
		try{
			int nw = Integer.parseInt(exp);
			
			if(nw > 15 || nw < 0) {
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
	public static boolean isValidMem(String addr) {
		return true;
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
	 * 
	 * @author Noah
	 * @date Apr 14, 2012; 5:10:51 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param str
	 * @return
	 * @specRef N/A
	 */
	public static boolean isValidString(int str) {
		return true;
	}
}
