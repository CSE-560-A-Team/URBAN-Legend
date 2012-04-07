package assemblernator;

import instructions.Comment;
import instructions.Generator;
import instructions.USI_ADRC;
import instructions.USI_AEXS;
import instructions.USI_AND;
import instructions.USI_CHAR;
import instructions.USI_CLR;
import instructions.USI_CLRA;
import instructions.USI_CLRX;
import instructions.USI_CRKB;
import instructions.USI_CWSR;
import instructions.USI_DMP;
import instructions.USI_END;
import instructions.USI_ENT;
import instructions.USI_EQU;
import instructions.USI_EQUE;
import instructions.USI_EXT;
import instructions.USI_HLT;
import instructions.USI_IAA;
import instructions.USI_IADD;
import instructions.USI_IDIV;
import instructions.USI_IMAD;
import instructions.USI_IMUL;
import instructions.USI_IRKB;
import instructions.USI_ISHL;
import instructions.USI_ISHR;
import instructions.USI_ISLA;
import instructions.USI_ISRA;
import instructions.USI_ISRG;
import instructions.USI_ISUB;
import instructions.USI_IWSR;
import instructions.USI_KICKO;
import instructions.USI_MOVD;
import instructions.USI_MOVDN;
import instructions.USI_NEWLC;
import instructions.USI_NOP;
import instructions.USI_NUM;
import instructions.USI_OR;
import instructions.USI_POP;
import instructions.USI_PSH;
import instructions.USI_PST;
import instructions.USI_PWR;
import instructions.USI_RET;
import instructions.USI_ROL;
import instructions.USI_ROR;
import instructions.USI_SKIPS;
import instructions.USI_SKT;
import instructions.USI_TR;
import instructions.USI_TRDR;
import instructions.USI_TREQ;
import instructions.USI_TRGT;
import instructions.USI_TRLK;
import instructions.USI_TRLT;

import java.io.IOException;

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
		System.out.println(IOFormat.formatInteger(1337, 4));
		
		System.out.print(IOFormat.formatHexInteger(1337, 20) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 11) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 5) + ", ");
		System.out.print(IOFormat.formatHexInteger(1337, 4) + ", ");
		System.out.println(IOFormat.formatHexInteger(1337, 3));
		
		System.out.print(IOFormat.formatBinInteger(1337, 20) + ", ");
		System.out.println(IOFormat.formatBinInteger(1337, 11));
		
	}
}
