package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.InstructionFormatter.formatOther;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Module;
import assemblernator.Module.Value;
import assemblernator.OperandChecker;

/**
 * @author Eric
 * @date Apr 14, 2012; 5:22:20 PM
 */
public abstract class UIG_Arithmetic extends AbstractInstruction {

	/**
	 * The type of operand specifying the destination for this operation.
	 */
	String dest = "";
	
	/**
	 *The type of operand specifying the source for this operation.
	 */
	String src = "";

	/**
	 * @author Eric
	 * @date Apr 14, 2012; 5:52:36 PM
	 */
	@Override
	public final boolean check(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		// any size under two is invalid
		if (this.operands.size() < 2) {
			isValid = false;
			hErr.reportError(makeError("instructionMissingOp", this.getOpId(), ""), this.lineNum, -1);
			// checks all combinations for two operands if a combo is not found
			// operands are invalid
		} else if (this.operands.size() == 2) {
			// checks combos associated with DM
			if (this.hasOperand("DM")) {
				dest = "DM";
				//range checking
				Operand o = getOperandData("DM");
				Value constantSize = module.evaluate(o.expression, true, hErr, this,
						o.valueStartPosition);
				this.getOperandData("DM").value = constantSize;
				isValid = OperandChecker.isValidMem(constantSize.value);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
				// checks combos associated with DR
			} else if (this.hasOperand("DR")) {
				dest = "DR";
				//range checking
				Operand o = getOperandData("DR");
				Value constantSize = module.evaluate(o.expression, false, hErr, this,
						o.valueStartPosition);
				this.getOperandData("DR").value = constantSize;
				isValid = OperandChecker.isValidReg(constantSize.value);
				if(!isValid) hErr.reportError(makeError("OORarithReg", "DR", this.getOpId()), this.lineNum, -1);
				// checks combos associated with DX
			} else if (this.hasOperand("DX")) {
				dest = "DX";
				//range checking
				Operand o = getOperandData("DX");
				Value constantSize = module.evaluate(o.expression, false, hErr, this,
						o.valueStartPosition);
				this.getOperandData("DX").value = constantSize;
				isValid = OperandChecker.isValidIndex(constantSize.value);
				if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
	

			} else {
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "DX,DR or DM"), this.lineNum, -1);
			}
			
			if (this.hasOperand("FL")) {
				src = "FL";
				//range checking
				Operand o1 = getOperandData("FL");
				Value constantSize1 = module.evaluate(o1.expression, false, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("FL").value = constantSize1;
				if(dest.equals("DM")){
				isValid = OperandChecker.isValidLiteral(constantSize1.value, ConstantRange.RANGE_12_TC);
				if(!isValid) hErr.reportError(makeError("OORconstant", "FL", this.getOpId(), "-2^11", "2^11-1"), this.lineNum, -1);
				}else{
				isValid = OperandChecker.isValidLiteral(constantSize1.value, ConstantRange.RANGE_16_TC);
				if(!isValid) hErr.reportError(makeError("OORconstant", "FL", this.getOpId(), "-2^15", "2^15-1"), this.lineNum, -1);
				}
			} else if (this.hasOperand("FX")) {
				src = "FX";
				//range checking
				Operand o1 = getOperandData("FX");
				Value constantSize1 = module.evaluate(o1.expression, false, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("FX").value = constantSize1;
				isValid = OperandChecker.isValidIndex(constantSize1.value);
				if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
			}else if (this.hasOperand("FR")){
				src = "FR";
				//range checking
				Operand o1 = getOperandData("FR");
				Value constantSize1 = module.evaluate(o1.expression, false, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("FR").value = constantSize1;
				isValid = OperandChecker.isValidReg(constantSize1.value);
				if(!isValid) hErr.reportError(makeError("OORarithReg", "FR", this.getOpId()), this.lineNum, -1);
			}else if(this.hasOperand("FM")){
				src = "FM";
				//range checking
				Operand o1 = getOperandData("FM");
				Value constantSize1 = module.evaluate(o1.expression, true, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("FM").value = constantSize1;
				isValid = OperandChecker.isValidMem(constantSize1.value);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "FM", this.getOpId()), this.lineNum, -1);
			}else if(this.hasOperand("EX")){
				src = "EX";
				//range checking
				Operand o1 = getOperandData("EX");
				Value constantSize1 = module.evaluate(o1.expression, true, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("EX").value = constantSize1;
				if(dest.equals("DM")){
				isValid = OperandChecker.isValidLiteral(constantSize1.value, ConstantRange.RANGE_12_TC);
				if(!isValid) hErr.reportError(makeError("OORconstant", "EX", this.getOpId(), "-2^11", "2^11-1"), this.lineNum, -1);
				}else{
				isValid = OperandChecker.isValidLiteral(constantSize1.value, ConstantRange.RANGE_16_TC);
				if(!isValid) hErr.reportError(makeError("OORconstant", "EX", this.getOpId(), "-2^15", "2^15-1"), this.lineNum, -1);
				}
			}else{
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "FM,FR,FX,FL or EX"), this.lineNum, -1);
			}
				
			// checks all combinations for three operands instructions
		}	else if (this.operands.size() == 3) {
			if(this.hasOperand("DM") && this.hasOperand("DX")){
				dest = "DMDX";
				//check for DM
				Operand o2 = getOperandData("DM");
				Value constantSize2 = module.evaluate(o2.expression, true, hErr, this,
							o2.valueStartPosition);
				this.getOperandData("DM").value = constantSize2;
				isValid = OperandChecker.isValidMem(constantSize2.value);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
				//check for DX
				Operand o = getOperandData("DX");
				Value constantSize = module.evaluate(o.expression, false, hErr, this,
						o.valueStartPosition);
				this.getOperandData("DX").value = constantSize;
				isValid = OperandChecker.isValidIndex(constantSize.value);
				if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
				//check for fr
				if (this.hasOperand("FR")){
					src="FR";
					//check for FR
					Operand o1 = getOperandData("FR");
					Value constantSize1 = module.evaluate(o1.expression, false, hErr, this,
							o1.valueStartPosition);
					this.getOperandData("FR").value = constantSize1;
					isValid = OperandChecker.isValidReg(constantSize1.value);
					if(!isValid) hErr.reportError(makeError("OORarithReg", "FR", this.getOpId()), this.lineNum, -1);
				}else if (this.hasOperand("FX")){
					src="FX";
					//check for FX
					Operand o1 = getOperandData("FX");
					Value constantSize1 = module.evaluate(o1.expression, false, hErr, this,
							o1.valueStartPosition);
					this.getOperandData("FX").value = constantSize1;
					isValid = OperandChecker.isValidIndex(constantSize1.value);
					if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
				}else{
					isValid = false;
					hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "FR or FX"), this.lineNum, -1);
				}
			}else if(this.hasOperand("FM") && this.hasOperand("FX")){
				src="FMFX";
				//check for FX
				Operand o1 = getOperandData("FX");
				Value constantSize1 = module.evaluate(o1.expression, false, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("FX").value = constantSize1;
				isValid = OperandChecker.isValidIndex(constantSize1.value);
				if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
				//check for FM
				Operand o2 = getOperandData("FM");
				Value constantSize2 = module.evaluate(o2.expression, true, hErr, this,
						o2.valueStartPosition);
				this.getOperandData("FM").value = constantSize2;
				isValid = OperandChecker.isValidMem(constantSize2.value);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "FM", this.getOpId()), this.lineNum, -1);
				//check for dr
				if(this.hasOperand("DR")){
					dest="DR";
					//check for DR
					Operand o = getOperandData("DR");
					Value constantSize = module.evaluate(o.expression, false, hErr, this,
							o.valueStartPosition);
					this.getOperandData("DR").value = constantSize;
					isValid = OperandChecker.isValidReg(constantSize.value);
					if(!isValid) hErr.reportError(makeError("OORarithReg", "DR", this.getOpId()), this.lineNum, -1);
				}else if(this.hasOperand("DX")){
					dest="DX";
					//check for DX
					Operand o = getOperandData("DX");
					Value constantSize = module.evaluate(o.expression, false, hErr, this,
							o.valueStartPosition);
					this.getOperandData("DX").value = constantSize;
					isValid = OperandChecker.isValidIndex(constantSize.value);
					if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
				}else{
					isValid = false;
					hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "DR or DX"), this.lineNum, -1);
				}
			}else{
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "DM and DX or FM and FX"), this.lineNum, -1);
			}
		}
		
		return isValid;

	}

	/**
	 * @author Eric
	 * @date Apr 14, 2012; 5:52:36 PM
	 */
	@Override
	public final int[] assemble() {
		return formatOther(this);
	}
	
	/**
	 * Invokes parent's constructor.
	 * @param opid The opId of child instructions.
	 * @param opcode The distinguishing opcode of child instructions.
	 */
	UIG_Arithmetic(String opid, int opcode) {
		super(opid, opcode);
	}
	
	/**
	 * default constructor does nothing.
	 */
	UIG_Arithmetic() {}
}
