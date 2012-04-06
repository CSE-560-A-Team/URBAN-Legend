package assemblernator;

import java.lang.reflect.Array;
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
		 * Makes the entire symbolTable into a string which can easily printed to the screen
		 */
		@Override
		public String toString() {
			//way of storing each line of the symbol table
			ArrayList<String> completeTable = new ArrayList<String>();
			//dumps symbol table into a set which can be iterated through
			Set<Entry<String, Instruction>> theTable = symbols.entrySet();
			Iterator<Entry<String, Instruction>> theTableIt = theTable.iterator();
			//loop runs through the whole symbol table
			while (theTableIt.hasNext()) { 
				//gets the set values <K,V> stored into a map entry which can be used to get the values/key of K and V
				Map.Entry<String, Instruction> pairs = (Map.Entry<String, Instruction>)theTableIt.next();
		        String label = pairs.getKey();
		        Instruction theInstruction = pairs.getValue();
		        int theAddr = theInstruction.lc;
		        Enum theUsage = theInstruction.usage;
		        //since equate are the only one with a string in the symbol table i use this to get the value of that string
		        //i had to use another iterator since the operands are stored in a hashmap
		        if (theUsage == Usage.EQUATE){
		        Set<Entry<String, String>> theOperands = theInstruction.operands.entrySet();
				Iterator<Entry<String, String>> lastPart = theOperands.iterator();
				Map.Entry<String, String> theStringNeeded = (Map.Entry<String, String>)lastPart.next();
				String lastColumn = theStringNeeded.getValue();
				//makes one line of the symbol table into a string with spaces in between each column and a new line at the end
				String oneLine = label +" "+ theAddr +" "+ theUsage +" "+ lastColumn + '\n';
				//adds string to array to later be turned into one big string
				completeTable.add(oneLine);
		        }else{
		        String oneLine = label +" "+ theAddr +" "+ theUsage + '\n';
				completeTable.add(oneLine);
		        }
		         theTableIt.remove();
		    } 
			String megaTable = "";
			//makes one big string out of each line of the symbol table
			while (!completeTable.isEmpty()){
				megaTable = megaTable + completeTable.remove(0);
			}
			
			return megaTable;
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
