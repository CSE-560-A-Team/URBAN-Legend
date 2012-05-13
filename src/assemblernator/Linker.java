package assemblernator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 
 * @author Noah
 * @date May 12, 2012; 1:37:15 PM
 */
public class Linker {
	/**
	 * Map of entry label and address.
	 */
	public static Map<String, Integer> linkerTable;
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
		//sort the modules by order of address of modules.
		Arrays.sort(modules);

		if(modules != null) {
			String prgName = modules[0].prgname;
			int offset = 0;
			offsetModules.add(new OffsetModule(modules[0], offset)); //add first LinkingModule.
			//calc offset and adjust prog, linker record addr, and text record addr by offset.
			//add LinkerModule with adjusted addresses to offsetModules.
			for(int i = 0; i < modules.length - 1; ++i) {
				if(modules[i+1].prgLoadadd <= modules[i].prgLoadadd) {
					offset = ((modules[i].prgLoadadd + modules[i].prgTotalLen) - modules[i+1].prgLoadadd + 1);
					modules[i+1].prgLoadadd += offset;
					
					//put offset linker records into linker table.
					for(LinkerModule.LinkerRecord lr : modules[i+1].link) {
						linkerTable.put(lr.entryLabel, lr.entryAddr + offset);
					}
				}
				
				offsetModules.add(new OffsetModule(modules[i+1], offset));
			}
			
			//adjust text records.
			for(OffsetModule offMod : offsetModules) {
				/*
				//offset text records.
				for(LinkerModule.textRecord txtRecord : modules[i+1].textMod.keySet()) {
					txtRecord.assignedLC += offset;
				}
				*/
				
				for(Map.Entry<LinkerModule.TextRecord, LinkerModule.ModRecord[]> textMod 
						: offMod.module.textMod.entrySet()) {
					
				}
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
