package assemblernator;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Assembler class parses a file into a Module object.
 * @author Noah
 * @Date Apr 5, 2012 7:48:36PM
 *
 */
public class Assembler {
	/** Map of opId's to static Instructions*/
	public static Map<String, Instruction> instructions = new HashMap<String, Instruction>();
	/** Map of opCodes to static Instructions*/ 
	public static  Map<Integer, Instruction> byteCodes = new HashMap<Integer, Instruction>();
	/** version of Assembler*/
	public static final int VERSION = 1;
	/** set of operand keywords. */
	public static Set<String> keyWords = new HashSet<String>();
	
	/**
	 * Default constructor.  Adds keywords to set of keywords.
	 */
	Assembler() {
		keyWords.add("DM");
		keyWords.add("DR");
		keyWords.add("DX");
		keyWords.add("EX");
		keyWords.add("FC");
		keyWords.add("FL");
		keyWords.add("FM");
		keyWords.add("FR");
		keyWords.add("FS");
		keyWords.add("FX");
		keyWords.add("LR");
		keyWords.add("NW");
		keyWords.add("ST");
	}
	
	/**
	 * Parses a file into a Module.
	 * @author Noah
	 * @date Apr 5, 2012; 7:33:45 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param file source code for module.
	 * @return <pre>
	 * {@code let line = a line of characters in a file.
	 * Instruction i = Instruction.parse(line);
	 * startOp = first line of file = name * "KICKO" * "FC:" * address,
	 * 	where name is a string of characters representing the name of the module, 
	 * 		and address is a string of characters representing a memory address.
	 * module = sub-string from programName to keyword "END".
	 * returns Module m, where for all lines in file,
	 * 	m.assembly = sequence of i;
	 * 	m.symbols = Map of (i.label, i);
	 * 	m.programName = name;
	 * 	m.startAddr = number of modules from start of file.
	 *  m.moduleLength = length in lines of module;
	 *  m.execStart = address;}
	 *  </pre>
	 * @specRef N/A
	 */
	public final Module parseFile(File file) {
		Module module = new Module();
		
		
		try {
			Scanner fileScan = new Scanner(file);
			fileScan.useDelimiter(";");
			
			while (fileScan.hasNext()) {
				String line = fileScan.nextLine();
			
				
				
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		return new Module();
	}
	
}
