package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.InstructionFormatter.formatOther;
import static simulanator.Deformatter.breakDownOther;
import simulanator.Machine;
import simulanator.Deformatter.OpcodeBreakdown;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Module;
import assemblernator.Module.Value;
import assemblernator.Module.Value.BitLocation;
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
	 * The type of operand specifying the source for this operation.
	 */
	String src = "";

	/**
	 * @author Eric
	 * @date Apr 14, 2012; 5:52:36 PM
	 */
	@Override public final boolean check(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		// formats FL DM, FC DM, EX DM
		if (this.hasOperand("FL") && this.hasOperand("DM")
				|| this.hasOperand("EX") && this.hasOperand("DM")) {
			Operand o = getOperandData("DM");
			Value constantSize = module.evaluate(o.expression, true,
					BitLocation.LowAddress, hErr, this, o.valueStartPosition);
			this.getOperandData("DM").value = constantSize;
			isValid = OperandChecker.isValidMem(constantSize.value);
			if (!isValid) {
				hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()),
						this.lineNum, -1);
				return isValid;
			}
			if (this.hasOperand("FL")) {
				Operand o1 = getOperandData("FL");
				Value constantSize1 = module.evaluate(o1.expression, false,
						BitLocation.HighLiteral, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("FL").value = constantSize1;
				isValid = OperandChecker.isValidLiteral(constantSize1.value,
						ConstantRange.RANGE_12_TC);
				if (!isValid)
					hErr.reportError(
							makeError("OORconstant", "FL", this.getOpId(),
									"-2^11", "2^11-1"), this.lineNum, -1);
			}
			else if (this.hasOperand("EX")) {
				Operand o1 = getOperandData("EX");
				Value constantSize1 = module.evaluate(o1.expression, false,
						BitLocation.HighLiteral, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("EX").value = constantSize1;
				isValid = OperandChecker.isValidLiteral(constantSize1.value,
						ConstantRange.RANGE_12_TC);
				if (!isValid)
					hErr.reportError(
							makeError("OORconstant", "EX", this.getOpId(),
									"-2^11", "2^11-1"), this.lineNum, -1);
			}
			if (this.operands.size() != 2) {
				// error to many operands
				isValid = false;
				hErr.reportError(makeError("extraOperandsIns", this.getOpId()),
						this.lineNum, -1);
			}

			// formats FM DM
		}
		else if (this.hasOperand("FM") && this.hasOperand("DM")) {
			Operand o = getOperandData("DM");
			Value constantSize = module.evaluate(o.expression, true,
					BitLocation.LowAddress, hErr, this, o.valueStartPosition);
			this.getOperandData("DM").value = constantSize;
			isValid = OperandChecker.isValidMem(constantSize.value);
			if (!isValid) {
				hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()),
						this.lineNum, -1);
				return isValid;
			}
			Operand o2 = getOperandData("FM");
			Value constantSize2 = module.evaluate(o2.expression, true,
					BitLocation.HighAddress, hErr, this, o2.valueStartPosition);
			this.getOperandData("FM").value = constantSize2;
			isValid = OperandChecker.isValidMem(constantSize2.value);
			if (!isValid)
				hErr.reportError(makeError("OORmemAddr", "FM", this.getOpId()),
						this.lineNum, -1);

			if (this.operands.size() != 2) {
				// error to many operands
				isValid = false;
				hErr.reportError(makeError("extraOperandsIns", this.getOpId()),
						this.lineNum, -1);
			}
			// formats for {FL DR}, {FL DX}, {EX DR}, {EX DX}
		}
		else if (this.hasOperand("FL") || this.hasOperand("EX")) {

			// gets value for literal part of format
			if (this.hasOperand("FL")) {
				Operand o1 = getOperandData("FL");
				Value constantSize1 = module.evaluate(o1.expression, false,
						BitLocation.Literal, hErr, this, o1.valueStartPosition);
				this.getOperandData("FL").value = constantSize1;
				isValid = OperandChecker.isValidLiteral(constantSize1.value,
						ConstantRange.RANGE_16_TC);
				if (!isValid)
					hErr.reportError(
							makeError("OORconstant", "FL", this.getOpId(),
									"-2^15", "2^15-1"), this.lineNum, -1);
			}
			else if (this.hasOperand("EX")) {
				Operand o1 = getOperandData("EX");
				Value constantSize1 = module.evaluate(o1.expression, false,
						BitLocation.Literal, hErr, this, o1.valueStartPosition);
				this.getOperandData("EX").value = constantSize1;
				isValid = OperandChecker.isValidLiteral(constantSize1.value,
						ConstantRange.RANGE_16_TC);
				if (!isValid)
					hErr.reportError(
							makeError("OORconstant", "EX", this.getOpId(),
									"-2^15", "2^15-1"), this.lineNum, -1);
			}
			if (this.operands.size() != 2) {
				// error invalid combo
				isValid = false;
				hErr.reportError(makeError("extraOperandsIns", this.getOpId()),
						this.lineNum, -1);
			}
			// gets value for dest part of format
			if (this.hasOperand("DR")) {
				Operand o = getOperandData("DR");
				Value constantSize = module.evaluate(o.expression, false,
						BitLocation.Other, hErr, this, o.valueStartPosition);
				this.getOperandData("DR").value = constantSize;
				isValid = OperandChecker.isValidReg(constantSize.value);
				if (!isValid)
					hErr.reportError(
							makeError("OORarithReg", "DR", this.getOpId()),
							this.lineNum, -1);
			}
			else if (this.hasOperand("DX")) {
				Operand o = getOperandData("DX");
				Value constantSize = module.evaluate(o.expression, false,
						BitLocation.Other, hErr, this, o.valueStartPosition);
				this.getOperandData("DX").value = constantSize;
				isValid = OperandChecker.isValidIndex(constantSize.value);
				if (!isValid)
					hErr.reportError(
							makeError("OORidxReg", "DX", this.getOpId()),
							this.lineNum, -1);

			}
			else {
				// error missing dest DR DX
				isValid = false;
				hErr.reportError(
						makeError("instructionMissingOp", this.getOpId(),
								"{DR} or {DX}"), this.lineNum, -1);
			}
			// formats all other combos
		}
		else {

			// gets destReg, mem , and ixr
			if (this.hasOperand("DM")) {
				Operand o2 = getOperandData("DM");
				Value constantSize2 = module.evaluate(o2.expression, true,
						BitLocation.Address, hErr, this, o2.valueStartPosition);
				this.getOperandData("DM").value = constantSize2;
				isValid = OperandChecker.isValidMem(constantSize2.value);
				if (!isValid) {
					hErr.reportError(
							makeError("OORmemAddr", "DM", this.getOpId()),
							this.lineNum, -1);
					return isValid;
				}
				if (this.hasOperand("DX")) {
					Operand o = getOperandData("DX");
					Value constantSize = module
							.evaluate(o.expression, false, BitLocation.Other,
									hErr, this, o.valueStartPosition);
					this.getOperandData("DX").value = constantSize;
					isValid = OperandChecker.isValidIndex(constantSize.value);
					if (!isValid)
						hErr.reportError(
								makeError("OORidxReg", "DX", this.getOpId()),
								this.lineNum, -1);
				}
			}
			else if (this.hasOperand("DX")) {
				Operand o = getOperandData("DX");
				Value constantSize = module.evaluate(o.expression, false,
						BitLocation.Other, hErr, this, o.valueStartPosition);
				this.getOperandData("DX").value = constantSize;
				isValid = OperandChecker.isValidIndex(constantSize.value);
				if (!isValid)
					hErr.reportError(
							makeError("OORidxReg", "DX", this.getOpId()),
							this.lineNum, -1);
			}
			else if (this.hasOperand("DR")) {
				Operand o = getOperandData("DR");
				Value constantSize = module.evaluate(o.expression, false,
						BitLocation.Other, hErr, this, o.valueStartPosition);
				this.getOperandData("DR").value = constantSize;
				isValid = OperandChecker.isValidReg(constantSize.value);
				if (!isValid)
					hErr.reportError(
							makeError("OORarithReg", "DR", this.getOpId()),
							this.lineNum, -1);

			}
			else {
				// error missing dest
				isValid = false;
				hErr.reportError(
						makeError("instructionMissingOp", this.getOpId(),
								"{DM,DX}, {DR}, or {DX}"), this.lineNum, -1);
			}

			// gets srcReg, mem, and ixr
			if (this.hasOperand("FM")) {
				Operand o2 = getOperandData("FM");
				Value constantSize2 = module.evaluate(o2.expression, true,
						BitLocation.Address, hErr, this, o2.valueStartPosition);
				this.getOperandData("FM").value = constantSize2;
				isValid = OperandChecker.isValidMem(constantSize2.value);
				if (!isValid) {
					hErr.reportError(
							makeError("OORmemAddr", "FM", this.getOpId()),
							this.lineNum, -1);
					return isValid;
				}
				if (this.hasOperand("FX")) {
					Operand o1 = getOperandData("FX");
					Value constantSize1 = module.evaluate(o1.expression, false,
							BitLocation.Other, hErr, this,
							o1.valueStartPosition);
					this.getOperandData("FX").value = constantSize1;
					isValid = OperandChecker.isValidIndex(constantSize1.value);
					if (!isValid)
						hErr.reportError(
								makeError("OORidxReg", "FX", this.getOpId()),
								this.lineNum, -1);
				}
			}
			else if (this.hasOperand("FR")) {
				Operand o1 = getOperandData("FR");
				Value constantSize1 = module.evaluate(o1.expression, false,
						BitLocation.Other, hErr, this, o1.valueStartPosition);
				this.getOperandData("FR").value = constantSize1;
				isValid = OperandChecker.isValidReg(constantSize1.value);
				if (!isValid)
					hErr.reportError(
							makeError("OORarithReg", "FR", this.getOpId()),
							this.lineNum, -1);
			}
			else if (this.hasOperand("FX")) {
				Operand o1 = getOperandData("FX");
				Value constantSize1 = module.evaluate(o1.expression, false,
						BitLocation.Other, hErr, this, o1.valueStartPosition);
				this.getOperandData("FX").value = constantSize1;
				isValid = OperandChecker.isValidIndex(constantSize1.value);
				if (!isValid)
					hErr.reportError(
							makeError("OORidxReg", "FX", this.getOpId()),
							this.lineNum, -1);
			}
			else {
				// error missing source
				isValid = false;
				hErr.reportError(
						makeError("instructionMissingOp", this.getOpId(),
								"{FM,FX}, {FR}, or {FX}"), this.lineNum, -1);
			}
			if (this.operands.size() > 3) {
				// error to many operands
				isValid = false;
				hErr.reportError(makeError("extraOperandsIns", this.getOpId()),
						this.lineNum, -1);
			}
			else if (this.operands.size() < 2) {
				// error missing operands
				isValid = false;
				hErr.reportError(
						makeError("tooFewOperandsIns", this.getOpId()),
						this.lineNum, -1);
			}

		}
		return isValid;
	}


	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public final void execute(int instruction, Machine machine) {
		OpcodeBreakdown brkDwn = breakDownOther(instruction);
		int srcValue = brkDwn.readFromSource(machine);
		int destValue = brkDwn.readFromDest(machine);
		brkDwn.putToDest(operate(destValue, srcValue, machine.hErr), machine);
	}

	/**
	 * 
	 * @author Eric
	 * @date May 23, 2012; 7:43:08 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param srcValue
	 *            the Value of the source
	 * @param destValue
	 *            the Value of the destination
	 * @param hErr
	 *            TODO
	 * @return the value to be put in memory
	 * @specRef N/A
	 */
	abstract int operate(int srcValue, int destValue, ErrorHandler hErr);


	/**
	 * @author Eric
	 * @date Apr 14, 2012; 5:52:36 PM
	 */
	@Override public final int[] assemble() {
		return formatOther(this);
	}

	/**
	 * Invokes parent's constructor.
	 * 
	 * @param opid
	 *            The opId of child instructions.
	 * @param opcode
	 *            The distinguishing opcode of child instructions.
	 */
	UIG_Arithmetic(String opid, int opcode) {
		super(opid, opcode);
	}

	/**
	 * default constructor does nothing.
	 */
	UIG_Arithmetic() {}
}
