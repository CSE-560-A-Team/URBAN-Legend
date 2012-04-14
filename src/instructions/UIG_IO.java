package instructions;

import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.OperandChecker;


/**
 * 
 * @author Noah
 * @date Apr 14, 2012; 5:47:13 PM
 */
public abstract class UIG_IO extends Instruction{
	OperandType operandType;

	/**
	 * 
	 * @author Noah
	 * @date Apr 14, 2012; 5:24:01 PM
	 */
	public enum OperandType {
		DM(true, false, false),
		DMDX(true, true, false),
		FM(false, false, false),
		FMFX(false, true, false),
		FL(false, false, true);
		
		
		private boolean input;
		private boolean index;
		private boolean literal;
		
		/**
		 * Constructs.
		 * @param input is the instruction being used as input or output?
		 * @param index is there an index register being used?
		 * @param literal is there a literal being used?
		 */
		OperandType(boolean input, boolean index, boolean literal) {
			this.input = input;
			this.index = index;
			this.literal = literal;
		}
	}

	
	/**
	 * @see assemblernator.Instruction#check(ErrorHandler)
	 */
	@Override
	public final boolean check(ErrorHandler hErr) {
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
	public final int[] assemble() {
		//all instructions start w/ 6 bit opcode.
		String nybbles = IOFormat.formatBinInteger(this.getOpcode(), 6); 
		String index = "000"; //default value of index bits if index registers are not used.
		int mem; 
		int[] assembled = new int[1];
		
		nybbles = nybbles + "0000"; //i/o instructions have a unused 4 bits following opcode.
		
		if(operandType.input) { //operands = {DM, NW} or {DM, NW, DX} 
			OperandChecker.isValidMem(this.getOperand("DM"));
			mem = Integer.parseInt(this.getOperand("DM")); //parse into decimal integer.
			if(operandType.index) { //operand = {DM, NW, DX}
				index = this.getOperand("DX"); //get index register decimal.
				OperandChecker.isValidIndex(index); 
				nybbles = nybbles + IOFormat.formatBinInteger(Integer.parseInt(index), 3); //add index register bits.
			}
		} else {
			if(operandType.literal) { //operands = {FL, NW}
				OperandChecker.isValidLiteral(this.getOperand("FL"));
				mem = Integer.parseInt(this.getOperand("FL"));
			} else {
				OperandChecker.isValidMem(this.getOperand("FM"));
				mem = Integer.parseInt(this.getOperand("FM"));
				if(operandType.index) {
					index = this.getOperand("FX"); //get index register decimal.
					OperandChecker.isValidIndex(index);
					nybbles = nybbles + IOFormat.formatBinInteger(Integer.parseInt(index), 3); //add index register bits.
				}
			}
		}
		
		OperandChecker.isValidNumWords(this.getOperand("NW"));
		nybbles = nybbles + IOFormat.formatBinInteger(Integer.parseInt(this.getOperand("NW")), 3); //add number of word bits.
		
		if(operandType.input) { 
			nybbles = nybbles + "00"; //unused two bits for input instruction format.
		} else {
			nybbles = nybbles + "0"; //1 unused bit for output instruction format.
			//followed by literal bit.
			if(operandType.literal) {
				nybbles = nybbles + "1"; 
			} else {
				nybbles = nybbles + "0";
			}
		}
		
		nybbles = nybbles + IOFormat.formatBinInteger(mem, 13); //concat mem bits.
		assembled[0] = Integer.parseInt(nybbles, 2); //parse as a binary integer.
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
