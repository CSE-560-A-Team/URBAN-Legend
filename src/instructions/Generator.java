package instructions;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import ulutil.IOFormat;


/**
 * @author Josh Ventura
 * @date Apr 7, 2012; 1:32:45 PM
 */
public class Generator {
	/**
	 * @author Josh Ventura
	 * @date Apr 7, 2012; 1:33:02 PM
	 * @modified UNMODIFIED
	 * @tested This function is used only once by developers. Any issues are
	 *         irrelevant.
	 * @errors This function is used only once by developers. Any issues are
	 *         irrelevant.
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @specRef N/A
	 */
	public static void generate() {
		try {
			FileInputStream fis = new FileInputStream(
					"src/instructions/ins_template.txt");
			byte[] file = new byte[65536];
			int lr = fis.read(file);
			fis.close();

			fis = new FileInputStream("src/instructions/ISA.txt");
			Scanner scan = new Scanner(fis);

			String fstr = new String(file, 0, lr);
			
			String getInstCode = "";

			String tok = scan.next();
			while (scan.hasNext()) {
				String specref = tok, opc, insname, lbltype, insclass, datetime,
						usage, lcexp, opcodehex, opcodebin, isinstruction = "1";

				System.out.print(specref + " ");
				opc = scan.next();
				System.out.print(opc + " ");
				insname = scan.next().toUpperCase();
				System.out.print(insname + " ");
				lbltype = scan.next();
				System.out.print(lbltype + " ");
				usage = scan.next();
				System.out.print(usage + " ");
				lcexp = scan.next();
				System.out.print(lcexp + " ");
				insclass = "USI_" + insname.toUpperCase();

				DateFormat df = new SimpleDateFormat("MMM dd, yyyy; hh:mm:ss");
				datetime = df.format(new Date());

				if (opc.equals("x")) {
					opcodehex = "0xFFFFFFFF";
					opcodebin = "This instruction doesn't have an opcode.";
					isinstruction = "0";
				}
				else {
					int oci = Integer.parseInt(opc, 2);
					opcodehex = "0x" + IOFormat.formatHexInteger(oci, 8);
					opcodebin = "0b" + IOFormat.formatBinInteger(oci, 6)
							+ "00000000000000000000000000";
				}

				tok = scan.next();
				System.out.print(tok + " ");

				if (tok == "none") {
					System.out.println(tok);
					tok = scan.next();
					continue;
				}

				if (scan.hasNext()) {
					tok = scan.next();
					System.out.print(tok + " ");
					while (tok.equals("or")) {
						tok = scan.next();
						System.out.print(tok + " ");
						tok = scan.next();
						System.out.print(tok + " ");
					}
				}
				System.out.println();
				
				String usageovr = "";
				if (!usage.equals("none")) {
					usageovr = "\n\t\tusage = Usage." + usage + ";";
				}

				String fprint = fstr.replace("${SPECREF}", specref);
				fprint = fprint.replace("${DATETIME}", datetime);
				fprint = fprint.replace("${INSNAME}", insname);
				fprint = fprint.replace("${INSCLASS}", insclass);
				fprint = fprint.replace("${OPCODEHEX}", opcodehex);
				fprint = fprint.replace("${OPCODEBIN}", opcodebin);
				fprint = fprint.replace("${ISINSTRUCTION}", isinstruction);
				fprint = fprint.replace("${USAGEOVERRIDE}", usageovr);
				fprint = fprint.replace("${LCEXP}", lcexp);

				/* System.out.println("${SPECREF} = " + specref
				 * + ", ${DATETIME} = `" + datetime + "', ${INSNAME} = `"
				 * + insname + "', ${INSCLASS} = `" + insclass
				 * + "' ${OPCODEHEX} = `" + opcodehex
				 * + "' ${OPCODEBIN} = `" + opcodebin + "'"); */
				System.out.println(specref + ", `" + datetime + "' `" + insname
						+ "' `" + insclass + "' `" + opcodehex + "' `"
						+ opcodebin + "'");

				FileWriter fos = new FileWriter("src/instructions/"
						+ insclass + ".java");
				fos.write(fprint);
				fos.close();
				
				getInstCode += insclass + ".getInstance();\n";
			}
			
			System.out.println(getInstCode);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
