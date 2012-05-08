package assemblernator;

import guinator.GUIMain;
import instructions.USI_EQU;
import instructions.USI_EXT;
import instructions.USI_NOISE;
import instructions.USI_NUM;

import java.io.File;
import java.util.ArrayList;

import assemblernator.ErrorReporting.DefaultErrorHandler;
import assemblernator.Instruction.Operand;
import assemblernator.Instruction.Usage;
import assemblernator.Module.Value;
import assemblernator.Module.Value.BitLocation;

/**
 * @file Main.java
 * @author Ratul Khosla, Eric Smith, Noah Torrance, Josh Ventura
 */

/**
 * @mainpage
 *           <img src="custom/logo.svg" style="clear:both" /><br/>
 *           <center><h1>An assembler for the URBAN Architecture</h1></center>
 */

/**
 * Main class to handle command line arguments and call subsystems.
 */
public class Main {
	/**
	 * Main method; called at program start. Handles command line arguments.
	 * 
	 * @param args
	 *            System-passed arguments to handle.
	 */
	public static void main(String[] args) {
		Module m = new Module();
		String num[] = new String[] { "zero", "one", "two", "three", "four",
				"five", "six", "seven" };
		for (int i = 0; i < num.length; i++) {
			Instruction in = USI_EQU.getInstance().getNewInstance();
			in.label = num[i];
			((USI_EQU) in).value = new Value(i, 'A');
			in.usage = Usage.EQUATE;
			m.getSymbolTable().addEntry(in, null);
		}

		{
			Instruction in = USI_EXT.getInstance().getNewInstance();
			for (int i = 0; i < 10; ++i)
				in.operands.add(new Operand("LR", "exlabel" + i, 0, 0));
			in.usage = Usage.EXTERNAL;
			m.getSymbolTable().addEntry(in, null);
			for (int i = 0; i < 10; ++i) {
				in = USI_NUM.getInstance().getNewInstance();
				in.label = "rlabel" + i;
				in.usage = Usage.LABEL;
				in.lc = i;
				m.getSymbolTable().addEntry(in, null);
			}
		}

		String x[] = new String[] { "1+1+2+3-5", "1+1", "1+1-1", "1+1+1",
				"1+1+1+1+1-2", "1 + 3 + 5 + 7 - 2", "1 + 3 - 2 + 5 + 7 - 2",
				"one + one", "two + two", "six + one", "two + six - one",
				"one + two + 4 + six - 5 -one+8+*+ 0 + * + *", "rlabel1",
				"rlabel1 + rlabel3", "rlabel5-rlabel1", "exlabel1",
				"exlabel1-rlabel1", "exlabel1-rlabel1+rlabel2",
				"exlabel1 + exlabel2", "exlabel1 + exlabel2 - exlabel2",
				"exlabel2 + 10 - exlabel2" };
		for (int i = 0; i < x.length; i++) {
			Value a = m.evaluate(x[i], true, BitLocation.Literal, new DefaultErrorHandler(), USI_EQU
					.getInstance().getNewInstance(), 0);
			System.out.println(x[i] + " = " + a.value + ", flag = " + a.arec);
		}


		USI_NOISE.test();

		if (args.length < 1) {
			System.out.println("URBAN Legend v" + Assembler.VERSION);
			System.out
					.println("Usage: java -jar urban.jar file1 file2 file3... -o executablename");
			GUIMain.launch();
			return;
		}

		String outputFile = null;
		ArrayList<String> filesToAssemble = new ArrayList<String>();

		for (int i = 0; i < args.length; i++) {
			if (args[i].length() < 1)
				continue;
			if (args[i].charAt(0) == '-') {
				if (args[i].equals("-o")) {
					if (++i > args.length) {
						System.err
								.println("Expected output filename following -o");
						System.exit(1);
					}
					outputFile = args[i];
					continue;
				}
				if (args[i].equals("-v") || args[i].equals("--version")) {
					System.out.println("URBAN Legend v" + Assembler.VERSION);
					System.exit(0);
				}
				System.err.println("Unknown command " + args[i]);
				System.exit(1);
			}
			filesToAssemble.add(args[i]);
		}
		if (filesToAssemble.size() == 0) {
			System.err.println("URBAN Legend: No input files");
		}
		for (int i = 0; i < filesToAssemble.size(); i++) {
			System.out.println("Assembling " + filesToAssemble.get(i));

			File f = new File(filesToAssemble.get(i));
			if (f.canRead()) {
				try {
					Module aModule = Assembler.parseFile(f,
							new DefaultErrorHandler());
					System.out.println("\n" + aModule.toString());


				} catch (NullPointerException npe) {
					System.err.println("Failed to read file `"
							+ filesToAssemble.get(i)
							+ "': Null pointer exception");
					npe.printStackTrace();
				}
			}
			else {
				System.err.println("Cannot open file `"
						+ filesToAssemble.get(i) + "'");
				System.exit(1);
			}
		}
		System.out.println("Currently no binary generation for output to `"
				+ outputFile + "'.");
	}
}
