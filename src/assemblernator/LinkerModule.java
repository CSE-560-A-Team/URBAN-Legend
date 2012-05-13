package assemblernator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import assemblernator.LinkerModule.linkerRecord;

/**
 * 
 * @author Eric
 * @date May 12, 2012; 3:59:15 PM
 */
public class LinkerModule {
	Map<textRecord,modRecord[]> textMod = new TreeMap<textRecord,modRecord[]>();
	List<linkerRecord> link = new ArrayList<linkerRecord>();
	String prgname;
	int prgLoadadd;
	int prgTotalLen;
	int prgStart;
	int endRec;
	int endLink;
	int endText;
	int endMod;
	
	public static class textRecord{
	int assignedLC;
	int instrData;
	char flagHigh;
	char flagLow;
	int modHigh;
	int modLow;
	}
	public static class modRecord{
	int hex;
	char plusMin;
	char flagAE;
	String linkerLabel;
	char HLS;
	}
	public static class linkerRecord{
	String entryLabel;
	int entryAddr;
	}
	
	public LinkerModule(InputStream in){
		textRecord ttemp = new textRecord();
		modRecord mtemp = new modRecord();
		linkerRecord ltemp = new linkerRecord();

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
