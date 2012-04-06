package assemblernator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import assemblernator.Instruction.Usage;

/**
 * A class representing an assembled urban module.
 * 
 * @author Josh Ventura
 * @date Apr 5, 2012; 7:15:44 PM
 */
public class Module {
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
		 * adds an entry into the symbol table.
		 * @author Noah
		 * @date Apr 6, 2012; 1:56:43 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param instr instruction to add.
		 * @specRef N/A
		 */
		public void addEntry(Instruction instr) {
			symbols.put(instr.label, instr);
		}
		
		/**
		 * Makes the entire symbolTable into a string which can easily printed to the screen
		 */
		@Override
		public String toString() {
			//way of storing each string line of the symbol table
			ArrayList<String> completeTable = new ArrayList<String>();

			//dumps symbol table into a set which can be iterated through
			Set<Entry<String, Instruction>> table = symbols.entrySet();
			Iterator<Entry<String, Instruction>> tableIt = table.iterator();
			
			//loop runs through the whole symbol table
			while (tableIt.hasNext()) { 
				//gets the set values <K,V> stored into a map entry which can be used to get the values/key of K and V
				Map.Entry<String, Instruction> pairs = tableIt.next();
				
		        String label = pairs.getKey();
		        Instruction instr = pairs.getValue();
		        int addr = instr.lc;
		        Usage usage = instr.usage;
		        
		        String oneLine = label + " " + addr + " " + usage;
		     
		        //since equate are the only one with a string in the symbol table i use this to get the value of that string
		        if (usage == Usage.EQUATE) {
		        	Set<Entry<String, String>> operands = instr.operands.entrySet();
		        	Iterator<Entry<String, String>> operandsIt = operands.iterator();
		        	
		        	oneLine = oneLine + operandsIt.next().getValue(); //value of operand not operand Key. 
		        } 
		        
		        oneLine = oneLine + "\n";
		        
		        completeTable.add(oneLine);
		        
		    } 
			
			String megaTable = "";
			//makes one big string out of each line of the symbol table
			while (!completeTable.isEmpty()) {
				megaTable = megaTable + completeTable.remove(0);
			}
			
			return megaTable;
		}
		
		
	}

	/** An array of parsed instructions in the order they appeared in the file. **/
	ArrayList<Instruction> assembly;
	
	/** instance of symbol table */
	SymbolTable symbolTable = new SymbolTable();
	
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
