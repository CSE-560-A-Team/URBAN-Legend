package assemblernator;

import java.io.IOException;

import instructions.Comment;
import instructions.MOVD_Instruction;

/**
 * @author Ratul Khosla, Eric Smith, Noah Torrance, Josh Ventura
 * 
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
		System.out.println("Hello World\n");

		Comment.getInstance();
		MOVD_Instruction.getInstance();

		String sample[] = {
				"label MovD EX:'lol what';",
				"xx MOVD DR:1,FM:Mud,FX:1;",
				"CC MOVD DM:Mud,FR:1,DX:1;",
				"dd MOVD DR:1,FR:2; Register to register",
				"; Full-line comment",
				"birch movd FR:SomeEQUlabel+1, DM:someotherequlabellol-100;"
		};

		for (String l : sample)
			try {
				Instruction i = Instruction.parse(l);
				if (i != null) {
					System.out.println(i);
				}
				else
					System.out.println("More info requested.");
			} catch (IOException e) {
				System.out.println("Requested another line (" + e.getMessage()
						+ ")");
			} catch (Exception e) {
				System.out.println("CRITICAL FAILURE. HAHAHAHAHAHAHAHA");
				e.printStackTrace();
			}
		
		System.out.print(IOFormat.formatInteger(1337, 20) + ", ");
		System.out.print(IOFormat.formatInteger(1337, 11) + ", ");
		System.out.print(IOFormat.formatInteger(1337, 5) + ", ");
		System.out.print(IOFormat.formatInteger(1337, 4) + ", ");
		System.out.print(IOFormat.formatInteger(1337, 3) + ", ");
		System.out.print(IOFormat.formatInteger(1337, 2) + ", ");
		System.out.print(IOFormat.formatInteger(1337, 1) + ": ");
		System.out.println(IOFormat.formatInteger(1337, 0));
		
		System.out.print(IOFormat.formatHexInteger(1337, 20) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 11) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 5) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 4) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 3) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 2) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 1) + ": ");
		System.out.println(IOFormat.formatHexInteger(1337, 0));
		
		System.out.print(IOFormat.formatBinInteger(1337, 20) + ", ");
		System.out.print(IOFormat.formatBinInteger(1337, 11) + ", ");
		System.out.print(IOFormat.formatBinInteger(1337, 5) + ", ");
		System.out.print(IOFormat.formatBinInteger(1337, 4) + ", ");
		System.out.print(IOFormat.formatBinInteger(1337, 3) + ", ");
		System.out.print(IOFormat.formatBinInteger(1337, 2) + ", ");
		System.out.print(IOFormat.formatBinInteger(1337, 1) + ": ");
		System.out.println(IOFormat.formatBinInteger(1337, 0));
	}
}
