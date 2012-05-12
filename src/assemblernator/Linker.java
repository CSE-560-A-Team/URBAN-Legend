package assemblernator;

import java.io.OutputStream;

/**
 * 
 * @author Noah
 * @date May 12, 2012; 1:37:15 PM
 */
public class Linker {
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
		for(int i = 0; i < modules.length; ++i) {
			
		}
		return new LinkerModule[0];
	}
}
