package instructions;

import static assemblernator.ErrorReporting.makeError;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

import simulanator.Machine;

import assemblernator.AbstractInstruction;
import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Module.Value.BitLocation;
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.OperandChecker;

/**
 * The ISRG instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef IA3
 */
public class USI_SND extends AbstractInstruction {
	/**
	 * The operation identifier of this instruction; while comments should not
	 * be treated as an instruction, specification says they must be included in
	 * the user report. Hence, we will simply give this class a semicolon as its
	 * instruction ID.
	 */
	private static final String opId = "SND";

	/** This instruction's identifying opcode. */
	private static final int opCode = 0x04; // 0b000100

	/** The static instance for this instruction. */
	static USI_SND staticInstance = new USI_SND(true);

	/**
	 * Denotes the waveform (type) of our sample.
	 * 
	 * @author Josh Ventura
	 * @date May 5, 2012; 2:03:15 AM
	 */
	enum SampleType {
		/** White noise */
		WHITE(0),
		/** Pink noise */
		PINK(1),
		/** Square wave */
		SQUARE(2),
		/** Saw wave */
		SAW(3),
		/** Sine wave */
		SINE(4),
		/** BOINK! */
		BOINK(5),
		/** Guitar Pluck */
		PLUCK(6);

		/** The integer ID for this sample */
		public int id;

		/**
		 * @param ID
		 *            The ID of this sample type.
		 */
		private SampleType(int ID) {
			id = ID;
		}
	}

	/** An integer ID of our sample type. */
	SampleType sampleType;

	/** @see assemblernator.Instruction#getNewLC(int, Module) */
	@Override public int getNewLC(int lc, Module mod) {
		return lc + 1;
	}

	/** @see assemblernator.Instruction#check(ErrorHandler, Module) */
	@Override public boolean check(ErrorHandler hErr, Module module) {
		if (operands.size() < 3) {
			hErr.reportError(makeError("tooFewOperandsIns", opId), lineNum, 0);
			return false;
		}
		if (operands.size() > 3) {
			hErr.reportError(makeError("extraOperandsIns", opId), lineNum, 0);
			return false;
		}
		Operand op = getOperandData("ST"); // Get Sample Type
		if (op == null) {
			hErr.reportError(
					makeError("instructionMissingOpS", opId, "ST",
							"Sample Type"), lineNum, 0);
			return false;
		}
		if (!OperandChecker.isValidString(op.expression)) {
			hErr.reportError(makeError("STstringCount"), lineNum, -1);
			return false;
		}
		String sample = IOFormat.escapeString(op.expression, lineNum,
				op.valueStartPosition, hErr).toLowerCase();
		switch (sample.charAt(0)) {//@formatter:off
		case 'w': if (sample.equals("white") ) { sampleType = SampleType.WHITE;  break; }
		case 'p': if (sample.equals("pink")  ) { sampleType = SampleType.PINK;   break; }
		          if (sample.equals("pluck") ) { sampleType = SampleType.PLUCK;  break; }
		case 's': if (sample.equals("sine")  ) { sampleType = SampleType.SINE;   break; }
		          if (sample.equals("saw")   ) { sampleType = SampleType.SAW;    break; }
		          if (sample.equals("square")) { sampleType = SampleType.SQUARE; break; }
		case 'b': if (sample.equals("boink") ) { sampleType = SampleType.BOINK;  break; }
		default:  hErr.reportError(makeError("unmatchedStr", sample, "any built-in sample"), lineNum, op.keywordStartPosition);
		} //@formatter:on

		op = getOperandData("FR");
		if (op == null) {
			hErr.reportError(
					makeError("instructionMissingOpS", opId, "FR", "FRequency"),
					lineNum, 0);
			return false;
		}
		op.value = module.evaluate(op.expression, false, BitLocation.Other, hErr, this,
				op.valueStartPosition);
		if (op.value.value < 50 || op.value.value > 20000) {
			hErr.reportError(
					makeError("OORconstant", "FR", opId, "50", "20000"),
					lineNum, op.valueStartPosition);
			return false;
		}

		op = getOperandData("DM");
		if (op == null) {
			hErr.reportError(
					makeError("instructionMissingOpS", opId, "DM",
							"Duration Millis"), lineNum, 0);
			return false;
		}
		op.value = module.evaluate(op.expression, false, BitLocation.Other, hErr, this,
				op.valueStartPosition);
		if (op.value.value < 10 || op.value.value > 10000) {
			hErr.reportError(
					makeError("OORconstant", "DM", opId, "10", "10000"),
					lineNum, op.valueStartPosition);
			return false;
		}

		return true;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return new int[] { (opCode << 26) | (sampleType.id << 23)
				| (getOperandData("FR").value.value << 9)
				| (getOperandData("DM").value.value / 10) };
	}

	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {
		// TODO: IMPLEMENT
	}


	/** Note Lengths. */
	static enum Tone {//@formatter:off
		/** G below C */ GbelowC(196), /** A */ A(220), /** A# */ Asharp(233), /** B */ B(247),
		/** C */ C(262), /** C# */ Csharp(277), /** C */ D(294), /** D# */ Dsharp(311),
		/** E */ E(330), /** F */ F(349), /** F# */ Fsharp(370), /** G */ G(392),
		/** G# */ Gsharp(415), /** F5 */ F5(698), /** A5*/ A5(880), /** C6 */ C6(1046);
		//@formatter:on
		/** Frequency in Hz. */
		public int freq;

		/**
		 * @param x
		 *            Frequency of the note.
		 */
		Tone(int x) {
			freq = x;
		}
	}

	/** Note durations. */
	static enum Duration {
		/** 1/4 note. */
		EIGHTH(125), /** 1/4 note. */
		QUARTER(250), /** 1/2 note. */
		HALF(500), /** 3/4 note. */
		TQ(750), /** Whole note. */
		WHOLE(1000);
		/** Duration in milliseconds. */
		int ms;

		/**
		 * @param x
		 *            The duration, in milliseconds.
		 */
		Duration(int x) {
			ms = x;
		}
	}

	/** Tone + Duration = Note */
	static class Note {
		/** The tone */
		Tone tone;
		/** The duration */
		Duration dur;

		/**
		 * @param t
		 *            Tone.
		 * @param d
		 *            Duration.
		 */
		public Note(Tone t, Duration d) {
			tone = t;
			dur = d;
		}
	}

	/** Test audio. */
	public static void test() {
		byte buf[] = new byte[48000 * 10];
		int bpos = 0;

		Note[] mhall = { new Note(Tone.A5, Duration.EIGHTH),
				new Note(Tone.A5, Duration.QUARTER),
				new Note(Tone.A5, Duration.QUARTER),
				new Note(Tone.F5, Duration.EIGHTH),
				new Note(Tone.A5, Duration.QUARTER),
				new Note(Tone.C6, Duration.HALF), };
		/* { new Note(Tone.D, Duration.TQ),
		 * new Note(Tone.F, Duration.TQ), new Note(Tone.D, Duration.TQ),
		 * new Note(Tone.C, Duration.TQ),
		 * new Note(Tone.A, Duration.WHOLE),
		 * new Note(Tone.A, Duration.HALF), }; */
		/* { new Note(Tone.B, Duration.QUARTER),
		 * new Note(Tone.A, Duration.QUARTER),
		 * new Note(Tone.GbelowC, Duration.QUARTER),
		 * new Note(Tone.A, Duration.QUARTER),
		 * new Note(Tone.B, Duration.QUARTER),
		 * new Note(Tone.B, Duration.QUARTER),
		 * new Note(Tone.B, Duration.HALF),
		 * new Note(Tone.A, Duration.QUARTER),
		 * new Note(Tone.A, Duration.QUARTER),
		 * new Note(Tone.A, Duration.HALF),
		 * new Note(Tone.B, Duration.QUARTER),
		 * new Note(Tone.D, Duration.QUARTER),
		 * new Note(Tone.D, Duration.HALF) }; */
		for (int n = 0; n < mhall.length; n++) {
			int bend = bpos + (mhall[n].dur.ms * 48000) / 1000;
			int i;
			for (i = bpos; i < bend - 1200; i++)
				buf[i] = (byte) (Math.sin(2 * Math.PI
						* ((i - bpos) / 48000.0 * mhall[n].tone.freq)) * 64);
			byte frqat = buf[i - 1];
			while (i <= bend)
				buf[i++] = frqat = (byte) (frqat > 0 ? frqat - 1
						: frqat < 0 ? frqat + 1 : 0);
			bpos = bend;
		}
		play(buf, 48000);
	}

	/**
	 * Plays a sample buffer.
	 * 
	 * @author Josh Ventura
	 * @date May 5, 2012; 2:26:44 AM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @param data
	 *            The sample data to play.
	 * @param sps
	 *            The sample rate, in samples per second.
	 * @specRef N/A
	 */
	public static void play(byte[] data, int sps) {
		AudioFormat afmt = new AudioFormat(sps, 8, 1, true, false);
		// AudioFormat afmt = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
		// sps, 8,1,((8 + 7) / 8) * 1, sps,false);
		DataLine.Info info = new DataLine.Info(Clip.class, afmt);
		try {
			final Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(afmt, data, 0, data.length);

			new Thread() {
				@Override public void run() {
					clip.start();
					try {
						do
							Thread.sleep(99);
						while (clip.isActive());
					} catch (InterruptedException e) {}
					System.out.println("Clip done");
					clip.stop();
					clip.close();
				}
			}.start();
		} catch (LineUnavailableException e1) {
			System.err.println("oops");
		}

	}

	// =========================================================
	// === Redundant code ======================================
	// =========================================================
	// === This code's the same in all instruction classes, ====
	// === But Java lacks the mechanism to allow stuffing it ===
	// === in super() where it belongs. ========================
	// =========================================================

	/**
	 * @see Instruction
	 * @return The static instance of this instruction.
	 */
	public static Instruction getInstance() {
		return staticInstance;
	}

	/** @see assemblernator.Instruction#getOpId() */
	@Override public String getOpId() {
		return opId;
	}

	/** @see assemblernator.Instruction#getOpcode() */
	@Override public int getOpcode() {
		return opCode;
	}

	/** @see assemblernator.Instruction#getNewInstance() */
	@Override public Instruction getNewInstance() {
		return new USI_SND();
	}

	/**
	 * Calls the Instance(String,int) constructor to track this instruction.
	 * 
	 * @param ignored
	 *            Unused parameter; used to distinguish the constructor for the
	 *            static instance.
	 */
	private USI_SND(boolean ignored) {
		super(opId, opCode);
	}

	/** Default constructor; does nothing. */
	private USI_SND() {}
}