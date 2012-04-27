package assemblernator;

/**
 * 
 * @author Noah
 * @date Apr 27, 2012; 6:15:53 PM
 */
public class InstructionFormatter {
	/**
	 * @author Ratul Khosla
	 * @date Apr 27, 2012; 6:20:32 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param instr 
	 * 			the instruction.
	 * @return assemble
	 * 				the assembled binary code for the instruction.
	 * @specRef N/A
	 */
	public static int [] formatOther(Instruction instr) {
		String code = IOFormat.formatBinInteger(instr.getOpcode(), 6); //011000
		
		code = code + "0";// order bit is always 0 for shift/manipulate instructions.
		
		String reg = "000"; //default value of register bits if registers are not used.
		String index = "000"; //default value of index bits if index registers are not used. 
		String mem; 
		String lit = "0";
		String stack = "0";
		int[] assembled = new int[1];
			
		
		if(instr.hasOperand("DX")) { 
			index = IOFormat.formatBinInteger(instr.getOperandData("DX").value,3);
		} else if(instr.hasOperand("DR")) { 
			reg = IOFormat.formatBinInteger(instr.getOperandData("DR").value,3);
		} else if(instr.hasOperand("FX")) { 
			index = IOFormat.formatBinInteger(instr.getOperandData("FX").value,3);
		} else if(instr.hasOperand("FR")) { 
			reg = IOFormat.formatBinInteger(instr.getOperandData("FR").value,3);
		} else if(instr.hasOperand("ST")) { 
			stack = IOFormat.formatBinInteger(instr.getOperandData("ST").value,1);
		} else if(instr.hasOperand("FL")) { 
			lit = IOFormat.formatBinInteger(instr.getOperandData("FL").value,1);
		}


		code = code + reg;
		code = code + index;//add index register bits.
		code = code + IOFormat.formatBinInteger(instr.getOperandData("NW").value,4); //add number of word bits.
		code = code + stack;//for stack bits
		code = code + lit; //for literal bits 	
		
		if(instr.hasOperand("DM")) { 
			mem = IOFormat.formatBinInteger(instr.getOperandData("DM").value,13);
		} else if(instr.hasOperand("FM")) { 
			mem = IOFormat.formatBinInteger(instr.getOperandData("FM").value,13);
		} else {
			mem = IOFormat.formatBinInteger(instr.getOperandData("FC").value,13);
		}		
		code = code + mem; //concat mem bits.
		assembled[0] = Integer.parseInt(code, 2); //parse as a binary integer.
		
		return assembled;
	}
	
	/**
	 * 
	 * @author Noah
	 * @date Apr 27, 2012; 6:17:09 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param instr
	 * @return
	 * @specRef N/A
	 */
	public static int [] formatHaltDump(Instruction instr) {
		return new int[1];
	}
	
	/**
	 * @author Ratul Khosla
	 * @date Apr 27, 2012; 6:35:33 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param instr 
	 * 				the instruction
	 * @return assemble
	 * 				the binary code of the instruction. 
	 * @specRef N/A
	 */
	public static int [] formatClear(Instruction instr) {
		String code = IOFormat.formatBinInteger(instr.getOpcode(), 6); //011000
		
		code = code + "00 0000 0000 0000 0000 0000 0000";// order bit is always 0 for shift/manipulate instructions.
		
		int[] assembled = new int[1];
		assembled[0] = Integer.parseInt(code, 2); //parse as a binary integer.
		
		return assembled;	}
	
	/**
	 * 
	 * @author Noah
	 * @date Apr 27, 2012; 6:16:53 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param instr
	 * @return
	 * @specRef N/A
	 */
	public static int [] formatInput(Instruction instr) {
		
		return new int[1];
	}
	
	/**
	 * 
	 * @author Noah
	 * @date Apr 27, 2012; 6:16:56 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param instr
	 * @return
	 * @specRef N/A
	 */
	public static int [] formatOutput(Instruction instr) {
		return new int[1];
	}
}
