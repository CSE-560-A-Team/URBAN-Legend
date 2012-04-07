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
 * <pre> 
 * {@code assembly = a sequence of Instructions.
 * symbolTable is a sequence of entry's where an entry = (label, address, usage, string);
 * 	for an instruction with the label, 
 * 	the address is the location of the instruction,
 * 	the usage is how the instruction is used,
 * 	and the string is the string of characters that the label is equated to if 
 * 	the opcode of the instruction = EQU or EQUe.
 * startAddr is the integer address at which the module starts.
 * moduleLength is the integer length of the module.}
 * </pre>
 * 
 * Module = (assembly, symbolTable, startAddr, moduleLength).
 * 
 * @author Josh Ventura
 * @date Apr 5, 2012; 7:15:44 PM
 */
public class Module {
	/**
	 * A symbolTable as defined in specifications.
	 */
	private class SymbolTable {
		
		/** 
		 * let (label, Instruction) = p.
		 * symbols is a sorted Map of p's.
		 * Each p is a single entry in SymbolTable, where 
		 * 	label = Instruction.label,
		 * 	address = Instruction.lc, 
		 * 	usage = Instruction.usage,
		 *  and string = the value of the operand in Instruction.
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
		 * String representation of they symbol table.
		 * @author Noah
		 * @date Apr 6, 2012; 8:32:43 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param instr instruction to add.
		 * @return <pre>
		 * {@code let line = a string of character representing a single entry
		 * 	in the symbol table. specifically:
		 * 		label + " " + address + " " + " " + usage + " " + string.
		 * 	each line is unique.
		 * returns a sequence of lines for all the entries in the symbol table.}
		 * </pre>
		 * @specRef N/A
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
	
	/** 
	 * a symbol table.
	 */
	private SymbolTable symbolTable = new SymbolTable();
	
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
	
	/**
	 * 	Adds a single entry into the symbol table given an Instruction. <br>
	 * @author Noah
	 * @date Apr 6, 2012; 7:53:39 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param instr the Instruction entry to add into symbol table.
	 */
	public void addSymbolTableEntry(Instruction instr) {
		symbolTable.addEntry(instr);
	}
	
	/**
	 * 	Returns a the String representation of the symbol table.
	 * @author Noah
	 * @date Apr 7, 2012; 9:33:05 AM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @return <pre>
	 * {@code let line = a string of character representing a single entry
	 * 	in the symbol table. specifically, line =
	 * 		label + " " + address + " " + " " + usage + " " + string.
	 * 	each line is unique.
	 * returns a sequence of lines for all the entries in the symbol table.}
	 * </pre>
	 * @specRef N/A
	 */
	public String getSymbTableStringRep() {
		return symbolTable.toString();
	}
}
