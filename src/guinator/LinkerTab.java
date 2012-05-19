package guinator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import assemblernator.ErrorReporting.ErrorHandler;
import assemblernator.Linker;
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
	/** What you press to add files. */
	JButton addFiles;
	/** What you press to start the linker. */
	JButton doLink;

	/** The actual modules we will be linking */
	ArrayList<LinkerModule> linkMods;

	/**
	 * Populate the linker tab UI.
	 */
	public LinkerTab() {
		setOrientation(VERTICAL_SPLIT);
		JPanel npanel = new JPanel();
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.add(addFiles = new JButton("Add File"));
		toolbar.add(doLink = new JButton("Link"));
		toolbar.setFloatable(false);
		toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

		addFiles.addActionListener(new LinktabListener());

		linkTable = new FullTable();
		String cols[] = { "Filename", "Name", "Date of Assembly", "Load At",
				"Start At" };
		linkTable.setModel(new DefaultTableModel(cols, 0));
		linkTable.setShowHorizontalLines(false);
		linkTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		linkTable.setFillsViewportHeight(true);

		npanel.setLayout(new BoxLayout(npanel, BoxLayout.PAGE_AXIS));
		npanel.add(new JScrollPane(linkTable));
		npanel.add(toolbar);

		setLeftComponent(npanel);
		setRightComponent(warningOutput = new JTextArea());
		setDividerLocation(330);
	}

	/**
	 * @author Josh Ventura
	 * @date May 18, 2012; 11:03:01 PM
	 */
	class LinkErrHander implements ErrorHandler {
		/**
		 * @see assemblernator.ErrorReporting.ErrorHandler#reportError(java.lang.String,
		 *      int, int)
		 */
		@Override public void reportError(String err, int line, int pos) {
			warningOutput.append("ERROR: " + err + "\n");
		}

		/**
		 * @see assemblernator.ErrorReporting.ErrorHandler#reportWarning(java.lang.String,
		 *      int, int)
		 */
		@Override public void reportWarning(String warn, int line, int pos) {
			warningOutput.append("WARNING: " + warn + "\n");
		}
	}

	/** This tab's error handling context */
	LinkErrHander hErr = new LinkErrHander();

	/**
	 * @author Josh Ventura
	 * @date May 18, 2012; 9:13:52 PM
	 */
	public class LinktabListener implements ActionListener {

		/** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
		@Override public void actionPerformed(ActionEvent e) {
			if (e.getSource() == doLink) {
				String saveto = GUIUtil.getSaveFname(LinkerTab.this, ".ux");

			}
			else if (e.getSource() == addFiles) {
				String[] fnames = GUIUtil.getLoadFnames(LinkerTab.this, ".o",
						".ulo");
				LinkerModule lms[] = Linker.getModules(fnames, hErr);
				int i = 0;
				for (LinkerModule lm : lms) {
					linkTable.getRowCount();
					DefaultTableModel tm = (DefaultTableModel) linkTable.getModel();
					System.out.println("Adding row: " + fnames[i++] + "," + 
							lm.progName + "," + 
							lm.date + "," + 
							lm.loadAddr + "," + 
							lm.execStart);
					tm.addRow(new Object[] {
							fnames[i++],
							lm.progName,
							lm.date,
							lm.loadAddr,
							lm.execStart
					});
					linkMods.add(lm);
				}
			}
		}

	}
}
