package assemblernator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.lateralgm.joshedit.JoshText;

/**
 * @author Josh Ventura
 * @date May 29, 2012; 12:18:41 PM
 */
public class AssemblerTest {

	/**
	 * @author Josh Ventura
	 * @date May 29, 2012; 11:52:29 AM
	 * @param m
	 *            Module whose test case will be printed.
	 * @param objectFile
	 *            The stream to which the object file will be written, or null.
	 * @return The test case as HTML.
	 */
	public static String getTestCase(Module m, ByteArrayOutputStream objectFile) {
		String[][] table = m.getSymbolTable().toStringTable();
		String res = "<h1>Symbol table</h1>\n";
		res += "<table>\n";
		res += "  <tr>\n";
		for (int i = 0; i < table[0].length; i++)
			res += "    <th>" + table[0][i] + "</th>\n";
		res += "  </tr>";

		for (int i = 1; i < table.length; i++) {
			res += "  <tr>\n";
			for (int j = 0; j < table[i].length; j++)
				res += "    <td>" + JoshText.htmlSpecialChars(table[i][j])
						+ "</td>\n";
			res += "  </tr>\n";
		}
		res += "</table>\n<br />\n";

		res += "<h1>User report</h1>\n";
		res += "<pre>";
		res += m.toString();
		res += "</pre>\n";

		res += "<h2>Object File</h2>\n";
		res += "<pre>";
		ByteArrayOutputStream baos = objectFile == null ? new ByteArrayOutputStream()
				: objectFile;
		try {
			m.writeObjectFile(baos);
			res += baos.toString().replaceAll(":" + m.programName + ":",
					":" + m.programName + ":\n");
		} catch (IOException e) {
			res += "<i>The object file could not be generated for this program.</i>";
		} catch (Exception e) {
			res += "<i>The object file could not be generated for this program.</i>";
		}
		res += "</pre>\n";
		return res;
	}
}
