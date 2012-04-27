package instructions;

import static assemblernator.ErrorReporting.makeError;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.IOFormat;
import assemblernator.Module;
import assemblernator.OperandChecker;

/**
 * @author Ratul Khosla
 * @date Apr 14, 2012; 5:25:24 PM
 */
public abstract class UIG_ShiftManipulate extends AbstractInstruction {
	
	/**
	 * A variable representing the from part of the instruction.
	 */
	String origin = "";
	/**
	 * A variable representing the destination/to part of the instruction. 
	 */
	String dest = "";
		
	/**
	 * @author Ratul Khosla
	 * @date Apr 14, 2012; 5:29:29 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @return  returns whether the given combinatin is valid or not.
	 * @specRef N/A
	 */
	@Override
	public final boolean check(ErrorHandler hErr, Module module){
		boolean isValid = true;
		int value;
		//The only possible combination has 2 operands
		if(this.operands.size() > 2){
			isValid = false;
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
		} else if(this.operands.size() < 2) {
			isValid = false;
			hErr.reportError(makeError("tooFewOperandsIns", this.getOpId()), this.lineNum, -1);
		} else if(!(this.hasOperand("FC"))){
			isValid = false;
			//isValid = OperandChecker.isValidConstant(value, ConstantRange.RANGE_SHIFT);
			hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "FC"), this.lineNum, -1);
			//if(!isValid) hErr.reportError(makeError("OORconstant", "FC", this.getOpId()), this.lineNum, -1);
			//now there are 2 operands, one of which is FC
		} else if(this.hasOperand("DR")){
			//check FC
			value = module.evaluate(this.getOperand("FC"), false, hErr, this, this.getOperandData("FC").keywordStartPosition); //value of FC
			isValid = OperandChecker.isValidConstant(value, ConstantRange.RANGE_SHIFT); //check if value of FC is valid.
			if(!isValid) hErr.reportError(makeError("OORconstant", "FC", 
					this.getOpId(), Integer.toString(ConstantRange.RANGE_SHIFT.min), Integer.toString(ConstantRange.RANGE_SHIFT.max)), this.lineNum, -1);
			this.getOperandData("FC").value = value;
			//
			dest = "DR";
			//range checking
			value = module.evaluate(this.getOperand("DR"), false, hErr, this, this.getOperandData("DR").keywordStartPosition);
			isValid = OperandChecker.isValidReg(value);
			if(!isValid) hErr.reportError(makeError("OORidxReg", "DR", this.getOpId()), this.lineNum, -1);
			this.getOperandData("DR").value = value;
		} else if(this.hasOperand("DX")){
			dest = "DX";
			//range checking
			value = module.evaluate(this.getOperand("DX"), false, hErr, this, this.getOperandData("DX").keywordStartPosition);
			isValid = OperandChecker.isValidIndex(value);
			if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
			this.getOperandData("DX").value = value;
		} else {
			isValid = false;
			if(this.hasOperand("FR")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FR"), this.lineNum, -1);
			}  else if(this.hasOperand("DM")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "DM"), this.lineNum, -1);				
			}  else if(this.hasOperand("FL")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FL"), this.lineNum, -1);				
			} else if(this.hasOperand("FX")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FX"), this.lineNum, -1);				
			} else if(this.hasOperand("FS")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FS"), this.lineNum, -1);				
			} else if(this.hasOperand("LR")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "LR"), this.lineNum, -1);				
			} else if(this.hasOperand("FM")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FM"), this.lineNum, -1);				
			} else if(this.hasOperand("EX")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "EX"), this.lineNum, -1);				
			} else if(this.hasOperand("NW")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "NW"), this.lineNum, -1);				
			} else if(this.hasOperand("ST")){
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "ST"), this.lineNum, -1);				
			}
		}
		
		//checks for invalid combo's between operands and opid's.
		//only checks if is valid so far.
		if(isValid) {
			if(this.hasOperand("DM") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "DM"), this.lineNum, -1);
				
			} else if (this.hasOperand("FM") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FM"), this.lineNum, -1);
			} else if (this.hasOperand("FX") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FX"), this.lineNum, -1);
			} else if (this.hasOperand("FR") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "FR"), this.lineNum, -1);
			} else if (this.hasOperand("EX") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "EX"), this.lineNum, -1);
			} else if (this.hasOperand("LR") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "LR"), this.lineNum, -1);
			} else if (this.hasOperand("ST") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "ST"), this.lineNum, -1);
			} else if (this.hasOperand("NW") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", this.getOpId(), "NW"), this.lineNum, -1);
			}
		}
		
		
		return isValid;
	}

	/**
	 * @see assemblernator.Instruction#assemble()
	 */
	@Override
	public final int[] assemble() {
		//all instructions start w/ 6 bit opcode.
		String code = IOFormat.formatBinInteger(this.getOpcode(), 6); //011000
		
		code = code + "0";// order bit is always 0 for shift/manipulate instructions.
		
		String reg = "000"; //default value of register bits if registers are not used.
		String index = "000"; //default value of index bits if index registers are not used. 
		String mem; 
		int[] assembled = new int[1];
			
		
		if(this.hasOperand("DX")) { 
			index = IOFormat.formatBinInteger(this.getOperandData("DX").value,3);
		} else if(this.hasOperand("DR")) { 
			reg = IOFormat.formatBinInteger(this.getOperandData("DR").value,3);
		}

		code = code + reg;
		code = code + index;//add index register bits.
		code = code + IOFormat.formatBinInteger(Integer.parseInt(this.getOperand("NW")), 3); //add number of word bits.
		code = code + "00"; //for stack and literal bits 	
		
		mem = IOFormat.formatBinInteger(this.getOperandData("FC").value,13);
				
		code = code + mem; //concat mem bits.
		assembled[0] = Integer.parseInt(code, 2); //parse as a binary integer.
		
		return assembled;
	}


/**
 * Invokes parent's constructor.
 * @param opid	 the operation id
 * @param opcode 	the opcode
 */UIG_ShiftManipulate(String opid, int opcode) {
	super(opid, opcode);
}

/**
 * default constructor does nothing.
 */
UIG_ShiftManipulate() {}
}

