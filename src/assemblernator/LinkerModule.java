package assemblernator;

import static assemblernator.ErrorReporting.makeError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import assemblernator.ErrorReporting.ErrorHandler;
import simulanator.ScanWrap;

/**
 * 
 * @author Eric
 * @date May 12, 2012; 3:59:15 PM
 */
public class LinkerModule implements Comparable<LinkerModule>{

	/**
	 * The type of record being added to.
	 * @author Noah
	 * @date May 25, 2012; 6:50:23 PM
	 */
	private enum AddType {
		HEADER, LINKER, TEXT, END;
	}
	
	/**
	 * A user report for a single linker module.
	 * @author Noah
	 * @date May 25, 2012; 7:09:53 PM
	 */
	private class UserReport {
		/** what type of record being added to */
		public AddType addType = AddType.HEADER;
		/** the object file code for header record + errors in header record. */
		private String headerRecord;
		/** the object file codes for linker records + errors in the linker records. */
		private Map<Integer, String> linkerRecords = new TreeMap<Integer, String>(); 
		/** the object file codes for text records + errors in the text records. */
		private Map<Integer, String> textRecords = new TreeMap<Integer, String>(); 
		/** the object file code for end record + errors in end record. */
		private String endRecord;
		
		/**
		 * Adds content to the contents of record with addType with the key address.
		 * Requires: addType = LINKER, or TEXT.
		 * @author Noah
		 * @date May 25, 2012; 6:50:43 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param address the address of contents.
		 * @param content the string to add to contents.
		 * @specRef N/A
		 */
		public void add(int address, String content) {
			String newContent;
			switch(this.addType) {
			case LINKER:
				newContent = this.linkerRecords.get(address) + content;
				this.linkerRecords.put(address, newContent);
			break;
			case TEXT:
				newContent = this.textRecords.get(address) + content;
				this.textRecords.put(address, newContent);
			}
		}
		
		/**
		 * Adds content to headerRecord or endRecord.
		 * Requires:addType = HEADER or END.
		 * @author Noah
		 * @date May 25, 2012; 6:51:09 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param content content to add.
		 * @specRef N/A
		 */
		public void add(String content) {
			switch(this.addType) {
			case HEADER:
				this.headerRecord = this.headerRecord + content;
			break;
			case END:
				this.endRecord = this.endRecord + content;
			break;
			}
		}
		
		
		/** 
		 * Returns a string representation of the object
		 * 
		 * Requires:addType = HEADER or END.
		 * @author Ratul Khosla
		 * @date May 25, 2012; 8:23:09 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @specRef N/A
		 */
		@Override
		public String toString() {
			String report;
			report = "" + headerRecord;
			
			Map<Integer, String> contents = this.linkerRecords;
			
			for(Map.Entry<Integer, String> record : contents.entrySet()) { 		
				report += record.getValue();  
			}
			
			contents = this.textRecords;
			
			for(Map.Entry<Integer, String> record : contents.entrySet()) { 		
				report += record.getValue();				
			}
			
			report += endRecord; 
			return report;
		}
	}

	/** user report */
	public UserReport userRep = new UserReport();
	/** Contains all the link records. */
	public Map<String, Integer> linkRecord = new HashMap<String, Integer>();
	/** Contains all the mod and text records. */
	public List<TextModRecord> textMod = new ArrayList<TextModRecord>();
	/**Contains all the errors. */
	public List<TextRecord> errorText = new ArrayList<TextRecord>();
	/**Header record string */
	String headerRecord = "";
	/**End record string */
	String endRecord = "";
	/**Link record String */
	public String linkerRecord = "";
	/** Name of program. */
	public String progName;
	/** Program load address. */
	public int loadAddr;
	/** Length of program. */
	public int prgTotalLen;
	/** Start address of the program */
	public int execStart;
	/** Date the object file was made */
	public String date;
	/** Version that was used to make outputfile */
	public int version;
	/** Total number of records */
	public int endRec;
	/** Total number of link records */
	public int endLink;
	/** Total number of text records */
	public int endText;
	/** Total number of mod records */
	public int endMod;
	/** the offset amount from program address. */
	public int offset;
	/** Success failure boolean */
	public boolean success = false;
	/** End reached */
	public boolean done = true;
	/** file name being read. */
	public String filename;
	/**
	 * 
	 * @author Eric
	 * @date May 20, 2012; 11:42:52 PM
	 */
	public static class TextModRecord {
		/**Text Records*/
		public TextRecord text = new TextRecord();
		/**Mod Records*/
		public List<ModRecord> mods = new ArrayList<ModRecord>();
	}

	/**
	 * @author Eric
	 * @date May 13, 2012; 6:07:31 PM
	 */
	public static class TextRecord {
		/** Program assigned LC for text record */
		public int assignedLC;
		/** Assembled instruction */
		public String instrData;
		/** Flag for high order bits */
		public char flagHigh;
		/** Flag for low order bits */
		public char flagLow;
		/** Number of modifications for high order bits */
		public int modHigh;
		/** Number of modifications for low order bits */
		public int modLow;
		/** Text record String */
		public String textRecord = "";
		/** Mod record String */
		public String modRecord = "";
	}

	/**
	 * @author Eric
	 * @date May 13, 2012; 6:07:53 PM
	 */
	public static class ModRecord {
		/** 4 hex nybbles */
		public int hex;
		/** H, L, or S */
		public char HLS;
		/** The middle of modifications records*/
		public List<MiddleMod> midMod = new ArrayList<MiddleMod>();
	}

	/**
	 * @author Eric
	 * @date May 17, 2012; 5:50:57 PM
	 */
	public static class MiddleMod {
		/** Plus or minus sign */
		public char plusMin;
		/** Flag A or E or N */
		public char addrType;
		/** The linkers label for mods */
		public String linkerLabel;
	}

	/**
	 * 
	 * @param read
	 *            the outputFile containing all the records
	 * @param error errorhandler for constructor
	 */
	public LinkerModule(Scanner read, ErrorHandler error){
		// scan wrap
		ScanWrap reader = new ScanWrap(read, error);

		//String used for name
		String ender = "";
		String temp = "";
		String errorMessage = "";

		//Number of records
		int mod = 0;
		int link = 0;
		int text = 0;

		//value checking 
		boolean isValid = true;
		boolean add = true;
		
		//checks for an H
		String check = reader.readString(ScanWrap.notcolon, "loaderNoHeader");
		if (!reader.go("disreguard"))
			return;

		//Runs through header record
		if (check.equalsIgnoreCase("H")) {
			this.progName = reader.readString(ScanWrap.notcolon, "loaderNoName");
			if (!reader.go("disreguard"))
				return;
			this.headerRecord = "H:" + this.progName+ ":";
			this.loadAddr = reader.readInt(ScanWrap.hex4, "loaderHNoAddr", 16);
			if (!reader.go("disreguard"))
				return;
			//error checking
			isValid = OperandChecker.isValidMem(this.loadAddr);
			if(!isValid){
				error.reportError(makeError("invalidValue"),0,0);
				return;
			}
			this.headerRecord = this.headerRecord + ":" +this.loadAddr+ ":";
			this.prgTotalLen = reader
					.readInt(ScanWrap.hex4, "loaderHNoPrL", 16);
			if (!reader.go("disreguard"))
				return;
			//error checking
			isValid = OperandChecker.isValidMem(this.prgTotalLen);
			if(!isValid){
				error.reportError(makeError("invalidValue"),0,0);
				return;
			}
			this.headerRecord = this.headerRecord + ":" +this.prgTotalLen + ":";
			this.execStart = reader.readInt(ScanWrap.hex4, "loaderNoEXS", 16);
			if (!reader.go("disreguard"))
				return;
			//error checking
			isValid = OperandChecker.isValidMem(this.execStart);
			if(!isValid){
				error.reportError(makeError("invalidValue"),0,0);
				return;
			}
			this.headerRecord = this.headerRecord + ":" +this.execStart+ ":";
			this.date = reader.readString(ScanWrap.datep, "loaderHNoDate");
			if (!reader.go("disreguard"))
				return;
			this.headerRecord = this.headerRecord + ":" +this.date+ ":";
			this.version = reader.readInt(ScanWrap.dec4, "loaderHNoVer", 10);
			if (!reader.go("disreguard"))
				return;
			this.headerRecord = this.headerRecord + ":" +this.version+ ":";
			temp = reader.readString(ScanWrap.notcolon, "loaderHNoLLMM");
			if (!reader.go("disreguard"))
				return;
			this.headerRecord = this.headerRecord + ":" +temp+ ":";
			// some kind of error checking
			ender = reader.readString(ScanWrap.notcolon, "loaderNoName");
			if (!reader.go("disreguard"))
				return;
			if(!ender.equals(this.progName)){
				error.reportWarning(makeError("noMatch"), 0, 0);
			this.headerRecord = this.headerRecord + ":" + ender + ":";
			}
		}else{
			error.reportError(makeError("loaderNoHeader"),0,0); 
			return;
		}
		//checks for L or T record
		check = reader.readString(ScanWrap.notcolon, "");
		if (!reader.go("disreguard"))
			return;
		//loops to get all the L and T records from object file
		while (check.equals("L") || check.equals("T")) {
			TextModRecord theRecordsForTextMod = new TextModRecord();
			String entryLabel = "";
			int entryAddr = 0;
			//gets all information from linker record
			if (check.equals("L")) {
				link++;
				entryLabel = reader.readString(ScanWrap.notcolon, "");
				if (!reader.go("disreguard"))
					return;
				this.linkerRecord = this.linkerRecord + ":" + "L:"  + entryLabel + ":";
				entryAddr = reader.readInt(ScanWrap.hex4, "loaderNoEXS",
						16);
				if (!reader.go("disreguard"))
					return;
				//error checking
				isValid = OperandChecker.isValidMem(entryAddr);
				if(!isValid){
					error.reportError(makeError("invalidValue"),0,0);
					return;
				}
				this.linkerRecord = this.linkerRecord + ":"+ entryAddr + ":";
				// some kind of error checking
				ender = reader.readString(ScanWrap.notcolon, "loaderNoName");
//				if (!reader.go("disreguard"))
//					return;
				if(!ender.equals(this.progName)){
					error.reportWarning(makeError("noMatch"), 0, 0);
					this.linkerRecord = this.linkerRecord + ":"+ ender + "\nLabel does not match Program Name \n";
				}else{
					this.linkerRecord = this.linkerRecord + ":"+ ender + "\n";
				}
				linkRecord.put(entryLabel, entryAddr);
				check = reader.readString(ScanWrap.notcolon, "invalidRecord");
				if (!reader.go("disreguard"))
					return;
				//gets all information out of Text record
			} else if (check.equals("T")) {
				text++;
				theRecordsForTextMod.text.assignedLC = reader.readInt(ScanWrap.hex4, "textLC", 16);
				theRecordsForTextMod.text.textRecord = "T:" + theRecordsForTextMod.text.assignedLC + ":";
				if (!reader.go("disreguard")){
					errorMessage = "800: Text record missing valid program assigned location.";
					add = false;
				}
				//error checking
				isValid = OperandChecker.isValidMem(theRecordsForTextMod.text.assignedLC);
				if(!isValid){
					errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
					add = false;
				}
				theRecordsForTextMod.text.instrData = reader.readString(ScanWrap.notcolon, "textData");
				theRecordsForTextMod.text.textRecord = theRecordsForTextMod.text.textRecord + ":" + theRecordsForTextMod.text.instrData+ ":";
				if (!reader.go("disreguard"))
				{
					errorMessage = "801: Text record missing valid assembled instruction/data.";
					add = false;
				}

				theRecordsForTextMod.text.flagHigh = reader.readString(ScanWrap.notcolon, "textStatus").charAt(0);
				theRecordsForTextMod.text.textRecord = theRecordsForTextMod.text.textRecord + ":" + theRecordsForTextMod.text.flagHigh + ":";
				if (!reader.go("disreguard"))
				{
					errorMessage = "802: Text record missing valid status flag.";
					add = false;
				}
				if(!(theRecordsForTextMod.text.flagHigh == 'A' || theRecordsForTextMod.text.flagHigh == 'R' || theRecordsForTextMod.text.flagHigh == 'E' || theRecordsForTextMod.text.flagHigh == 'C')){
					errorMessage = "802: Text record missing valid status flag.";
					add = false;
				}		
				theRecordsForTextMod.text.flagLow = reader.readString(ScanWrap.notcolon, "textStatus").charAt(0);
				theRecordsForTextMod.text.textRecord = theRecordsForTextMod.text.textRecord + ":" + theRecordsForTextMod.text.flagLow + ":";
				if (!reader.go("disreguard"))
				{
					errorMessage = "802: Text record missing valid status flag.";
					add = false;
				}
				if(!(theRecordsForTextMod.text.flagLow == 'A' || theRecordsForTextMod.text.flagLow == 'R' || theRecordsForTextMod.text.flagLow == 'E' || theRecordsForTextMod.text.flagLow == 'C')){
					errorMessage = "802: Text record missing valid status flag.";
					add = false;
				}
				theRecordsForTextMod.text.modHigh = reader.readInt(ScanWrap.notcolon, "textMod", 16);
				theRecordsForTextMod.text.textRecord = theRecordsForTextMod.text.textRecord + ":" + theRecordsForTextMod.text.modHigh + ":";
				if (!reader.go("disreguard"))
				{
					errorMessage = "803: Text record missing valid number of modifications.";
					add = false;
				}
				//check for mod high
				if(theRecordsForTextMod.text.modHigh>16 || theRecordsForTextMod.text.modHigh<0)
				{
					errorMessage = "803: Text record missing valid number of modifications.";
					add = false;
				}
				theRecordsForTextMod.text.modLow = reader.readInt(ScanWrap.notcolon, "textMod", 16);
				theRecordsForTextMod.text.textRecord = theRecordsForTextMod.text.textRecord + ":" + theRecordsForTextMod.text.modLow + ":";
				if (!reader.go("disreguard"))
				{
					errorMessage = "803: Text record missing valid number of modifications.";
					add = false;
				}
				//check for mod low
				if(theRecordsForTextMod.text.modLow>16 || theRecordsForTextMod.text.modLow<0)
				{
					errorMessage = "803: Text record missing valid number of modifications.";
					add = false;
				}
				// some kind of error checking
				ender = reader.readString(ScanWrap.notcolon, "loaderNoName");
				theRecordsForTextMod.text.textRecord = theRecordsForTextMod.text.textRecord + ":" + ender + ":\n" + errorMessage;
				errorMessage = "";
//				if (!reader.go("disreguard"))
//					return;
				if(!ender.equals(this.progName)){
					error.reportWarning(makeError("noMatch"), 0, 0);
				}
				check = reader.readString(ScanWrap.notcolon, "invalidRecord");
				if (!reader.go("disreguard"))
					return;
				//gets all mod records for a text record
				while (check.equals("M")) {
					ModRecord modification = new ModRecord();
					mod++;
					modification.hex = reader.readInt(ScanWrap.hex4, "modHex", 16);
					theRecordsForTextMod.text.modRecord = theRecordsForTextMod.text.modRecord + "M:" + modification.hex + ":";
					if (!reader.go("disreguard"))
					{
						errorMessage = "804: Modification record missing 4 hex nybbles.";
						add = false;
					}
					//error checking
					isValid = OperandChecker.isValidMem(modification.hex);
					if(!isValid){
						errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
						add = false;
					}
					boolean run = true;
					String loop = "";
					boolean firstRun = true;
					while (run) {
						MiddleMod midtemp = new MiddleMod();
						if(firstRun){
						midtemp.plusMin = reader.readString(ScanWrap.notcolon,
								"modPm").charAt(0);
						firstRun=false;
						}else{
						midtemp.plusMin = loop.charAt(0);
						}
						theRecordsForTextMod.text.modRecord = theRecordsForTextMod.text.modRecord +":"+ midtemp.plusMin + ":";
						if (!reader.go("disreguard"))
						{
							errorMessage = "805: Modification record missing plus or minus sign.";
							add = false;
						}
						//error checking
						isValid = OperandChecker.isValidPlusMin(midtemp.plusMin);
						if(!isValid){
							errorMessage = "812: Modification records must contain plus or minus sign.";
							add = false;
						}
						midtemp.addrType = reader.readString(ScanWrap.notcolon,
								"modFlag").charAt(0);
						theRecordsForTextMod.text.modRecord = theRecordsForTextMod.text.modRecord +":"+ midtemp.addrType + ":";
						if (!reader.go("disreguard"))
						{
							errorMessage = "806: Modification record missing correct flag R, E, or N.";
							add = false;
						}
						if(!(midtemp.addrType == 'E' || midtemp.addrType == 'R' || midtemp.addrType == 'N')){
							errorMessage = "806: Modification record missing correct flag R, E, or N.";
							add = false;
						}
						midtemp.linkerLabel = reader.readString(
								ScanWrap.notcolon, "modLink");
						theRecordsForTextMod.text.modRecord = theRecordsForTextMod.text.modRecord +":"+ midtemp.linkerLabel + ":";
						if (!reader.go("disreguard"))
						{
							errorMessage = "807: Modification record missing valid label for address.";
							add = false;
						}
						loop = reader.readString(ScanWrap.notcolon, "modHLS");
						if (!reader.go("disreguard"))
						{
							errorMessage = "808: Modification record missing correct char H, L, or S.";
							add = false;
						}
						if (loop.equals("")) {
							run = false;
						}
						modification.midMod.add(midtemp);
					}
					loop = reader.readString(ScanWrap.notcolon, "modHLS");
					if (!reader.go("disreguard"))
					{
						errorMessage = "808: Modification record missing correct char H, L, or S.";
						add = false;
					}
					modification.HLS = loop.charAt(0);
					theRecordsForTextMod.text.modRecord = theRecordsForTextMod.text.modRecord +":"+ modification.HLS + ":";
					if(!(modification.HLS == 'H' || modification.HLS == 'L' || modification.HLS == 'S')){
						errorMessage = "808: Modification record missing correct char H, L, or S.";
							add = false;
					}
					// some kind of error checking
					ender = reader.readString(ScanWrap.notcolon, "loaderNoName");
					theRecordsForTextMod.text.modRecord = theRecordsForTextMod.text.modRecord +":"+ ender + ":\n" + errorMessage;
//					if (!reader.go("disreguard"))
//						return;
					if(!ender.equals(this.progName)){
						error.reportWarning(makeError("noMatch"), 0, 0);
					}
					theRecordsForTextMod.mods.add(modification);
					check = reader.readString(ScanWrap.notcolon, "invalidRecord");
					if (!reader.go("disreguard"))
					{
						errorMessage = "809: Invalid Record. Records must start with valid record character.";
						add = false;
					}
				}// end of mod record
				if(add){
					textMod.add(theRecordsForTextMod);
				}else{
					errorText.add(theRecordsForTextMod.text);
					add = true;
				}
			}// end of text record
			
		}//end of while loop checking for linking records and text records
		
		//checks for an end record
		if (check.equals("E")) {
			this.endRec = reader.readInt(ScanWrap.hex4, "endRecords", 16);
			this.endRecord = "E:" + this.endRec + ":";
			if (!reader.go("disreguard"))
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
			//error checking
			isValid = OperandChecker.isValidMem(this.endRec);
			if(!isValid){
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
			}
			this.endLink = reader.readInt(ScanWrap.hex4, "endRecords", 16);
			this.endRecord = this.endRecord + ":" + this.endLink + ":";
			if (!reader.go("disreguard"))
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
			//error checking
			isValid = OperandChecker.isValidMem(this.endLink);
			if(!isValid){
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
			}
			this.endText = reader.readInt(ScanWrap.hex4, "endRecords", 16);
			this.endRecord = this.endRecord + ":" + this.endText + ":";
			if (!reader.go("disreguard"))
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
			//error checking
			isValid = OperandChecker.isValidMem(this.endText);
			if(!isValid){
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
			}
			this.endMod = reader.readInt(ScanWrap.hex4, "endRecords", 16);
			this.endRecord = this.endRecord + ":" + this.endMod + ":";
			if (!reader.go("disreguard"))
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";
			//error checking
			isValid = OperandChecker.isValidMem(this.endMod);
			if(!isValid){
				errorMessage = "811: Invalid value. Records values must be from 0 - 4095.";;
			}
			ender = reader.readString(ScanWrap.notcolon, "loaderNoName");
			this.endRecord = this.endRecord + ":" + ender +":\n" + errorMessage;
			//error checking
//			if (!reader.go("disreguard"))
//				return;
			if(!ender.equals(this.progName)){
				error.reportWarning(makeError("noMatch"), 0, 0);
			}

		}else{
			error.reportError(makeError("loaderNoEnd"),0,0); 
			return;
		}

		//warnings for amount of mod text and link records
		if(link != this.endLink){
			error.reportWarning(makeError("linkMatch"), 0, 0);
		}else if(text != this.endText){
			error.reportWarning(makeError("textMatch"), 0, 0);
		}else if(mod != this.endMod){
			error.reportWarning(makeError("modMatch"), 0, 0); 
		}else if((link+text+mod+2) != this.endRec){
			error.reportWarning(makeError("totalMatch"), 0, 0); 
		}
	


		//program ran successful. Checks for more in file
		this.success = true;
		if (read.hasNext()) {
			this.done = false;
		}
	}
	/**
	 * Compares loadAddr of the LinkerModules
	 */
	@Override
	public int compareTo(LinkerModule cmp) {
		if(this.loadAddr > cmp.loadAddr){
			return 1;
		}else if(this.loadAddr < cmp.loadAddr){
			return -1;
		}else{
			return 0;
		}
	}

}
