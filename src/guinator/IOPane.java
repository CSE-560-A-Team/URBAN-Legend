package guinator;

import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import ulutil.HTMLOutputStream;

import assemblernator.ErrorReporting.ErrorHandler;

/**
 * @author Josh Ventura
 * @date May 22, 2012; 2:13:25 PM
 */
public class IOPane extends JTextPane {
	/** Shut up, ECJ. */
	private static final long serialVersionUID = 1L;

	/** The HTML document to print stuff to. */
	HTMLDocument htmlDoc;
	/** The HTML kit to print stuff to. */
	HTMLEditorKit htmlKit;

	/** Bitches& */
	HTMLOutputStream hos = new HTMLOutputStream() {
		@Override public void writeSource(String x) {
			try {
				htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), x, 0, 0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override public void write(int b) throws IOException {
			try {
				htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), new String(
						new byte[] { (byte) b }), 0, 0, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/** Default constructor. */
	public IOPane() {
		super();
		setEditorKit(htmlKit = new HTMLEditorKit());
		setDocument(htmlDoc = new HTMLDocument());
		setEditable(false);
	}

	/**
	 * @author Josh Ventura
	 * @date May 18, 2012; 11:03:01 PM
	 */
	class ColorfulErrHander implements ErrorHandler {
		/**
		 * @see assemblernator.ErrorReporting.ErrorHandler#reportError(java.lang.String,
		 *      int, int)
		 */
		@Override public void reportError(String err, int line, int pos) {
			try {
				htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(),
						"<span style=\"color:red\"><b>ERROR:</b> " + err
								+ "</span><br/>\n", 0, 0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * @see assemblernator.ErrorReporting.ErrorHandler#reportWarning(java.lang.String,
		 *      int, int)
		 */
		@Override public void reportWarning(String warn, int line, int pos) {
			try {
				htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(),
						"<span style=\"color:#FF8000\"><b>WARNING:</b> " + warn
								+ "</span><br/>\n", 0, 0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** This tab's error handling context */
	public ColorfulErrHander hErr = new ColorfulErrHander();

	/**
	 * @author Josh Ventura
	 * @date May 22, 2012; 2:41:24 PM
	 * @param html
	 *            HTML to insert.
	 * @specRef N/A
	 */
	public void append(String html) {
		hos.writeHTML(html);
	}

	/**
	 * @author Josh Ventura
	 * @date May 22, 2012; 2:41:24 PM
	 * @param str
	 *            The string to escape and insert.
	 * @specRef N/A
	 */
	public void appendPlain(String str) {
		try {
			hos.write(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
