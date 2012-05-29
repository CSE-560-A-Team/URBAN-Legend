package ulutil;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JOptionPane;

import simulanator.Machine.URBANInputStream;
import simulanator.Machine.URBANOutputStream;
import assemblernator.ErrorReporting.ErrorHandler;

/**
 * @author Josh Ventura
 * @date May 29, 2012; 1:15:36 AM
 */
public class FrankenOutput extends HTMLOutputStream implements
		URBANOutputStream, URBANInputStream, ErrorHandler {
	/** Sum of buffered HTML */
	public String appme = "";
	/** The window which will parent input dialogs */
	Component dialogParent = null;

	/** Default constructor */
	public FrankenOutput() {}

	/**
	 * Default constructor
	 * 
	 * @param parentWindow
	 *            The JComponent which will parent any input dialogs spawned.
	 */
	public FrankenOutput(Component parentWindow) {
		dialogParent = parentWindow;
	}

	/** @see java.io.OutputStream#write(int) */
	@Override public void write(int b) throws IOException {
		appme += (char) b;
	}

	/** @see ulutil.HTMLOutputStream#writeSource(java.lang.String) */
	@Override public void writeSource(String x) {
		appme += x;
	}

	/** @see simulanator.Machine.URBANOutputStream#putString(java.lang.String) */
	@Override public void putString(String str) {
		appme += escape(str);
	}

	/** @see assemblernator.ErrorReporting.ErrorHandler#reportError(String,int,int) */
	@Override public void reportError(String err, int line, int pos) {
		appme += "<span color=\"red\">" + err + "</span>";
	}

	/** @see assemblernator.ErrorReporting.ErrorHandler#reportWarning(String,int,int) */
	@Override public void reportWarning(String warn, int line, int pos) {
		appme += "<span color=\"#FF8000\">" + warn + "</span>";
	}

	/** @see simulanator.Machine.URBANInputStream#getString() */
	@Override public String getString() {
		String res = (String) JOptionPane.showInputDialog(dialogParent,
				"Application requests input", "Application input",
				JOptionPane.PLAIN_MESSAGE, null, null, "");
		appme += "<i><b>Prompted for user input. User entered </b><dd>" + res
				+ "</dd></i>";
		return res;
	}
};
