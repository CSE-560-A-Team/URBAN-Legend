package instructions;

import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.ErrorReporting.ErrorHandler;



/**
 * @author Ratul Khosla
 * @date Apr 14, 2012; 5:25:24 PM
 */
public abstract class UIG_ShiftManipulate extends Instruction {
	
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
	public final boolean check(ErrorHandler hErr){
		boolean isValid = true;
		
		//The only possible combination has 2 operands
		if(!(this.operands.size() == 2)){
			isValid = false;
			//error
		}	
		else if(!this.hasOperand("FC")){
				isValid = false;
				//error
		} else if(this.hasOperand("DR")) {
				dest = "DR";
		} else if(this.hasOperand("FR")) {
				origin = "FR";
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
		
		String code = IOFormat.formatBinInteger(this.getOpcode(), 6);
		if(dest == "DR"){
				//format 
		}else if(origin == "FR"){
			// format 
		}
		
		
		return null;
		
	}


/**
 * Invokes parent's constructor.
 * @param opid
 * @param opcode
 */UIG_ShiftManipulate(String opid, int opcode) {
	super(opid, opcode);
}

/**
 * default constructor does nothing.
 */
UIG_ShiftManipulate() {}
}

