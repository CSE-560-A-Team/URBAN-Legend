package simulanator;

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
	public class OpcodeBreakdownOther {
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
	}
	
	/** The bit indicating if the data is a literal. */
	private final int LITERALBIT = 0x01000000;
	/** The bit indicating if the source is not a regular register. */
	private final int SOURCEKBIT = 0x00800000;
	/** The bitmask of the source register index. */
	private final int SOURCEBITS = 0x00700000;
	/** The bit indicating if the dest is not a regular register. */
	private final int DESTINKBIT = 0x00080000;
	/** The bitmask of the dest register index. */
	private final int DESTINBITS = 0x00070000;
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
	public OpcodeBreakdownOther breakDownOther(int instruction) {
		OpcodeBreakdownOther res = new OpcodeBreakdownOther();
		res.literal = (instruction & LITERALBIT) != 0;
		res.sourceKind = ((instruction & SOURCEKBIT) == 0) ? Location.REGISTER
				: ((instruction & SOURCEBITS) == 0) ? Location.MEMORY
						: Location.INDEXREGISTER;
		if (res.sourceKind == Location.MEMORY)
		{
			res.source = instruction & res.destination;
		}

		res.destKind = ((instruction & DESTINKBIT) == 0) ? Location.REGISTER
				: ((instruction & DESTINBITS) == 0) ? Location.MEMORY
						: Location.INDEXREGISTER;

		return res;
	}
}
