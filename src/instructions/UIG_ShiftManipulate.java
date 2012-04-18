package instructions;

import assemblernator.AbstractInstruction;
import assemblernator.Module;
import assemblernator.OperandChecker;
import assemblernator.ErrorReporting.ErrorHandler;
import static assemblernator.ErrorReporting.makeError;

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
		int value = module.evaluate(this.getOperand("FC"), false, hErr, this, this.getOperandData("FC").keywordStartPosition);
		//The only possible combination has 2 operands
		if(!(this.operands.size() == 2)){
			isValid = false;
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
		}	
		else if(!(this.hasOperand("FC"))){
			isValid = false;
			isValid = OperandChecker.isValidConstant(value, ConstantRange.RANGE_SHIFT);
			hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "FC"), this.lineNum, -1);
			if(!isValid) hErr.reportError(makeError("OORconstant", "FC", this.getOpId()), this.lineNum, -1);
			//now there are 2 operands, one of which is FC
		} else if(this.hasOperand("DR")){
			dest = "DR";
			//range checking
			value = module.evaluate(this.getOperand("DR"), false, hErr, this, this.getOperandData("DR").keywordStartPosition);
			isValid = OperandChecker.isValidMem(value);
			if(!isValid) hErr.reportError(makeError("OORidxReg", "DR", this.getOpId()), this.lineNum, -1);
		} else if(this.hasOperand("DX")){
			dest = "DX";
			//range checking
			value = module.evaluate(this.getOperand("DX"), false, hErr, this, this.getOperandData("DX").keywordStartPosition);
			isValid = OperandChecker.isValidMem(value);
			if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
		} else{
			isValid = false;
			hErr.reportError(makeError("operandInsWrong", "EX", this.getOpId()), this.lineNum, -1);
		}
		
		//checks for invalid combo's between operands and opid's.
		//only checks if is valid so far.
		if(isValid) {
			if(this.hasOperand("DM") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", "DM", this.getOpId()), this.lineNum, -1);
				
			} else if (this.hasOperand("FM") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", "FM", this.getOpId()), this.lineNum, -1);
			} else if (this.hasOperand("EX") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", "EX", this.getOpId()), this.lineNum, -1);
			} else if (this.hasOperand("LR") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", "LR", this.getOpId()), this.lineNum, -1);
			} else if (this.hasOperand("ST") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", "ST", this.getOpId()), this.lineNum, -1);
			} else if (this.hasOperand("NW") && (this.getOpId().equals("ISHR") || this.getOpId().equals("ISHL")
					|| this.getOpId().equals("ISRA") || this.getOpId().equals("ISLA") || this.getOpId().equals("ROR") 
					|| this.getOpId().equals("ROL"))) {
				isValid = false;
				hErr.reportError(makeError("operandInsWrong", "NW", this.getOpId()), this.lineNum, -1);
			}
		}
		
		
		return isValid;
	}

	/**
	 * @see assemblernator.Instruction#assemble()
	 */
	@Override
	public final int[] assemble() {
		

		return null;
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

