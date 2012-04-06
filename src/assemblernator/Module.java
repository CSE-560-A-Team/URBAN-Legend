package assemblernator;

import java.util.ArrayList;
import java.util.SortedMap;

/**
 * A class representing an assembled urban module.
 * 
 * @author Josh Ventura
 * @date Apr 5, 2012; 7:15:44 PM
 */
public class Module {
	/** An array of parsed instructions in the order they appeared in the file. **/
	ArrayList<Instruction> assembly;

	/** The symbol table for this module. **/
	SortedMap<String, Instruction> symbols;

	/**
	 * The address at which execution will begin; set from the KICKO instruction
	 * or by using the AEXS instruction. Must be between 0 and 1023, according
	 * to specification.
	 * @specRef OB1.5
	 */
	int startAddr;

	/**
	 * The length of this module, in instructions, between 0 and 1023, according
	 * to specification.
	 * @specRef OB1.3
	 */
	int moduleLength;
}
