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
import assemblernator.IOFormat;
import assemblernator.Instruction;
import assemblernator.Module;
import assemblernator.Module.Value.BitLocation;
import assemblernator.OperandChecker;

/**
 * The ISRG instruction.
 * 
 * @author Generate.java
 * @date Apr 08, 2012; 08:26:19
 * @specRef UL1
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
		/** Bass noise */
		BASS(1),
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
		          if (sample.equals("pluck") ) { sampleType = SampleType.PLUCK;  break; }
		case 's': if (sample.equals("sine")  ) { sampleType = SampleType.SINE;   break; }
		          if (sample.equals("saw")   ) { sampleType = SampleType.SAW;    break; }
		          if (sample.equals("square")) { sampleType = SampleType.SQUARE; break; }
		case 'b': if (sample.equals("boink") ) { sampleType = SampleType.BOINK;  break; }
		          if (sample.equals("bass")  ) { sampleType = SampleType.BASS;   break; }
		default:  hErr.reportError(makeError("unmatchedStr", sample, "any built-in sample"), lineNum, op.keywordStartPosition);
		} //@formatter:on

		op = getOperandData("FR");
		if (op == null) {
			hErr.reportError(
					makeError("instructionMissingOpS", opId, "FR", "FRequency"),
					lineNum, 0);
			return false;
		}
		op.value = module.evaluate(op.expression, false, BitLocation.Other,
				hErr, this, op.valueStartPosition);
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
		op.value = module.evaluate(op.expression, false, BitLocation.Other,
				hErr, this, op.valueStartPosition);
		if (op.value.value < 25 || op.value.value > 6400) {
			hErr.reportError(
					makeError("OORconstant", "DM", opId, "25", "6400"),
					lineNum, op.valueStartPosition);
			return false;
		}

		return true;
	}

	/** @see assemblernator.Instruction#assemble() */
	@Override public int[] assemble() {
		return new int[] { (opCode << 26) | (sampleType.id << 23)
				| (getOperandData("FR").value.value << 9)
				| (getOperandData("DM").value.value / 25) };
	}

	/** @see assemblernator.Instruction#execute(int, Machine) */
	@Override public void execute(int instruction, Machine machine) {
		int freq = (instruction & ~(0xFFFFFFFF >>> 23 << 23)) >> 9;
		int dur = (instruction & (~(0xFFFFFFFF >>> 9 << 9))) * 10;
		int sample = (instruction & ~(0xFFFFFFFF >>> 26 << 26)) >> 23;
		play(samples[sample].generate(freq, dur, 48000), 48000);
		try {
			Thread.sleep(dur);
		} catch (InterruptedException e) {}
	}

	/**
	 * Tone generation class.
	 * 
	 * @author Josh Ventura
	 * @date May 24, 2012; 1:41:07 AM
	 */
	static interface ToneGenerator {
		/**
		 * @param freq
		 *            The frequency of the tone.
		 * @param dur
		 *            The duration of the tone.
		 * @param sps
		 *            Samples per second.
		 * @return Bytes of this sample.
		 */
		public byte[] generate(int freq, int dur, int sps);
	}

	/**
	 * @author Josh Ventura
	 * @date May 24, 2012; 7:59:03 PM
	 */
	static abstract class WaveFormTone implements ToneGenerator {
		/**
		 * Math function to get waveform value at a position
		 * 
		 * @param x
		 *            The position.
		 * @return The value of this waveform.
		 */
		abstract double waveform(double x);

		/** Generate a sine tone. */
		@Override final public byte[] generate(int freq, int dur, int sps) {
			System.out.println("Generate sine tone " + freq + " * " + dur);
			int buflen = (dur * sps) / 1000, i;
			byte[] buf = new byte[buflen];
			for (i = 0; i < buf.length - 1200; i++)
				buf[i] = (byte) (waveform(2 * Math.PI
						* (i / (double) sps * freq)) * 64);
			byte frqat = buf[i - 1];
			while (i < buf.length)
				buf[i++] = frqat = (byte) (frqat > 0 ? frqat - 1
						: frqat < 0 ? frqat + 1 : 0);
			return buf;
		}
	}

	/** Sine tone generator. */
	static class SineTone extends WaveFormTone implements ToneGenerator {
		/** Use system sine(). */
		@Override double waveform(double x) {
			return Math.sin(x);
		}
	}

	/** Saw tone generator. */
	static class SawTone extends WaveFormTone implements ToneGenerator {
		/** Saw function. */
		@Override double waveform(double x) {
			return (x / (2 * Math.PI)) % 1;
		}
	}

	/** Square tone generator. */
	static class SquareTone extends WaveFormTone implements ToneGenerator {
		/**
		 * @param x
		 *            The phase.
		 * @return The saw value.
		 */
		@Override double waveform(double x) {
			return Math.min(.8, Math.max(-.8, Math.sin(x) * 10));
		}
	}

	/** Square tone generator. */
	static class WhiteNoise extends WaveFormTone implements ToneGenerator {
		/**
		 * @param x
		 *            The phase.
		 * @return The noise value.
		 */
		@Override double waveform(double x) {
			return ((x / (2 * Math.PI)) % 1) * (1 - 2 * Math.random());
		}
	}

	/** Warp a bass drum sample to a frequency. */
	static class BassSample implements ToneGenerator {
		/** Generate a sine tone. */
		@Override public byte[] generate(int freq, int dur, int sps) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/** Warp the BOINK sample to a frequency. */
	static class BoinkSample implements ToneGenerator {
		/** Generate a sine tone. */
		@Override public byte[] generate(int freq, int dur, int sps) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/** Generate a pluck with a frequency. */
	static class PluckGenerator implements ToneGenerator {
		/** Generate a sine tone. */
		@Override public byte[] generate(int freq, int dur, int sps) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/** All available samples. */
	ToneGenerator samples[] = new ToneGenerator[] { new WhiteNoise(),
			new BassSample(), new SquareTone(), new SawTone(), new SineTone(),
			new BoinkSample(), new PluckGenerator() };

	/**
	 * Plays a sample buffer.
	 * 
	 * @author Josh Ventura
	 * @date May 5, 2012; 2:26:44 AM
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
					} catch (InterruptedException e) {
						System.err.println("INTERRUPT");
					}
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
