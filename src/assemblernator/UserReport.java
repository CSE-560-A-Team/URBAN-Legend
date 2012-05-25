package assemblernator;

import java.util.Map;
import java.util.TreeMap;

/**
 * A user report for a single linker module.
 * @author Noah
 * @date May 25, 2012; 7:09:53 PM
 */
public class UserReport {
	/**
	 * The type of record being added to.
	 * @author Noah
	 * @date May 25, 2012; 6:50:23 PM
	 */
	public enum AddType {
		HEADER, LINKER, TEXT, END;
	}
	
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
}
