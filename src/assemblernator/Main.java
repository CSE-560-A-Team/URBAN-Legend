package assemblernator;
/**
 * @author Ratul Khosla, Eric Smith, Noah Torrance, Josh Ventura
 *
 */


import java.util.Date;

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

		System.out.print(new String(IOFormat.formatInteger(1337, 10)) + "\n");
		System.out.print(new String(IOFormat.formatInteger(1337, 5)) + "\n");
		System.out.print(new String(IOFormat.formatInteger(1337, 4)) + "\n");
		System.out.print(new String(IOFormat.formatInteger(1337, 2)) + "\n");
		System.out.print(new String(IOFormat.formatInteger(1337, 1)) + "\n");
		System.out.print(new String(IOFormat.formatInteger(1337, 0)) + "\n");

		System.out
				.print(new String(IOFormat.formatHexInteger(1337, 10)) + "\n");
		System.out.print(new String(IOFormat.formatHexInteger(1337, 4)) + "\n");
		System.out.print(new String(IOFormat.formatHexInteger(1337, 3)) + "\n");
		System.out.print(new String(IOFormat.formatHexInteger(1337, 2)) + "\n");
		System.out.print(new String(IOFormat.formatHexInteger(1337, 1)) + "\n");
		System.out.print(new String(IOFormat.formatHexInteger(1337, 0)) + "\n");

		System.out.print(new String(IOFormat.formatInteger(9001, 7)) + "\n");
		System.out.print(new String(IOFormat.formatDate(new Date())) + "\n");
	}
}
