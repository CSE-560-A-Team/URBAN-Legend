package assemblernator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import assemblernator.Instruction.Operand;
import assemblernator.Instruction.Usage;

/**
 * A class representing an assembled urban module.
 * 
 * <pre>
 * {@code assembly = a sequence of Instructions.
 * symbolTable is a Module.SymbolTable
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
	 * <pre>
	 * symbolTable is a sequence of entry's where an entry = (label, address, usage, string);
	 * 	for an instruction with the label, 
	 * 	the address is the location of the instruction,
	 * 	the usage is how the instruction is used,
	 * 	and the string is the string of characters that the label is equated to if 
	 * 	the opcode of the instruction = EQU or EQUe.
	 * SymbolTable.symbols union SymbolTable.extEntSymbols = SymbolTable.
	 * </pre>
	 */
	public static class SymbolTable implements
			Iterable<Map.Entry<String, Instruction>> {

		/**
		 * let (label, Instruction) = p.
		 * symbols is a sorted Map of p's.
		 * Each p is a single entry in SymbolTable, where
		 * label = Instruction.label,
		 * address = Instruction.lc,
		 * usage = Instruction.usage,
		 * and string = the value of the operand in Instruction.
		 * 
		 * does not include Instructions extEntInstr, where instr.opID = "EXT" or "ENT".
		 */
		private SortedMap<String, Instruction> symbols = new TreeMap<String, Instruction>(
				String.CASE_INSENSITIVE_ORDER);

		/**
		 * let (label, Instruction) = p.
		 * symbols is a sorted Map of p's.
		 * Each p is a single entry in SymbolTable, where
		 * label = Instruction.label,
		 * address = Instruction.lc,
		 * usage = Instruction.usage,
		 * and string = the value of the operand in Instruction.
		 * 
		 * only includes Instructions extEntInstr, where instr.opID = "EXT" or "ENT".
		 */
		private Map<String, Instruction> extEntSymbols = new TreeMap<String, Instruction>();
		
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
			//keep track of instructions w/ opID "ENT" and "EXT" separately.
			if (instr.getOpId().equalsIgnoreCase("ENT") || instr.getOpId().equalsIgnoreCase("EXT")) {
				//put each operand as a separate entry into the symbol table.
				for (int i = 1; i < instr.countOperand("LR") + 1; i++) { 
					extEntSymbols.put(instr.getOperand("LR", i), instr);
				}
			} else {
				symbols.put(instr.label, instr);
			}

		}

		/**
		 * Returns a reference to the Instruction w/ the label given
		 * from the symbol table.
		 * Assumes label is in symbol table.
		 * @author Noah
		 * @date Apr 7, 2012; 10:22:52 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param label
		 *            label of Instruction to get reference to from symbol <br>
		 *            table.
		 * @return Instruction with label, label from symbol table.
		 * @specRef N/A
		 */
		public Instruction getEntry(String label) {
			Instruction entry;
			
			if(symbols.containsKey(label)) {
				entry = symbols.get(label);
			} else { 
				entry = extEntSymbols.get(label);
			}
			return entry;
		}

		/**
		 * provides an Iterator over the elements of the symbol table.
		 * 
		 * @author Noah
		 * @date Apr 7, 2012; 4:19:44 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @return an Iterator over elements of symbol table.
		 * @specRef N/A
		 */
		@Override 
		public Iterator<Map.Entry<String, Instruction>> iterator() {
			List<Map.Entry<String, Instruction>> combinedSymbols = new ArrayList<Map.Entry<String, Instruction>>();
			combinedSymbols.addAll(symbols.entrySet()); //combine
			combinedSymbols.addAll(extEntSymbols.entrySet()); //combine
			
			return combinedSymbols.iterator();
		}


		/**
		 * String representation of the symbol table.
		 * 
		 * @author Eric
		 * @date Apr 6, 2012; 8:58:56 AM
		 * @modified Apr 9, 11:23:41 AM; combined separate maps into one symbol table, <br>
		 * 				and made a comparator to sort. <br> -Noah
		 * 			 Apr 7, 1:26:50 PM; added opcode to lines. - Noah <br>
		 *           Apr 6, 11:02:08 AM; removed remove() call to prevent
		 *           destruction of symbol table, <br>
		 *           also, cleaned code up. -Noah<br>
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @return <pre>
		 * {@code let line = a string of character representing a single entry
		 * 	in the symbol table, concatenated w/ the opcode of the instruction with
		 * 	the label in hex format, uppercased.  specifically:
		 * 		label + "\t" + address + "\t" + "\t" + usage + "\t" + string + "\n".
		 * 	each line is unique.
		 * returns "Label:\tLC:\tUsage:\tEquString:\n" + a sequence of lines for all the entries in the symbol table.}
		 * </pre>
		 * @specRef N/A
		 */
		@Override 
		public String toString() {
			/**
			 * Compares two Map.Entry's.
			 * @author Noah
			 * @date Apr 9, 2012; 3:38:54 AM
			 */
			final class MapEntryComparator implements Comparator <Map.Entry<String, Instruction>> {
				@Override public int compare(Map.Entry<String, Instruction> o1, Map.Entry<String, Instruction> o2) {
					return String.CASE_INSENSITIVE_ORDER.compare(o1.getKey(), o2.getKey()); //same ordering as values.
				}
			}
			
			List<Map.Entry<String, Instruction>> combinedSymbols = new ArrayList<Map.Entry<String, Instruction>>();
			combinedSymbols.addAll(symbols.entrySet()); //combine
			combinedSymbols.addAll(extEntSymbols.entrySet()); //combine
			
			Collections.sort(combinedSymbols, new MapEntryComparator()); //sort
					
			// way of storing each line of the symbol table
			List<String> completeTable = new ArrayList<String>();

			// iterator over elements of set of label, Instruction pairs.
			Iterator<Entry<String, Instruction>> tableIt = combinedSymbols.iterator();

			String megaTable = "Label:\tLC:\tUsage:\tEquString:\n";
			// loop runs through the whole symbol table
			while (tableIt.hasNext()) {
				// gets the set values <K,V> stored into a map entry which can
				// be used to get the values/key of K and V
				Map.Entry<String, Instruction> entry = tableIt.next();

				String label = entry.getKey();
				Instruction instr = entry.getValue();
				String addr = IOFormat.formatHexInteger(instr.lc, 4);
				Usage usage = instr.usage;

				String oneLine = label + "\t " + addr + "\t" + usage;

				// since equate are the only one with a string in the symbol
				// table i use this to get the value of that string
				if (usage == Usage.EQUATE) {
					// gets iterator over set of operands.
					Iterator<Operand> operandsIt = instr.operands.iterator();

					if (operandsIt.hasNext()) {
						// only one operand if usage = EQUATE.
						oneLine = oneLine + "\t" + operandsIt.next().expression;
					}
				}

				oneLine = oneLine + "\n";

				completeTable.add(oneLine);

			}

			// makes one big string out of each line of the symbol table
			while (!completeTable.isEmpty()) {
				megaTable = megaTable + completeTable.remove(0);
			}

			return megaTable;
		}


	}

	/** An array of parsed instructions in the order they appeared in the file. **/
	List<Instruction> assembly = new ArrayList<Instruction>();

	/**
	 * a symbol table.
	 */
	private SymbolTable symbolTable = new SymbolTable();

	/**
	 * The address at which execution will begin; set from the KICKO instruction
	 * or by using the AEXS instruction. Must be between 0 and 1023, according
	 * to specification.
	 * 
	 * @specRef OB1.5
	 */
	int startAddr;

	/**
	 * The length of this module, in instructions, between 0 and 1023, according
	 * to specification.
	 * 
	 * @specRef OB1.3
	 */
	int moduleLength;

	/**
	 * Returns a reference to the symbol table.
	 * 
	 * @author Noah
	 * @date Apr 7, 2012; 10:24:23 AM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @return this.symbolTable
	 * @specRef N/A
	 */
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	/**
	 * Evaluates an expression in terms of constants and EQU directives.
	 * 
	 * @author Noah
	 * @date Apr 8, 2012; 1:34:17 AM
	 * @modified Apr 8, 2012; 11:44:46 PM: Changed to support all possible - Josh
	 *           operands to EQU.
	 *           Apr 12, 2012; 8:50:02 PM: modified for readability. - Noah
	 *           Apr 13, 2012; 11:23:33 PM: modified for correctness - Noah
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param exp
	 *            The expression to be evaluated.
	 * @return The value of the expression.
	 * @specRef N/A
	 */
	public int evaluate(String exp) {
		int value = 0;
		exp = exp.trim(); //trim off leading and trailing white-space.
		if (IOFormat.isValidLabel(exp)) { //FC expressions are not valid labels.
			Instruction i = symbolTable.getEntry(exp);  
			if(i.hasOperand("FC")) {
				value = evaluate(i.getOperand("FC"));
			} else if(i.hasOperand("FM")) {
				value = i.lc;
			} else if(i.hasOperand("LR")){
				value = evaluate(i.getOperand("LR"));
			} else { //no valid operand
				throw new NullPointerException("No correct operand to EQU at line " + i.lineNum + "!");
			}
		} else { //exp is a FC expression.
			value = Integer.parseInt(exp);
		}

		return value;
	}


	/**
	 * Returns a string representation of Module.
	 * 
	 * @author Noah
	 * @date Apr 8, 2012; 12:26:15 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @return <pre>
	 * {@code symbTableStr is the string representation of the symbol table. 
	 * assemblyStr the string representation of assembly;
	 * 	The string representation of assembly is a sequence of instrBreaks,
	 * 		where for an Instruction instr in assembly, the corresponding 
	 * 			instrBreak = origSourceLine + "Line number: " + instr.LineNum + " " + LC: " + lc + " " + "Label: " + label + ",\n"
	 * 			+ "instruction/Directive: " + instr.getOpID() + " " + Binary Equivalent: " + binEquiv + "\n" 
	 * 			+ "operand " + i + operandKeyWord + ":" + operandValue;
	 * 				where if instr does not have a label, then label = "", 
	 * 					else label = instr.label, and
	 * 				if instr is a directive and thus has no opcode, then binEquiv = "------", 
	 * 					else binEquiv = instr.opcode in binary format, and
	 * 				i represents the ith operand of instr, and
	 * 					operandKeyword = the key word for the ith operand;
	 * 					operandValue = the value associated w the operand with operandKeyword keyword for the ith operand, and
	 * 				origSourceLine = instr.origSrcLine,
	 * 				and lc = instr.lc displayed in hexadecimal w/ 4 bits.
	 * 	
	 * returns "Symbol Table:\n" + symbTableStr + "\nInstruction breakdowns:\n" + assemblyStr;}
	 * </pre>
	 * 
	 * @specRef N/A
	 */
	@Override public String toString() {
		String rep = "Symbol Table:\n" + symbolTable.toString()
				+ "\nInstruction breakdowns:\n";
		Iterator<Instruction> assemblyIt = assembly.iterator();

		while (assemblyIt.hasNext()) {
			Instruction instr = assemblyIt.next();

			rep = rep + "original source line: " + instr.origSrcLine + "\n";

			//opcode.
			String binEquiv = IOFormat.formatBinInteger(instr.getOpcode(), 6); 
			String label = instr.label;
			String lc = IOFormat.formatHexInteger(instr.lc, 4);

			// instr is a directive and thus has no opcode.
			if (instr.getOpcode() == 0xFFFFFFFF) {
				binEquiv = "------"; // so binary equivalent is non-existant.
			}

			// if the instruction has no label
			if (instr.label == null) {
				label = ""; // no label to print.
				// also, can't print "------" b/c label may be "------".
			}


			String instrBreak = "Line number: " + instr.lineNum + " " + "LC: "
					+ lc + " " + "Label: " + label + ",\n"
					+ "instruction/Directive: " + instr.getOpId() + " "
					+ "Binary Equivalent: " + binEquiv + "\n";

			Iterator<Operand> operandIt = instr.operands.iterator();

			int i = 1;
			while (operandIt.hasNext()) {
				Operand oprnd = operandIt.next();

				instrBreak = instrBreak + "Operand " + i + ": " + oprnd.operand
						+ ":" + oprnd.expression + "\n";

				i++;
			}

			instrBreak = instrBreak + "\n";

			rep = rep + instrBreak;
		}

		return rep;
	}
}
