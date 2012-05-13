package assemblernator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author Eric
 * @date May 12, 2012; 3:59:15 PM
 */
public class LinkerModule {
	Map<TextRecord, ModRecord[]> textMod = new TreeMap<TextRecord, ModRecord[]>();
	List<LinkerRecord> link = new ArrayList<LinkerRecord>();
	String prgname;
	int prgLoadadd;
	int prgTotalLen;
	int prgStart;
	int endRec;
	int endLink;
	int endText;
	int endMod;
	
	public static class TextRecord{
	int assignedLC;
	int instrData;
	char flagHigh;
	char flagLow;
	int modHigh;
	int modLow;
	}
	public static class ModRecord{
	int hex;
	char plusMin;
	char flagAE;
	String linkerLabel;
	char HLS;
	}
	public static class LinkerRecord{
	String entryLabel;
	int entryAddr;
	}
	
	public LinkerModule(InputStream in){
		TextRecord ttemp = new TextRecord();
		ModRecord mtemp = new ModRecord();
		LinkerRecord ltemp = new LinkerRecord();

		//dont know if i need to error check but just in case
		try {
			char header = (char)in.read();
			//header info 
			if(header == 'H'){
				String temp = "";
				boolean run = true;
				while(run){
					char ch = (char)in.read();
					if(ch == ':'){
						run=false;
					}else{
						temp = temp+ch;
					}
				}
				this.prgname = temp;
				
			}
			
			
			//while loop till the end record is hit
				//check for t record or l record
				//if t record check for m record
				//while loop till record other than m is hit
			
			//gets all the end record info
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
