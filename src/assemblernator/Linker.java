package assemblernator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import assemblernator.Instruction.ConstantRange;
import static assemblernator.OperandChecker.isValidMem;
import static assemblernator.OperandChecker.isValidLiteral;

/**
 * 
 * @author Noah
 * @date May 12, 2012; 1:37:15 PM
 */
public class Linker {
	/**
	 * Returns the loader header record.
	 * @author Noah
	 * @date May 13, 2012; 1:22:22 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param prgName name of first module's program name.
	 * @param loadAddr the load address of the linked programs.
	 * @param execStart the execution start address of the linked programs.
	 * @param totalLen the total length of the linked programs.
	 * @return a loader file header record.
	 * @specRef LM1
	 */
	public static byte[] LoaderHeader(String prgName, int loadAddr, int execStart, int totalLen) {
		return new byte[0];
	}
	
	/**
	 * 
	 * @author Noah
	 * @date May 13, 2012; 1:43:16 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param addr
	 * @param code
	 * @return
	 * @specRef N/A
	 */
	public static byte[] LoaderText(int addr, int code) {
		return new byte[0];
	}
	
	/**
	 * 
	 * @author Noah
	 * @date May 13, 2012; 1:44:04 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param totalRecords
	 * @param totalTextRecords
	 * @return
	 * @specRef N/A
	 */
	public static byte[] LoaderEnd(int totalRecords, int totalTextRecords) {
		return new byte[0];
	}
	/**
	 * Takes fileNames, the file names of the object files to link, and an out to output 
	 * the load file.
	 * @author Noah
	 * @date May 12, 2012; 3:44:18 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param modules an array of LinkerModules.
	 * @param out output stream to output load file.
	 * @specRef N/A
	 */
	public static void link(LinkerModule[] modules, OutputStream out) {
		/**
		 * 
		 * @author Noah
		 * @date May 12, 2012; 11:05:06 PM
		 */
		final class OffsetModule {
			LinkerModule module;
			int offset;
			
			OffsetModule(LinkerModule module, int offset) {
				this.module = module;
				this.offset = offset;
			}
		}
		
		List<OffsetModule> offsetModules = new ArrayList<OffsetModule>();
		Map<String, Integer> linkerTable = new HashMap<String, Integer>();
		boolean isValid = true;
		//sort the modules by order of address of modules.
		Arrays.sort(modules);

		if(modules.length > 0) {
			int totalLen = modules[0].prgTotalLen;
			int totalRecords = 2;
			int totalTextRecords = 0;
			int execStartAddr = modules[0].prgStart;
			int offset = 0;
			offsetModules.add(new OffsetModule(modules[0], offset)); //add first LinkerModule.
			//calc offset and adjust prog, linker record addr, and text record addr by offset.
			//add LinkerModule with adjusted addresses to offsetModules.
			for(int i = 0; i < modules.length - 1; ++i) {
				if(modules[i+1].prgLoadadd <= modules[i].prgLoadadd) {
					offset = ((modules[i].prgLoadadd + modules[i].prgTotalLen) - modules[i+1].prgLoadadd + 1);
					modules[i+1].prgLoadadd += offset;
					totalLen += modules[i+1].prgTotalLen;
					if(modules[i+1].prgStart > execStartAddr) {
						execStartAddr = modules[i+1].prgStart;
					}
					//put offset linker records into linker table.
					for(LinkerModule.LinkerRecord lr : modules[i+1].link) {
						linkerTable.put(lr.entryLabel, lr.entryAddr + offset);
					}
				}
				
				offsetModules.add(new OffsetModule(modules[i+1], offset));
			}
			
			try {
				//write header record.
				out.write(LoaderHeader(modules[0].prgname, modules[0].prgLoadadd, execStartAddr, totalLen));
				for(OffsetModule offMod : offsetModules) {
					for(Map.Entry<LinkerModule.TextRecord, LinkerModule.ModRecord[]> textMod 
							: offMod.module.textMod.entrySet()) {
						char litBit = IOFormat.formatBinInteger(textMod.getKey().instrData, 2).charAt(7);
						int opcode = textMod.getKey().instrData;
						int mem;
						int adjustVal = 0;
						int mask;
						
						textMod.getKey().assignedLC += offMod.offset; //offset text lc.
						//adjust text mem.
						for(LinkerModule.ModRecord mRec : textMod.getValue()) {
							if(mRec.flagAE == 'E') {
								if(linkerTable.containsKey(mRec.linkerLabel)) {
									adjustVal = linkerTable.get(mRec.linkerLabel);
								} else {
									//error
									continue;
								}
							} else { //'R'
								adjustVal = offMod.offset;
							}
							
							if((mRec.HLS == 'S' && litBit == '0') || (mRec.HLS == 'L')) {
								mask = 0x00000111;
								opcode &= 0x11111000; //zero out mem bits.
							} else if(mRec.HLS == 'S') {
								mask = 0x00001111;
								opcode &= 0x11110000; //zero out mem bits.
							} else { //'H's
								mask = 0x00111000;
								opcode &= 0x11000111; //zero out mem bits.
							}
							
							mem = textMod.getKey().instrData & mask; //unaltered opcode & mask to get mem bits.
							
							//adjust mem
							if(mRec.plusMin == '+') {
								mem += adjustVal; 
								if(litBit == '0') {
									isValid = isValidMem(mem);
								} else {
									isValid = isValidLiteral(mem, ConstantRange.RANGE_16_TC);
								}
							} else {
								mem -= adjustVal;
								if(litBit == '0') {
									isValid = isValidMem(mem);
								} else {
									isValid = isValidLiteral(mem, ConstantRange.RANGE_16_TC);
								}
							}
							
							if(isValid) {
								opcode |= mem; //fill in mem bits.
								textMod.getKey().instrData = opcode;
							}
						}
						
						//write text records.
						if(isValid) {
							out.write(LoaderText(textMod.getKey().assignedLC, textMod.getKey().instrData));
							++totalTextRecords;
						}
					}
					
				}
				//write end record
				out.write(LoaderEnd(totalRecords, totalTextRecords));
				
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Given fileNames, constructs an array of LinkerModules from the object files with
	 * the names in fileNames.
	 * @author Noah
	 * @date May 12, 2012; 4:05:35 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param fileNames file names of object files to read from.
	 * @return an array of LinkerModules representing the object files.
	 * @specRef N/A
	 */
	public static LinkerModule[] getModules(String[] fileNames) {
		LinkerModule[] modules = new LinkerModule[fileNames.length];
		
		for(int fileIndex = 0; fileIndex < fileNames.length; ++fileIndex) {
			try {
				InputStream in = new BufferedInputStream(new FileInputStream(fileNames[fileIndex]));
				for(int i = 0; i < fileNames.length; ++i) {
					modules[i] = new LinkerModule(in);
				}
			} catch(FileNotFoundException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		return modules;
	}
}
