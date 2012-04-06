package assemblernator;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A class representing an assembled urban module.
 * 
 * @author Josh Ventura
 * @date Apr 5, 2012; 7:15:44 PM
 */
public class Module {
	/** An array of parsed instructions in the order they appeared in the file. **/
	ArrayList<Instruction> assembly;

	/**
	 * Symbol Table.
	 * @author Noah
	 * @date Apr 5, 2012; 10:03:26 PM
	 */
	private class SymbolTable {
		/** 
		 * label = Instruction.label;
		 * a Map of (label, Instruction) sorted according to order of label. 
		 */
		private SortedMap<String, Instruction> symbols =  new TreeMap<String, Instruction>();
		
		/**
		 * 
		 */
		@Override
		public String toString() {
			return "";
		}
		
		
	}

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
