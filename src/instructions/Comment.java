package instructions;

import assemblernator.Instruction;
import assemblernator.Module;

/**
 * Specialized Instruction for full-line comments, since they must be included
 * in the user report.
 * 
 * @author Josh Ventura
 * @date Apr 6, 2012; 11:36:23 AM
 * @specRef S4.1
 */
public class Comment extends Instruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = ";";

	/** This instruction's identifying opcode. */
	private static final int opCode = -1; // Comments do not have an opcode

	/** The static instance for this instruction. */
	static Comment staticInstance = new Comment(true);

	/** The actual text given for this comment */
	private String comment_text = "";

	/** @see assemblernator.Instruction#getNewLC(int,Module) */
	@Override
	public int getNewLC(int lc, Module mod) {
		return lc;
	}

	/** @see assemblernator.Instruction#check() */
	@Override
	public boolean check() {
		return true;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override
	public int[] assemble() {
		return new int[0];
	}

	/** @see assemblernator.Instruction#execute(int) */
	@Override
	public void execute(int instruction) {
	}

	/** @see assemblernator.Instruction#getNewInstance() */
	@Override
	public Instruction getNewInstance() {
		return new Comment();
	}

	/**
	 * Construct a new comment with text.
	 * 
	 * @param c
	 *            The comment text.
	 */
	public Comment(String c) {
		comment_text = c;
	}

	/** Return the text of this comment, preceded by a semicolon. */
	@Override
	public String toString() {
		return "; " + comment_text;
	}

	// =========================================================
	// === Redundant code ======================================
	// =========================================================
	// === This code's the same in all instruction classes, ====
	// === But Java lacks the mechanism to allow stuffing it ===
	// === in super() where it belongs =========================
	// =========================================================

	/**
	 * @see Instruction
	 * @return The static instance of this instruction.
	 */
	public static Instruction getInstance() {
		return staticInstance;
	}

	/** @see assemblernator.Instruction#getOpId() */
	@Override
	public String getOpId() {
		return opId;
	}

	/** @see assemblernator.Instruction#getOpcode() */
	@Override
	public int getOpcode() {
		return opCode;
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private Comment(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private Comment() {
	}
}
