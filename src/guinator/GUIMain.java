package guinator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.lateralgm.joshedit.JoshText;

/**
 * @author Josh Ventura
 * @date Apr 8, 2012; 5:53:40 PM
 */
public class GUIMain {
	/**
	 * Launch the GUI.
	 * @author Josh Ventura
	 * @date Apr 8, 2012; 5:55:27 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @specRef N/A
	 */
	public static void launch() {
		JFrame mainWindow = new JFrame();
		JTabbedPane tabPane = new JTabbedPane();
		
		mainWindow.add(tabPane);
		mainWindow.setSize(640, 480);
		mainWindow.setVisible(true);
		
		JoshText jt = new JoshText();
		JScrollPane sp = new JScrollPane(jt);
		tabPane.addTab("Untitled", sp);
	}
}
