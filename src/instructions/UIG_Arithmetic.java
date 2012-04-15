package instructions;

import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.ErrorReporting.ErrorHandler;
import static assemblernator.ErrorReporting.makeError;
import assemblernator.OperandChecker;

/**
 * @author Eric
 * @date Apr 14, 2012; 5:22:20 PM
 */
public abstract class UIG_Arithmetic extends Instruction {

	String dest = "";
	String src = "";

	/**
	 * @author Eric
	 * @date Apr 14, 2012; 5:52:36 PM
	 */
	@Override
	public final boolean check(ErrorHandler hErr) {
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
				isValid = OperandChecker.isValidMem(this.getOperand("DM"));
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
				if (this.hasOperand("FR")) {
					src = "FR";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FR"));
					if(!isValid) hErr.reportError(makeError("OORidxReg", "FR", this.getOpId()), this.lineNum, -1);
				} else if (this.hasOperand("FM")) {
					src = "FM";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FM"));
					if(!isValid) hErr.reportError(makeError("OORmemAddr", "FM", this.getOpId()), this.lineNum, -1);
				} else if (this.hasOperand("FL")) {
					src = "FL";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FL"));
					if(!isValid) hErr.reportError(makeError("OOR13tc", "FL", this.getOpId()), this.lineNum, -1);
				} else {
					isValid = false;
					hErr.reportError(makeError("operandInsNeedAdd", this.getOpId(), "FR,FM, or FL", "DM"), this.lineNum, -1);
				}
				// checks combos associated with DR
			} else if (this.hasOperand("DR")) {
				dest = "DR";
				//range checking
				isValid = OperandChecker.isValidMem(this.getOperand("DR"));
				if(!isValid) hErr.reportError(makeError("OORidxReg", "DR", this.getOpId()), this.lineNum, -1);
				if (this.hasOperand("FR")) {
					src = "FR";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FR"));
					if(!isValid) hErr.reportError(makeError("OORidxReg", "FR", this.getOpId()), this.lineNum, -1);
				} else if (this.hasOperand("FM")) {
					src = "FM";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FM"));
					if(!isValid) hErr.reportError(makeError("OORmemAddr", "FM", this.getOpId()), this.lineNum, -1);
				} else if (this.hasOperand("FL")) {
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FL"));
					if(!isValid) hErr.reportError(makeError("OOR13tc", "FL", this.getOpId()), this.lineNum, -1);
					src = "FL";
				} else if (this.hasOperand("FX")) {
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FX"));
					if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
					src = "FX";
				} else {
					isValid = false;
					hErr.reportError(makeError("operandInsNeedAdd", this.getOpId(), "FR,FM,FL, or FX", "DR"), this.lineNum, -1);
				}
				// checks combos associated with DX
			} else if (this.hasOperand("DX")) {
				dest = "DX";
				if (this.hasOperand("FL")) {
					src = "FL";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FL"));
					if(!isValid) hErr.reportError(makeError("OOR13tc", "FL", this.getOpId()), this.lineNum, -1);
				} else if (this.hasOperand("FX")) {
					src = "FX";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FX"));
					if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
				} else {
					isValid = false;
					hErr.reportError(makeError("operandInsNeedAdd", this.getOpId(), "FX or FL", "DX"), this.lineNum, -1);
				}

			} else {
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "DX,DR or DM"), this.lineNum, -1);
			}
			// checks all combinations for three operands instructions
		} else if (this.operands.size() == 3) {
			// checks combos associated FR
			if (this.hasOperand("FR")) {
				src = "FR";
				//range checking
				isValid = OperandChecker.isValidMem(this.getOperand("FR"));
				if(!isValid) hErr.reportError(makeError("OORidxReg", "FR", this.getOpId()), this.lineNum, -1);
				if (this.hasOperand("DM") && this.hasOperand("DX")) {
					dest = "DMDX";
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("DX"));
					if(!isValid) hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
					isValid = OperandChecker.isValidMem(this.getOperand("DM"));
					if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
				} else {
					isValid = false;
					hErr.reportError(makeError("operandInsNeedAdd", this.getOpId(), "DM and DX", "FR"), this.lineNum, -1);
				}
				// checks combos associated DR
			} else if (this.hasOperand("DR")) {
				dest = "DR";
				//range checking
				isValid = OperandChecker.isValidMem(this.getOperand("DR"));
				if(!isValid) hErr.reportError(makeError("OORidxReg", "DR", this.getOpId()), this.lineNum, -1);
				if (this.hasOperand("FX") && this.hasOperand("FM")) {
					//range checking
					isValid = OperandChecker.isValidMem(this.getOperand("FX"));
					if(!isValid) hErr.reportError(makeError("OORidxReg", "FX", this.getOpId()), this.lineNum, -1);
					isValid = OperandChecker.isValidMem(this.getOperand("FM"));
					if(!isValid) hErr.reportError(makeError("OORmemAddr", "FM", this.getOpId()), this.lineNum, -1);
					src = "FXFM";
				} else {
					isValid = false;
					hErr.reportError(makeError("operandInsNeedAdd", this.getOpId(), "FX and FM", "DR"), this.lineNum, -1);
				}
			} else {
				isValid = false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "FR or DR"), this.lineNum, -1);
			}
			// more than three operands is invalid
		} else {
			isValid = false;
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
		}
		
		return isValid;

	}

	@Override
	public final int[] assemble() {
		String code = IOFormat.formatBinInteger(this.getOpcode(), 6);
		if (dest == "DR") {
			if (src == "FM" || src == "FL" || src == "FXFM") {
				// format 0
			} else {
				// format 1
			}
		} else if (dest == "DX") {
			// and so on
		}

		return null;

	}
}
