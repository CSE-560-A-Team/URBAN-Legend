package guinator;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import assemblernator.LinkerModule;

/**
 * @author Josh Ventura
 * @date May 14, 2012; 6:19:02 PM
 */
public class LinkerTab extends JSplitPane {
	/** Make ECJ shut up */
	private static final long serialVersionUID = 1L;

	/** Table of files to be linked. */
	JTable linkTable;
	/** Text box of link messages */
	JTextArea warningOutput;

	/** The actual modules we will be linking */
	ArrayList<LinkerModule> linkMods;

	/**
	 * Populate the linker tab UI.
	 */
	public LinkerTab() {
		setOrientation(VERTICAL_SPLIT);
		JPanel npanel = new JPanel();
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.add(new JButton("Link"));

		linkTable = new JTable();
		String cols[] = { "Filename", "Name", "Date of Assembly", "Load At", "Start At" };
		linkTable.setModel(new DefaultTableModel(cols, 0));

		npanel.setLayout(new BoxLayout(npanel, BoxLayout.PAGE_AXIS));
		npanel.add(linkTable);
		npanel.add(toolbar);
		
		setLeftComponent(npanel);
		setRightComponent(warningOutput = new JTextArea());
	}
}
