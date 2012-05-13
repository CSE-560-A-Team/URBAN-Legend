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
	String date;
	int version;
	int endRec;
	int endLink;
	int endText;
	int endMod;
	
	public static class TextRecord{
	int assignedLC;
	String instrData;
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
				String pName = "";
				boolean run = true;
				in.read();
				while(run){
					char ch = (char)in.read();
					if(ch == ':'){
						run=false;
					}else{
						pName = pName+ch;
					}
				}
				this.prgname = pName;
				run=true;
				String lc = "";
				while(run){
					char ch = (char)in.read();
					if(ch == ':'){
						run=false;
					}else{
						lc = lc+ch;
					}
				}
				this.prgLoadadd = Integer.parseInt(lc, 16);
				run=true;
				String totalLength = "";
				while(run){
					char ch = (char)in.read();
					if(ch == ':'){
						run=false;
					}else{
						totalLength = totalLength+ch;
					}
				}
				this.prgTotalLen = Integer.parseInt(totalLength, 16);
				run=true;
				String start = "";
				while(run){
					char ch = (char)in.read();
					if(ch == ':'){
						run=false;
					}else{
						start = start+ch;
					}
				}
				this.prgStart=Integer.parseInt(start, 16);
				run=true;
				String date = "";
				int counter = 0;
				while(run){
					char ch = (char)in.read();
					if(ch == ':' && counter == 2){
						run=false;
					}else{
						if(ch == ':'){
							counter++;
						}
						date= date+ch;
					}
				}
			}
			
			char record = (char)in.read();
			while(record != 'E'){
				if(record == 'T'){
					String tLC = "";
					boolean run = true;
					in.read();
					while(run){
						char ch = (char)in.read();
						if(ch == ':'){
							run=false;
						}else{
							tLC = tLC+ch;
						}
					}
					ttemp.assignedLC = Integer.parseInt(tLC, 16);
					run = true;
					String data = "";
					while(run){
						char ch = (char)in.read();
						if(ch == ':'){
							run=false;
						}else{
							data = data+ch;
						}
					}
					ttemp.instrData = data;
					run = true;
					char highFlag = 0;
					while(run){
						char ch = (char)in.read();
						if(ch == ':'){
							run=false;
						}else{
							highFlag = ch;
						}
					}
					ttemp.flagHigh=highFlag;
					run = true;
					char lowFlag = 0;
					while(run){
						char ch = (char)in.read();
						if(ch == ':'){
							run=false;
						}else{
							lowFlag = ch;
						}
					}
					ttemp.flagLow = lowFlag;
					run = true;
					String modHigh = "";
					while(run){
						char ch = (char)in.read();
						if(ch == ':'){
							run=false;
						}else{
							modHigh = modHigh+ch;
						}
					}
					ttemp.modHigh = Integer.parseInt(modHigh, 16);
					run = true;
					String modLow = "";
					while(run){
						char ch = (char)in.read();
						if(ch == ':'){
							run=false;
						}else{
							modLow = modLow+ch;
						}
					}
					ttemp.modLow = Integer.parseInt(modLow, 16);
					
					
				}else if(record == 'L'){
					
				}
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
