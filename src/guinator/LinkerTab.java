package guinator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

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
	IOPane warningOutput;
	/** What you press to add files. */
	JButton addFiles;
	/** What you press to start the linker. */
	JButton btnDoLink;

	/** The actual modules we will be linking */
	ArrayList<LinkerModule> linkMods = new ArrayList<LinkerModule>();

	/**
	 * Populate the linker tab UI.
	 */
	public LinkerTab() {
		setOrientation(VERTICAL_SPLIT);
		JPanel npanel = new JPanel();
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.add(addFiles = new JButton("Add File"));
		toolbar.add(btnDoLink = new JButton("Link"));
		toolbar.setFloatable(false);
		toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

		LinktabListener ltl = new LinktabListener();
		addFiles.addActionListener(ltl);
		btnDoLink.addActionListener(ltl);

		String cols[] = { "Filename", "Name", "Date of Assembly", "Load At",
				"Start At" };
		linkTable = new FullTable(new DefaultTableModel(cols, 0));
		linkTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		linkTable.setFillsViewportHeight(true);

		npanel.setLayout(new BoxLayout(npanel, BoxLayout.PAGE_AXIS));
		npanel.add(new JScrollPane(linkTable));
		npanel.add(toolbar);

		warningOutput = new IOPane();

		setLeftComponent(npanel);
		setRightComponent(new JScrollPane(warningOutput));
		setDividerLocation(330);
	}

	/**
	 * @author Josh Ventura
	 * @date May 18, 2012; 9:13:52 PM
	 */
	public class LinktabListener implements ActionListener {

		/** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
		@Override public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnDoLink)
				doLink();
			else if (e.getSource() == addFiles) {
				String[] fnames = GUIUtil.getLoadFnames(LinkerTab.this,
						"URBAN Object Files", ".o", ".ulo");
				System.out.println("Received " + fnames.length + " filenames");
				LinkerModule lms[] = Linker.getModules(fnames,
						warningOutput.hErr);
				System.out.println("Received " + lms.length + " modules");
				for (LinkerModule lm : lms) {
					linkTable.getRowCount();
					DefaultTableModel tm = (DefaultTableModel) linkTable
							.getModel();
					System.out.println("Adding row: " + lm.filename + ","
							+ lm.progName + "," + lm.date + "," + lm.loadAddr
							+ "," + lm.execStart);
					tm.addRow(new Object[] { lm.filename, lm.progName, lm.date,
							lm.loadAddr, lm.execStart });
					linkMods.add(lm);
				}
			}
		}
	}

	/**
	 * @author Josh Ventura
	 * @date May 29, 2012; 12:34:34 PM
	 * @specRef N/A
	 */
	public void doLink() {
		String saveto = GUIUtil.getSaveFname(LinkerTab.this,
				"URBAN Legend Executables", ".ulx");
		String linktable = Linker.link(linkMods.toArray(new LinkerModule[0]),
				saveto, warningOutput.hErr);
		for (LinkerModule lm : linkMods)
			warningOutput.appendPlain("================= Object File "
					+ (new File(lm.filename)).getName()
					+ " =================\n" + lm.toString() + "\n\n");
		warningOutput
				.appendPlain("================= Joint Symbol Table =================\n"
						+ linktable + "\n\n");
	}
}
