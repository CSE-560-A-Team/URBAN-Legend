package simulanator;

import static assemblernator.ErrorReporting.makeError;

/**
 * @author Josh Ventura
 * @date May 12, 2012; 5:32:58 PM
 */
public class Deformatter {
	/**
	 * @author Josh Ventura
	 * @date May 12, 2012; 5:42:34 PM
	 */
	public enum Location {
		/** A normal register. */
		REGISTER,
		/** An index register. */
		INDEXREGISTER,
		/** A memory address. */
		MEMORY
	}

	/**
	 * @author Josh Ventura
	 * @date May 12, 2012; 5:42:31 PM
	 */
	public static class OpcodeBreakdownOther {
		/** The format bit; true if we are using the memory-memory format. */
		public boolean format;
		/** True if the data field is a literal, false if it is an address. */
		public boolean literal;
		/** The kind of the source. */
		public Location sourceKind;
		/** The source register index or memory address. */
		public int source;
		/** True if the data field is a literal, false if it is an address. */
		public Location destKind;
		/** The destination register index or memory address. */
		public int destination;
		/** The index register of whatever memory is to be read. */
		public int memIXR = 0;
		/** The number of words specified */
		public int numWords = 0;

		/**
		 * Write a word to the machine according to the instruction broken down
		 * in this.
		 * 
		 * @author Josh Ventura
		 * @date May 12, 2012; 7:15:02 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param word
		 *            The word to put.
		 * @param mach
		 *            The Machine whose memory will be written to.
		 * @specRef N/A
		 */
		public void putToDest(int word, Machine mach) {
			switch (destKind) {
			case INDEXREGISTER:
				if (destination < 1 || destination > 7)
					mach.hErr.reportError(makeError("runIRegOOR"),
							mach.getLC(), 0);
				mach.setIndexRegister(destination, word);
				break;
			case REGISTER: // This should have come from three bits, but...
				if (destination < 0 || destination > 7)
					mach.hErr.reportError(makeError("runIRegOOR"),
							mach.getLC(), 0);
				mach.setRegister(destination, word);
				break;
			case MEMORY:
				if (destination < 0 || destination > 4095)
					mach.hErr.reportError(makeError("runMemOOR"), mach.getLC(),
							0);
				mach.setMemory(
						(memIXR == 0 ? destination : destination
								+ mach.getIndexRegister(memIXR)), word);
				break;
			default:
				mach.hErr.reportError(
						"Simulation error: Invalid destination set...", 0, 0);
			}
		}

		/**
		 * Read a word from the machine according to the instruction broken down
		 * in this.
		 * 
		 * @author Josh Ventura
		 * @date May 12, 2012; 7:15:02 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param mach
		 *            The Machine whose memory will be written to.
		 * @return The word that was read.
		 * @specRef N/A
		 */
		public int readFromSource(Machine mach) {
			switch (sourceKind) {
			case INDEXREGISTER:
				if (source < 1 || source > 7)
					mach.hErr.reportError(makeError("runIRegOOR"),
							mach.getLC(), 0);
				return mach.getIndexRegister(source);
			case REGISTER: // This should have come from three bits, but...
				if (source < 0 || source > 7)
					mach.hErr.reportError(makeError("runIRegOOR"),
							mach.getLC(), 0);
				return mach.getRegister(source);
			case MEMORY:
				if (literal)
					return source;
				if (source < 0 || source > 4095)
					mach.hErr.reportError(makeError("runMemOOR"), mach.getLC(),
							0);
				return mach.getMemory(memIXR == 0 ? source : source
						+ mach.getIndexRegister(memIXR));
			default:
				mach.hErr.reportError(
						"Simulation error: Invalid source set...",
						mach.getLC(), 0);
				return 0;
			}
		}

		/**
		 * Read a word from the machine according to the instruction broken down
		 * in this.
		 * 
		 * @author Josh Ventura
		 * @date May 12, 2012; 7:15:02 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @param mach
		 *            The Machine whose memory will be written to.
		 * @return The word that was read.
		 * @specRef N/A
		 */
		public int readFromDest(Machine mach) {
			int word;
			switch (destKind) {
			case INDEXREGISTER:
				if (destination < 1 || destination > 7)
					mach.hErr.reportError(makeError("runIRegOOR"),
							mach.getLC(), 0);
				word = mach.getIndexRegister(destination);
				break;
			case REGISTER: // This should have come from three bits, but...
				if (destination < 0 || destination > 7)
					mach.hErr.reportError(makeError("runIRegOOR"),
							mach.getLC(), 0);
				word = mach.getRegister(destination);
				break;
			case MEMORY:
				if (destination < 0 || destination > 4095)
					mach.hErr.reportError(makeError("runMemOOR"), mach.getLC(),
							0);
				return mach.getMemory(memIXR == 0 ? destination : destination
						+ mach.getIndexRegister(memIXR));
			default:
				mach.hErr.reportError(
						"Simulation error: Invalid destination set...",
						mach.getLC(), 0);
				word = 0;
			}
			return word;
		}

		/**
		 * @author Josh Ventura
		 * @date May 24, 2012; 10:55:28 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @param m The machine to access.
		 * @return The effective destination address.
		 * @specRef N/A
		 */
		public int getEffectiveDestAddress(Machine m) {
			if (destKind != Location.MEMORY) {
				m.hErr.reportError(makeError("runInsNotMemory"), -1, -1);
				return -1;
			}
			return memIXR != 0 ? destination + m.getIndexRegister(memIXR) : destination;
		}
		
		/**
		 * @author Josh Ventura
		 * @date May 24, 2012; 10:55:28 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @param m The machine to access.
		 * @return The effective destination address.
		 * @specRef N/A
		 */
		public int getEffectiveSrcAddress(Machine m) {
			if (sourceKind != Location.MEMORY) {
				m.hErr.reportError(makeError("runInsNotMemory"), -1, -1);
				return -1;
			}
			return memIXR != 0 ? source + m.getIndexRegister(memIXR) : source;
		}
	}

	/** The bit indicating the format used. */
	private static final int FORMATBIT = 0x02000000;
	/** The bit indicating if the data is a literal. */
	private static final int LITERALBIT = 0x01000000;
	/** The bit indicating if the source is not a regular register. */
	private static final int SOURCEKBIT = 0x00800000;
	/** The bitmask of the source register index. */
	private static final int SOURCEBITS = 0x00700000;
	/** The bitmask of the dest register index. */
	private static final int SOURCEOFFS = 20;
	/** The bit indicating if the dest is not a regular register. */
	private static final int DESTINKBIT = 0x00080000;
	/** The bitmask of the dest register index. */
	private static final int DESTINBITS = 0x00070000;
	/** The bitmask of the dest register index. */
	private static final int DESTINOFFS = 16;
	/** The bitmask of all data field bits. */
	private static final int ALLDATABITS = 0x0000FFFF;
	/** The bitmask of the address bits. */
	private static final int ADDRESSBITS = 0x00000FFF;
	/** The bitmask of the address index register bits. */
	private static final int ADDRESSIXRBITS = 0x00007000;
	/** The offset of the address index register bits. */
	private static final int ADDRESSIXROFFS = 12;

	/** Dual-memory high-order address bitmask. */
	private static final int DUALMEMHIADDRBITS = 0x00FFF000;
	/** Dual-memory high-order address offset. */
	private static final int DUALMEMHIADDROFFS = 12;
	/** Dual-memory low-order address bitmask. */
	private static final int DUALMEMLOADDRBITS = 0x00000FFF;
	/** Dual-memory low-order address offset. */
	private static final int DUALMEMLOADDROFFS = 0;

	/**
	 * @author Josh Ventura
	 * @date May 12, 2012; 5:54:26 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param instruction
	 *            The instruction to break down.
	 * @return The breakdown of this instruction as formatOther.
	 * @specRef N/A
	 */
	public static OpcodeBreakdownOther breakDownOther(int instruction) {
		OpcodeBreakdownOther res = new OpcodeBreakdownOther();
		res.format = (instruction & FORMATBIT) != 0;
		res.literal = (instruction & LITERALBIT) != 0;
		if (!res.format) {
			res.sourceKind = ((instruction & SOURCEKBIT) == 0) ? Location.REGISTER
					: ((instruction & SOURCEBITS) == 0) ? Location.MEMORY
							: Location.INDEXREGISTER;

			res.destKind = ((instruction & DESTINKBIT) == 0) ? Location.REGISTER
					: ((instruction & DESTINBITS) == 0) ? Location.MEMORY
							: Location.INDEXREGISTER;

			if (res.sourceKind == Location.MEMORY)
				if (res.literal)
					res.source = instruction & ALLDATABITS;
				else {
					res.source = instruction & ADDRESSBITS;
					res.memIXR = (instruction & ADDRESSIXRBITS) >> ADDRESSIXROFFS;
				}
			else
				res.source = (instruction & SOURCEBITS) >> SOURCEOFFS;

			if (res.destKind == Location.MEMORY)
				if (res.literal)
					res.destination = instruction & ALLDATABITS;
				else {
					res.destination = instruction & ADDRESSBITS;
					res.memIXR = (instruction & ADDRESSIXRBITS) >> ADDRESSIXROFFS;
				}
			else
				res.destination = (instruction & DESTINBITS) >> DESTINOFFS;
		}
		else {
			res.sourceKind = Location.MEMORY;
			res.destKind = Location.MEMORY;
			res.source = (instruction & DUALMEMHIADDRBITS) >> DUALMEMHIADDROFFS;
			res.destination = instruction
					& DUALMEMLOADDRBITS >> DUALMEMLOADDROFFS;
		}


		return res;
	}
}
