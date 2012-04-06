package assemblernator;

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
		
		MOVD_Instruction.getInstance();
		
		try {
			Instruction i = Instruction.parse("lolol MOVD EX:'For great justice';");
			if (i != null) {
				System.out.println("Success!");
				System.out.println(i);
			}
			else
				System.out.println("More info requested.");
		} catch (Exception e) {
			System.out.println("CRITICAL FAILURE. HAHAHAHAHAHAHAHA");
			e.printStackTrace();
		}
	}
}
