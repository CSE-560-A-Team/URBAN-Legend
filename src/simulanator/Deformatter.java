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
	enum Location {
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
	class OpcodeBreakdownOther {
		/** True if the data field is a literal, false if it is an address. */
		boolean literal;
		/** The kind of the source. */
		Location sourceKind;
		/** The source register index or memory address. */
		int source;
		/** True if the data field is a literal, false if it is an address. */
		Location destKind;
		/** The destination register index or memory address. */
		int destination;
	}

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
	OpcodeBreakdownOther breakDownOther(int instruction) {
		OpcodeBreakdownOther res = new OpcodeBreakdownOther();
		res.literal = (instruction & 0x01000000) != 0;
		res.sourceKind = ((instruction & 0x00800000) == 0) ? Location.REGISTER
				: ((instruction & 0x00700000) == 0) ? Location.MEMORY
						: Location.INDEXREGISTER;
		if (res.sourceKind == Location.MEMORY)
		{
			//res.source = instruction & 
		}

		res.destKind = ((instruction & 0x00080000) == 0) ? Location.REGISTER
				: ((instruction & 0x00070000) == 0) ? Location.MEMORY
						: Location.INDEXREGISTER;

		return res;
	}
}
