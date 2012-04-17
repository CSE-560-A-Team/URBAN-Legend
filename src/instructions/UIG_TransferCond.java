package instructions;

import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.ErrorReporting.ErrorHandler;
import static assemblernator.ErrorReporting.makeError;
import assemblernator.OperandChecker;

/**
 * @author Eric
 * @date Apr 14, 2012; 5:52:36 PM
 */
public abstract class UIG_TransferCond extends Instruction {
	String dest = "";
	String src = "";
	
	/**
	 * @author Eric
	 * @date Apr 14, 2012; 5:52:36 PM
	 */
	@Override
	public final boolean check(ErrorHandler hErr) {
		boolean isValid = true;
		//not enough operands
		if(this.operands.size() < 2){
			isValid=false;
			hErr.reportError(makeError("instructionMissingOp", this.getOpId(), ""), this.lineNum, -1);
			//checks combos for 2 operands
		}else if(this.operands.size() == 2){
			//combo check
			if(this.hasOperand("FR")){
				src="FR";
				//range checking
				isValid = OperandChecker.isValidReg(this.getOperand("FR"));
				if(!isValid) hErr.reportError(makeError("OORarithReg", "FR", this.getOpId()), this.lineNum, -1);
				if(this.hasOperand("DM")){
					dest="DM";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("DM"));
					if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
				}else{
					isValid = false;
					hErr.reportError(makeError("operandInsNeedAdd", this.getOpId(), "DM", "FR"), this.lineNum, -1);
					
				}
			}else{
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp","FR", this.getOpId()), this.lineNum, -1);
				
			}
			//checks combos for 3 operands
		}else if (this.operands.size() == 3){
			if(this.hasOperand("FR")){
			src="FR";
			//range checking
			isValid = OperandChecker.isValidReg(this.getOperand("FR"));
			if(!isValid) hErr.reportError(makeError("OORarithReg", "FR", this.getOpId()), this.lineNum, -1);
				if (this.hasOperand("DM") && this.hasOperand("DX")) {
				dest = "DMDX";
				//range checking
				isValid = OperandChecker.isValidIndex(this.getOperand("DX"));
				if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
				isValid = OperandChecker.isValidMem(this.getOperand("DM"));
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
				} else {
				isValid = false;
				hErr.reportError(makeError("operandInsNeedAdd", this.getOpId(), "DM and DX", "FR"), this.lineNum, -1);
				}
			
			}else{
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp","FR", this.getOpId()), this.lineNum, -1);
			}
			//to many operands
		}else{
			isValid =false;
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
		}
		
	
		return isValid;
	
}
	/**
	 * @author Eric
	 * @date Apr 14, 2012; 5:52:36 PM
	 */
	@Override
	public final int[] assemble() {
		return null;
	}
	
	
	/**
	 * Invokes parent's constructor.
	 * @param opid
	 * @param opcode
	 */
	UIG_TransferCond(String opid, int opcode) {
		super(opid, opcode);
	}
	
	/**
	 * default constructor does nothing.
	 */
	UIG_TransferCond() {}
	
}
