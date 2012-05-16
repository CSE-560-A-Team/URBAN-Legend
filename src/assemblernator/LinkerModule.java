package assemblernator;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import assemblernator.ErrorReporting.ErrorHandler;

import simulanator.ScanWrap;

/**
 * 
 * @author Eric
 * @date May 12, 2012; 3:59:15 PM
 */
public class LinkerModule {
	
	List<AllData> textModLink = new ArrayList<AllData>();
	/** Name of program. */
	String prgname;
	/** Program load address. */
	int prgLoadadd;
	/** Length of program. */
	int prgTotalLen;
	/** Start address of the program */
	int prgStart;
	/** Date the object file was made */
	String date;
	/** Version that was used to make outputfile */
	int version;
	/** Total number of records */
	int endRec;
	/** Total number of link records */
	int endLink;
	/** Total number of text records */
	int endText;
	/** Total number of mod records */
	int endMod;
	/** the offset amount from program address. */
	int offset;

	/**
	 * @author Eric
	 * @date May 13, 2012; 6:07:31 PM
	 */
	public static class TextRecord {
		/** Program assigned LC for text record */
		int assignedLC;
		/** Assembled instruction */
		String instrData;
		/** Flag for high order bits */
		char flagHigh;
		/** Flag for low order bits */
		char flagLow;
		/** Number of modifications for high order bits */
		int modHigh;
		/** Number of modifications for low order bits */
		int modLow;
	}

	/**
	 * @author Eric
	 * @date May 13, 2012; 6:07:53 PM
	 */
	public static class ModRecord {
		/** 4 hex nybbles */
		int hex;
		/** H, L, or S */
		char HLS;
		/**
		 * The middle of modifications records
		 */
		List<MiddleMod> midMod = new ArrayList<MiddleMod>();
	}
	
	public static class MiddleMod {
		/** Plus or minus sign */
		char plusMin;
		/**  Flag A or E */
		char flagRE;
		/** The linkers label for mods */
		String linkerLabel;
	}

	/**
	 * @author Eric
	 * @date May 13, 2012; 6:08:12 PM
	 */
	public static class LinkerRecord {
		/**  Label of link */
		String entryLabel;
		/** Address of link */
		int entryAddr;
	}
	/**
	 * 
	 */
	public static class AllData{
		/** TextRecord with all ModRecords associated with that text Record. */
		Map<TextRecord, List<ModRecord>> textMod = new TreeMap<TextRecord, List<ModRecord>>();
		/** List of all link records in file. */
		List<LinkerRecord> link = new ArrayList<LinkerRecord>();
	}
	/**
	 * 
	 * @param in
	 *            the outputFile containing all the records
	 */
	public LinkerModule(InputStream in) {
		// temp holders for information
		TextRecord ttemp = new TextRecord();
		List<ModRecord> completeMod = new ArrayList<ModRecord>();
		ModRecord mtemp = new ModRecord();
		MiddleMod midtemp = new MiddleMod();
		LinkerRecord ltemp = new LinkerRecord();
		Scanner read = new Scanner(in).useDelimiter(":");
		
		//scan wrap
		ErrorHandler error = null;
		ScanWrap reader = new ScanWrap(read, error);
		
		while(read.hasNext())
		{
			String check = read.next();
			if(check.equals("H") || check.equals("\nH"))
			{
				this.prgname = reader.readString(ScanWrap.notcolon, "loaderNoName" );
				if(!reader.go("disreguard"))
					break;
				this.prgLoadadd = reader.readInt(ScanWrap.hex4, "loaderHNoAddr", 16);
				if(!reader.go("disreguard"))
					break;
				this.prgTotalLen = reader.readInt(ScanWrap.hex4, "loaderHNoPrL", 16);
				if(!reader.go("disreguard"))
					break;
				this.prgStart = reader.readInt(ScanWrap.hex4, "loaderNoEXS", 16);
				if(!reader.go("disreguard"))
					break;
				this.date = reader.readString(ScanWrap.datep, "loaderHNoDate");
				if(!reader.go("disreguard"))
					break;
				this.version = reader.readInt(ScanWrap.dec4, "loaderHNoVer", 10);
				if(!reader.go("disreguard"))
					break;
				reader.readString(ScanWrap.notcolon, "loaderHNoLLMM");
				if(!reader.go("disreguard"))
					break;
				//some kind of error checking
				reader.readString(ScanWrap.notcolon, "loaderNoName" );
				if(!reader.go("disreguard"))
					break;
			}//end of header record
			
			
			if(check.equals("L") || check.equals("\nL")){
				ltemp.entryLabel = reader.readString(ScanWrap.notcolon, "");
				if(!reader.go("disreguard"))
					break;
				ltemp.entryAddr =reader.readInt(ScanWrap.hex4, "loaderNoEXS", 16);
				if(!reader.go("disreguard"))
					break;
				//some kind of error checking
				reader.readString(ScanWrap.notcolon, "loaderNoName" );
				if(!reader.go("disreguard"))
					break;
			}//end of link record
			
			
			
			if(check.equals("T") || check.equals("\nT")){
				ttemp.assignedLC = reader.readInt(ScanWrap.hex4, "", 16);
				if(!reader.go("disreguard"))
					break;
				ttemp.instrData = reader.readString(ScanWrap.notcolon, "");
				if(!reader.go("disreguard"))
					break;
				ttemp.flagHigh = reader.readString(ScanWrap.notcolon, "").charAt(0);
				if(!reader.go("disreguard"))
					break;
				ttemp.flagLow = reader.readString(ScanWrap.notcolon, "").charAt(0);
				if(!reader.go("disreguard"))
					break;
				ttemp.modHigh = reader.readInt(ScanWrap.notcolon, "", 16);
				if(!reader.go("disreguard"))
					break;
				ttemp.modLow = reader.readInt(ScanWrap.notcolon, "", 16);
				if(!reader.go("disreguard"))
					break;
				//some kind of error checking
				reader.readString(ScanWrap.notcolon, "loaderNoName" );
				if(!reader.go("disreguard"))
					break;
				check = read.next();
				while(check.equals("M") || check.equals("\nM")){
					mtemp.hex = reader.readInt(ScanWrap.hex4, "", 16);
					if(!reader.go("disreguard"))
						break;
					boolean run = true;
					String loop = "";
					while(run){
						midtemp.plusMin = reader.readString(ScanWrap.notcolon, "").charAt(0);
						if(!reader.go("disreguard"))
							break;
						midtemp.flagRE = reader.readString(ScanWrap.notcolon, "").charAt(0);
						if(!reader.go("disreguard"))
							break;
						midtemp.linkerLabel = reader.readString(ScanWrap.notcolon, "");
						if(!reader.go("disreguard"))
							break;
						loop = reader.readString(ScanWrap.notcolon, "");
						if(!reader.go("disreguard"))
							break;
						if(loop.equals("H") || loop.equals("L") || loop.equals("S")){
							run = false;
						}
						mtemp.midMod.add(midtemp);
					}
					mtemp.HLS = loop.charAt(0);
					//some kind of error checking
					reader.readString(ScanWrap.notcolon, "loaderNoName" );
					if(!reader.go("disreguard"))
						break;
					completeMod.add(mtemp);
					check = read.next();
				}//end of mod record
					
			}//end of text record
			
			
			
			
			if(check.equals("E") || check.equals("\nE")){
				this.endRec = reader.readInt(ScanWrap.hex4, "", 16);
				if(!reader.go("disreguard"))
					break;
				this.endLink = reader.readInt(ScanWrap.hex4, "", 16);
				if(!reader.go("disreguard"))
					break;
				this.endText = reader.readInt(ScanWrap.hex4, "", 16);
				if(!reader.go("disreguard"))
					break;
				this.endMod = reader.readInt(ScanWrap.hex4, "", 16);
				if(!reader.go("disreguard"))
					break;
				//some kind of error checking
				reader.readString(ScanWrap.notcolon, "loaderNoName" );
				if(!reader.go("disreguard"))
					break;
				
			}//end of end record
			
			
		}//loop again checking for a H record
	}
		

}


