package instructions;

import static assemblernator.ErrorReporting.makeError;
import static assemblernator.InstructionFormatter.formatOther;
import static simulanator.Deformatter.breakDownOther;
import simulanator.Machine;
import simulanator.Deformatter.OpcodeBreakdownOther;
import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.Module.Value;
import assemblernator.Module.Value.BitLocation;
import assemblernator.OperandChecker;

/**
 * The RET instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef JT6
 */
public class USI_RET extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "RET";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x00000026; // 0b10011000000000000000000000000000

	/** The static instance for this instruction. */
	static USI_RET staticInstance = new USI_RET(true);

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc+1;
	}
	/**
	 * The type of operand specifying the destination for this operation.
	 */
	String dest = "";
	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		boolean isValid = true;
		//less than 1 operand error
		if(this.operands.size() < 1){
			isValid = false;
			hErr.reportError(makeError("tooFewOperandsIns", this.getOpId()), this.lineNum, -1);
			//checks for DM
		}else if (this.operands.size() == 1){
			if(this.hasOperand("DM")){
				dest = "DM";
				//range check
				Operand o = getOperandData("DM");
				Value constantSize = module.evaluate(o.expression, true, BitLocation.Address,  hErr, this,
						o.valueStartPosition);
				this.getOperandData("DM").value = constantSize;
				isValid = OperandChecker.isValidMem(constantSize.value);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
			}else{
				isValid=false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "DM"), this.lineNum, -1);
			}
			//more than 1 operand error
		}else if(this.operands.size() == 2){
			if(this.hasOperand("DM") && this.hasOperand("DX")){
				dest="DMDX";
				//range check
				Operand o1 = getOperandData("DX");
				Value constantSize1 = module.evaluate(o1.expression, false, BitLocation.Other, hErr, this,
						o1.valueStartPosition);
				this.getOperandData("DX").value = constantSize1;
				isValid = OperandChecker.isValidIndex(constantSize1.value);
				if(!isValid){
					hErr.reportError(makeError("OORidxReg", "DX", this.getOpId()), this.lineNum, -1);
					return isValid;
				}
				Operand o2 = getOperandData("DM");
				Value constantSize2 = module.evaluate(o2.expression, true, BitLocation.Address, hErr, this,
						o2.valueStartPosition);
				this.getOperandData("DM").value = constantSize2;
				isValid = OperandChecker.isValidMem(constantSize2.value);
				if(!isValid) hErr.reportError(makeError("OORmemAddr", "DM", this.getOpId()), this.lineNum, -1);
			}else{
				isValid=false;
				hErr.reportError(makeError("instructionMissingOp", this.getOpId(), "DM or DX"), this.lineNum, -1);
			}
			//to many operands
		}else{
			isValid =false;
			hErr.reportError(makeError("extraOperandsIns", this.getOpId()), this.lineNum, -1);
		}
		return isValid; // TODO: IMPLEMENT
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return formatOther(this);
	}

	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {
		OpcodeBreakdownOther brkDwn = breakDownOther(instruction);
		int addr = brkDwn.destination;
		machine.lc = addr;
	}

	// =========================================================
	// === Redundant code ======================================
	// =========================================================
	// === This code's the same in all instruction classes, ====
	// === But Java lacks the mechanism to allow stuffing it ===
	// === in super() where it belongs. ========================
	// =========================================================

	/**
	 * @see Instruction
	 * @return The static instance of this instruction.
	 */
	public static Instruction getInstance() {
		return staticInstance;
	}

	/** @see assemblernator.Instruction#getOpId() */
	@Override public String getOpId() {
		return opId;
	}

	/** @see assemblernator.Instruction#getOpcode() */
	@Override public int getOpcode() {
		return opCode;
	}

	/** @see assemblernator.Instruction#getNewInstance() */
	@Override public Instruction getNewInstance() {
		return new USI_RET();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_RET(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_RET() {}
}

