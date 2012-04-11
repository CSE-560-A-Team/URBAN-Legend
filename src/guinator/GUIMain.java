package guinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.lateralgm.joshedit.JoshText;

import assemblernator.Assembler;
import assemblernator.Module;

/**
 * @author Josh Ventura
 * @date Apr 8, 2012; 5:53:40 PM
 */
public class GUIMain {

	// ===============================================================
	// == MENU ITEMS =================================================
	// ===============================================================
	/** Our compile menu item */
	static JMenuItem compile;
	/** Our exit menu item */
	static JMenuItem exit;

	// ===============================================================
	// == DISPLAYS ===================================================
	// ===============================================================
	/** Our editor */
	static JoshText jt;
	/** Our symbol table */
	static JTable sTable;
	/** Our user report */
	static JTextArea userReport;

	/**
	 * Launch the GUI.
	 * 
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
		UIManager.put("swing.boldMetal", false);
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		JFrame mainWindow = new JFrame();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabPane = new JTabbedPane();

		mainWindow.add(tabPane);
		mainWindow.setSize(720, 540);

		JMenuBar mb = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		filemenu.add(compile = new JMenuItem("Compile"));
		compile.addActionListener(new MenuListener());
		filemenu.addSeparator();
		filemenu.add(exit = new JMenuItem("Exit"));
		exit.addActionListener(new MenuListener());
		mb.add(filemenu);
		mainWindow.setJMenuBar(mb);

		jt = new JoshText();
		JScrollPane sp = new JScrollPane(jt);

		userReport = new JTextArea();
		userReport.setEditable(false);
		sTable = new JTable();

		JSplitPane sjsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp,
				new JScrollPane(userReport));
		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sjsp,
				new JScrollPane(sTable));

		sjsp.setOneTouchExpandable(true);
		jsp.setOneTouchExpandable(true);
		tabPane.addTab("Untitled", jsp);
		jsp.setDividerLocation(540);
		sjsp.setDividerLocation(360);

		mainWindow.setVisible(true);
		jt.grabFocus();
	}

	/**
	 * Class to handle menu selections.
	 * 
	 * @author Josh Ventura
	 * @date Apr 9, 2012; 12:56:30 AM
	 */
	static class MenuListener implements ActionListener {
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override public void actionPerformed(ActionEvent e) {
			if (e.getSource() == compile) {
				String str = jt.getText();
				System.out.println("this is what I have:\n" + str);
				Module mod = Assembler.parseString(str);
				userReport.setText(mod.toString());
				String rawTable = mod.getSymbolTable().toString();
				String[] allData = rawTable.trim().split("(\r\n|\n|\r)");
				if (allData != null && allData.length > 0) {
					String[] columnNames = allData[0].split("\\:\t");
					String[][] data = new String[allData.length - 1][columnNames.length];
					for (int i = 1; i < allData.length; i++) {
						String dc[] = allData[i].split("\t");
						int iw;
						for (iw = 0; iw < dc.length && iw < data[i-1].length; iw++)
							data[i-1][iw] = dc[iw];
						while (iw < data[i-1].length)
							data[i-1][iw++] = "";
					}
					sTable.setModel(new DefaultTableModel(data, columnNames));
					sTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				}
				else
					sTable.setModel(null);
				return;
			}
			if (e.getSource() == exit) {
				System.exit(0);
				return;
			}
		}
	}
}
