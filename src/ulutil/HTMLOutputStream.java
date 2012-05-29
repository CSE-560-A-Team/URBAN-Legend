package ulutil;

import java.io.OutputStream;

/**
 * @author Josh Ventura
 * @date May 28, 2012; 10:05:43 PM
 */
public abstract class HTMLOutputStream extends OutputStream {
	/**
	 * @author Josh Ventura
	 * @date May 28, 2012; 10:49:32 PM
	 * @param x
	 *            Stream backend writer.
	 */
	public abstract void writeSource(String x);

	/**
	 * @author Josh Ventura.toString()
	 * @date May 28, 2012; 10:26:16 PM
	 * @param str
	 *            The plain-text string to escape as HTML.
	 * @return The given string, escaped to render properly in HTML.
	 */
	public static String escape(String str) {
		StringBuilder asPlain = new StringBuilder(str.length() * 4);
		asPlain.append("<pre>");
		for (int i = 0; i < str.length(); ++i) {
			switch (str.charAt(i)) {
			case '\r':
				if (i + 1 < str.length() && str.charAt(i + 1) != '\n')
					asPlain.append("<br/>\n");
				continue;
			case '&':
				asPlain.append("&amp;");
				continue;
			case '"':
				asPlain.append("&quot;");
				continue;
			case '<':
				asPlain.append("&lt;");
				continue;
			case '>':
				asPlain.append("&gt;");
				continue;
				// case ' ':
				// asPlain.append("&nbsp;");
				// continue;
				/* case '\n':
				 * asPlain.append("\n");
				 * continue;
				 * case '\t':
				 * asPlain.append("\t");
				 * continue; */
			default:
				asPlain.append(str.charAt(i));
				continue;
			}
		}
		asPlain.append("</pre>");
		return asPlain.toString();
	}

	/**
	 * Writes planetext to the stream.
	 * 
	 * @author Josh Ventura
	 * @date May 28, 2012; 10:07:19 PM
	 * @param str
	 *            The string to put to the stream.
	 */
	public void write(String str) {
		writeSource(escape(str));
	}

	/**
	 * @author Josh Ventura
	 * @date May 28, 2012; 10:22:32 PM
	 * @param str
	 *            The HTML to write.
	 */
	public void writeHTML(String str) {
		writeSource(str);
	}

	/** Nonstandard--Casts byte array to String for print. */
	@Override public void write(byte[] b) {
		writeSource(new String(b));
	}
}
