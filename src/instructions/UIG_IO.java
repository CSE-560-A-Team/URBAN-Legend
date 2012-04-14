package instructions;

import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.ErrorReporting.ErrorHandler;


public abstract class UIG_IO extends Instruction{
	OperandType operandType;
	
	public enum OperandType {
		DM(true, false, false),
		DMDX(true, true, false),
		FM(false, false, false),
		FMFX(false, true, false),
		FL(false, false, true);
		
		
		private boolean input;
		private boolean index;
		private boolean literal;
		
		OperandType(boolean input, boolean index, boolean literal) {
			this.input = input;
			this.index = index;
			this.literal = literal;
		}
	}

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
	private boolean checkIndex(String exp) {
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
	
	private boolean checkNumWords(String exp) {
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
	
	private boolean checkLiteral(String lit) {
		return true;
	}
	
	private boolean checkMem(String addr) {
		return true;
	}
	
	private boolean checkMem(int addr) {
		return true;
	}
	
	/**
	 * @see assemblernator.Instruction#check(ErrorHandler)
	 */
	@Override
	public boolean check(ErrorHandler hErr) {
		boolean isValid = true;
		
		if(!this.hasOperand("NW")) {
			isValid = false;
			//error
		} else if(this.operands.size() == 2) {
			if(this.hasOperand("DM")) {
				this.operandType = OperandType.DM;
			} else if(this.hasOperand("FM")) {
				this.operandType = OperandType.FM;
			} else if(this.hasOperand("FL")){
				this.operandType = OperandType.FL;
			} else {
				isValid = false;
				//error
			}
		} else if(this.operands.size() == 3) {
			if(this.hasOperand("DM") && this.hasOperand("DX")) {
				this.operandType = OperandType.DMDX;
			} else if(this.hasOperand("FM")  && this.hasOperand("FX")) {
				this.operandType = OperandType.FMFX;
			} else {
				isValid = false;
				//error
			}
		} else {
			isValid = false;
			//error
		}
		
		return isValid;
	}
	
	/**
	 * @see assemblernator.Instruction#assemble()
	 */
	@Override
	public int[] assemble() {
		String nybbles = IOFormat.formatBinInteger(this.getOpcode(), 6); //opcode
		String binIndex = "000";
		int mem;
		int[] assembled = new int[1];
		
		nybbles = nybbles + "0000";
		
		if(operandType.input) {
			checkMem(this.getOperand("DM"));
			mem = Integer.parseInt(this.getOperand("DM"));
			if(operandType.index) {
				binIndex = this.getOperand("DX");
				checkIndex(binIndex);
				mem += Integer.parseInt(binIndex);
				checkMem(mem);
				nybbles = nybbles + binIndex;
			}
		} else {
			if(operandType.literal) {
				checkLiteral(this.getOperand("FL"));
				mem = Integer.parseInt(this.getOperand("FL"));
			} else {
				checkMem(this.getOperand("FM"));
				mem = Integer.parseInt(this.getOperand("FM"));
				if(operandType.index) {
					binIndex = this.getOperand("FX");
					checkIndex(binIndex);
					mem += Integer.parseInt(binIndex);
					checkMem(mem);
					nybbles = nybbles + binIndex;
				}
			}
		}
		
		checkNumWords(this.getOperand("NW"));
		nybbles = nybbles + IOFormat.formatBinInteger(Integer.parseInt(this.getOperand("NW")), 3);
		
		if(operandType.input) {
			nybbles = nybbles + "00";
		} else {
			nybbles = nybbles + "0";
			if(operandType.literal) {
				nybbles = nybbles + "1";
			} else {
				nybbles = nybbles + "0";
			}
		}
		
		
		nybbles = nybbles + IOFormat.formatBinInteger(mem, 13);
		assembled[0] = Integer.parseInt(nybbles);
		
		return assembled;
	}
	
	/**
	 * Invokes parent's constructor.
	 * @param opid
	 * @param opcode
	 */
	UIG_IO(String opid, int opcode) {
		super(opid, opcode);
	}
	
	/**
	 * default constructor does nothing.
	 */
	UIG_IO() {}
}
