package guinator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.lateralgm.joshedit.JoshText;
import org.lateralgm.joshedit.JoshText.CodeMetrics;
import org.lateralgm.joshedit.JoshText.Highlighter;
import org.lateralgm.joshedit.Runner.JoshTextPanel;

import assemblernator.Assembler;
import assemblernator.ErrorReporting.ErrorHandler;
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
	/** A menu item to export the file as an HTML document */
	static JMenuItem writeHTML;
	/** A menu item to export a test case as an HTML document */
	static JMenuItem writeHTMLTest;
	/** A menu item to copy a test case to the clipboard as HTML */
	static JMenuItem copyHTMLTest;
	/** Our exit menu item */
	static JMenuItem exit;

	// ===============================================================
	// == COMPONENTS =================================================
	// ===============================================================
	/** Our main window. */
	JFrame mainWindow;
	/** Our main tab pane. */
	JTabbedPane tabPane;

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
		new GUIMain();
	}

	/**
	 * Default constructor. Builds UI.
	 */
	public GUIMain() {

		UIManager.put("swing.boldMetal", false);
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		mainWindow = new JFrame();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle("URBAN Legend");

		tabPane = new JTabbedPane();

		mainWindow.add(tabPane);
		mainWindow.setSize(720, 540);

		JMenuBar mb = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		MenuListener ml = new MenuListener();
		filemenu.add(compile = new JMenuItem("Compile"));
		compile.addActionListener(ml);
		filemenu.addSeparator();
		filemenu.add(writeHTML = new JMenuItem("Export as HTML"));
		writeHTML.addActionListener(ml);
		filemenu.add(writeHTMLTest = new JMenuItem("Write HTML Test Case"));
		writeHTML.addActionListener(ml);
		filemenu.add(copyHTMLTest = new JMenuItem("Copy HTML Test Case"));
		copyHTMLTest.addActionListener(ml);
		filemenu.addSeparator();
		filemenu.add(exit = new JMenuItem("Exit"));
		exit.addActionListener(ml);
		mb.add(filemenu);
		mainWindow.setJMenuBar(mb);

		FileTab ft = new FileTab();
		tabPane.addTab("Untitled", ft);
		mainWindow.setVisible(true);
		ft.jt.grabFocus();
	}

	/**
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 11:17:49 PM
	 */
	class FileTab extends JSplitPane {
		/** Make the compiler shut up */
		private static final long serialVersionUID = 1L;

		/** Our editor */
		JoshText jt;
		/** Our symbol table */
		JTable sTable;
		/** Our bottom display tabs */
		JTabbedPane dispTabs;
		/** Our user report */
		JTextArea userReport;
		/** Our user report */
		JTable errorTable;

		/** The tab containing our build messages. */
		JComponent buildMessageTab;
		/** The tab containing our user report. */
		JComponent userReportTab;

		/** Column names for our error table. */
		String errTableColNames[] = { "T", "Line", "Pos", "Message" };

		/** An error marker to inform the user of error positions. */
		ProblemMarker problemMarker;

		/**
		 * Default constructor; build tab UI
		 */
		FileTab() {
			JoshTextPanel jtp = new JoshTextPanel(null, false);

			jt = jtp.text;
			jt.setTokenMarker(new URBANhighlighter());
			jt.highlighters.add(0, problemMarker = new ProblemMarker());

			userReport = new JTextArea();
			userReport.setEditable(false);
			errorTable = new JTable(new DefaultTableModel(new String[0][4],
					errTableColNames));
			errorTable.getColumnModel().getColumn(0).setMaxWidth(16);
			errorTable.getColumnModel().getColumn(1).setMaxWidth(48);
			errorTable.getColumnModel().getColumn(2).setMaxWidth(48);
			errorTable.setFillsViewportHeight(true);

			errorTable.setRowSorter(new TableRowSorter<TableModel>(errorTable
					.getModel()));

			dispTabs = new JTabbedPane();
			dispTabs.add("Build Messages", buildMessageTab = new JScrollPane(
					errorTable));
			dispTabs.add("User Report", userReportTab = new JScrollPane(
					userReport));

			// Major hack to compensate for Swing's ABYSMAL table sizing API!
			sTable = new JTable(new DefaultTableModel(new String[0][],
					new Module.SymbolTable().toStringTable()[0])) {
				/** Shut up, ECJ. */
				private static final long serialVersionUID = 1L;

				@Override public boolean getScrollableTracksViewportWidth() {
					return getPreferredSize().width < getParent().getWidth();
				}
			};


			sTable.getColumnModel().getColumn(0).setMinWidth(64);
			sTable.getColumnModel().getColumn(1).setPreferredWidth(42);
			sTable.getColumnModel().getColumn(1).setMinWidth(42);
			sTable.getColumnModel().getColumn(1).setMaxWidth(64);
			sTable.getColumnModel().getColumn(2).setPreferredWidth(80);
			sTable.getColumnModel().getColumn(2).setMaxWidth(96);
			sTable.getColumnModel().getColumn(2).setMinWidth(80);
			sTable.getColumnModel().getColumn(3).setMinWidth(48);
			sTable.setRowSorter(new TableRowSorter<TableModel>(sTable
					.getModel()));

			JSplitPane sjsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jtp,
					dispTabs);
			this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			this.setLeftComponent(sjsp);
			this.setRightComponent(new JScrollPane(sTable));
			sTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sTable.setFillsViewportHeight(true);

			this.setOneTouchExpandable(true);
			sjsp.setOneTouchExpandable(true);
			this.setDividerLocation(540);
			sjsp.setDividerLocation(336);
			this.setResizeWeight(.7);
			sjsp.setResizeWeight(.8);
		}

		/**
		 * Compile the code in this tab.
		 * 
		 * @author Josh Ventura
		 * @date Apr 11, 2012; 11:38:15 PM
		 * @modified Apr 18, 2012; 3:40:07 AM: Added return statement of module.
		 * @return The module that was compiled. for reuse purposes.
		 */
		Module compile() {
			problems.clear();
			String str = jt.getText();
			Module mod = Assembler.parseString(str, new GUIErrorHandler());
			userReport.setText(mod.toString());
			String[][] rawTable = mod.getSymbolTable().toStringTable();
			((DefaultTableModel) sTable.getModel())
					.setRowCount(rawTable.length - 1);
			for (int i = 1; i < rawTable.length; i++) {
				for (int j = 0; j < rawTable[i].length; j++)
					sTable.setValueAt(rawTable[i][j], i - 1, j);
				sTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			}
			problemMarker.update();
			genErrorTable();
			if (problems.isEmpty())
				dispTabs.setSelectedComponent(userReportTab);
			else
				for (int i = 0; i < problems.size(); i++)
					if (problems.get(i).isError) {
						dispTabs.setSelectedComponent(buildMessageTab);
						break;
					}
			return mod;
		}

		/** The list of all problems to highlight and report. */
		ArrayList<EWMark> problems = new ArrayList<GUIMain.EWMark>();

		/**
		 * Draws a background behind lines containing errors.
		 * 
		 * @author Josh Ventura
		 * @date Apr 11, 2012; 5:39:53 PM
		 */
		class ProblemMarker implements Highlighter {
			/** The size of the code last time compile ran, in lines. */
			int codeSizeAtCompile = -1;

			/**
			 * Called at compile time to update any internal data.
			 * 
			 * @author Josh Ventura
			 * @date Apr 12, 2012; 10:21:13 AM
			 * @modified UNMODIFIED
			 * @tested UNTESTED
			 * @errors NO ERRORS REPORTED
			 * @codingStandards Awaiting signature
			 * @testingStandards Awaiting signature
			 * @specRef N/A
			 */
			public void update() {
				codeSizeAtCompile = jt.getLineCount();
			}

			/**
			 * Draw our backgrounds.
			 * 
			 * @see org.lateralgm.joshedit.JoshText.Highlighter#paint(java.awt.Graphics,
			 *      java.awt.Insets,
			 *      org.lateralgm.joshedit.JoshText.CodeMetrics, int, int)
			 */
			@Override public void paint(Graphics g, Insets i, CodeMetrics gm,
					int line_start, int line_end) {
				if (jt.getLineCount() != codeSizeAtCompile) {
					codeSizeAtCompile = -1;
					return;
				}
				for (EWMark problem : problems)
					if (problem.line != -1) {
						g.setColor(problem.isError ? errColor : warnColor);
						g.fillRect(g.getClipBounds().x,
								(problem.line - 1) * gm.lineHeight(),
								g.getClipBounds().width, gm.lineHeight());
						if (problem.pos != -1) {
							g.setColor(problem.isError ? errCharColor
									: warnCharColor);
							g.drawRect(jt.line_wid_at(problem.line - 1,
									problem.pos),
									(problem.line - 1) * gm.lineHeight(), gm
											.glyphWidth(), gm.lineHeight());
						}
					}
			}
		}

		/**
		 * Generate our table of error messages for this source.
		 * 
		 * @author Josh Ventura
		 * @date Apr 12, 2012; 12:10:24 AM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @specRef N/A
		 */
		void genErrorTable() {
			DefaultTableModel dtm = (DefaultTableModel) errorTable.getModel();
			dtm.setRowCount(problems.size());
			for (int i = 0; i < problems.size(); i++) {
				EWMark p = problems.get(i);
				dtm.setValueAt(p.isError ? "E" : "W", i, 0);
				dtm.setValueAt(p.line, i, 1);
				dtm.setValueAt(Integer.toString(p.pos), i, 2);
				dtm.setValueAt(p.message, i, 3);
			}

			RowSorter<? extends TableModel> sorter = errorTable.getRowSorter();
			List<TableRowSorter.SortKey> sortKeys = new ArrayList<TableRowSorter.SortKey>();
			sortKeys.add(new TableRowSorter.SortKey(1, SortOrder.ASCENDING));
			sorter.setSortKeys(sortKeys);

			System.out.println(errorTable.getModel().getClass().getName());
		}

		/**
		 * Error reporter that prints error messages to the user report and
		 * marks them.
		 * 
		 * @author Josh Ventura
		 * @date Apr 11, 2012; 3:06:25 PM
		 */
		public class GUIErrorHandler implements ErrorHandler {

			/**
			 * Adds an error marker to our editor.
			 * 
			 * @see assemblernator.ErrorReporting.ErrorHandler#reportError(java.lang.String,int,int)
			 */
			@Override public void reportError(String err, int line, int pos) {
				problems.add(new EWMark(true, err, line, pos));
			}

			/**
			 * Adds a warning marker to our editor.
			 * 
			 * @see assemblernator.ErrorReporting.ErrorHandler#reportWarning(java.lang.String,int,int)
			 */
			@Override public void reportWarning(String warn, int line, int pos) {
				problems.add(new EWMark(false, warn, line, pos));
			}

		}
	}

	/**
	 * Compiles the active code, reporting any errors.
	 * 
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 10:41:57 PM
	 * @modified UNMODIFIED
	 * @tested UNTESTED
	 * @errors NO ERRORS REPORTED
	 * @codingStandards Awaiting signature
	 * @testingStandards Awaiting signature
	 * @specRef N/A
	 */
	void compileActive() {
		FileTab ft = (FileTab) tabPane.getSelectedComponent();
		if (ft != null)
			ft.compile();
	}

	/**
	 * Class to handle menu selections.
	 * 
	 * @author Josh Ventura
	 * @date Apr 9, 2012; 12:56:30 AM
	 */
	class MenuListener implements ActionListener {

		/**
		 * Format compiler output as a test case.
		 * 
		 * @author Josh Ventura
		 * @date Apr 18, 2012; 3:41:55 AM
		 * @modified UNMODIFIED
		 * @return HTML test case report.
		 */
		String getTestCase() {
			FileTab ft = (FileTab) tabPane.getSelectedComponent();
			if (ft == null)
				return "";
			String res = "<h1>Input</h1>\n<pre>";
			Module m = ft.compile();
			if (m == null)
				return "";
			res += ft.jt.getHTML();
			res += "</pre>\n<br />\n";

			String[][] table = m.getSymbolTable().toStringTable();
			res += "<h1>Symbol table</h1>\n";
			res += "<table>\n";
			res += "  <tr>\n";
			for (int i = 0; i < table[0].length; i++)
				res += "    <th>" + table[0][i] + "</th>\n";
			res += "  </tr>";

			for (int i = 1; i < table.length; i++) {
				res += "  <tr>\n";
				for (int j = 0; j < table[i].length; j++)
					res += "    <td>" + JoshText.htmlSpecialChars(table[i][j]) + "</td>\n";
				res += "  </tr>\n";
			}
			res += "</table>\n<br />\n";

			res += "<h1>User report</h1>\n";
			res += "<pre>";
			res += m.toString();
			res += "</pre>\n";

			return res;
		}

		/** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
		@Override public void actionPerformed(ActionEvent e) {
			if (e.getSource() == compile) {
				compileActive();
				return;
			}
			if (e.getSource() == writeHTML) {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				if (jfc.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
					if (!f.exists()
							|| JOptionPane.showConfirmDialog(mainWindow,
									"Overwrite existing file?") == JOptionPane.YES_OPTION) {
						try {
							FileWriter fw = new FileWriter(f);
							FileTab ft = (FileTab) tabPane
									.getSelectedComponent();
							if (ft != null) {
								fw.write("<html>\n<body>\n<pre>");
								fw.write(ft.jt.getHTML());
								fw.write("</pre>\n</body>\n</html>\n");
							}
							fw.close();
						} catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(mainWindow,
									"Failed to open file.");
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(mainWindow,
									"Failed to write to file.");
						}
					}
				}
				return;
			}
			if (e.getSource() == writeHTMLTest) {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				if (jfc.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
					if (!f.exists()
							|| JOptionPane.showConfirmDialog(mainWindow,
									"Overwrite existing file?") == JOptionPane.YES_OPTION) {
						try {
							FileWriter fw = new FileWriter(f);
							FileTab ft = (FileTab) tabPane
									.getSelectedComponent();
							if (ft != null) {
								fw.write("<html>\n<body>\n");
								fw.write(getTestCase());
								fw.write("</body>\n</html>\n");
							}
							fw.close();
						} catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(mainWindow,
									"Failed to open file.");
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(mainWindow,
									"Failed to write to file.");
						}
					}
				}
			}
			if (e.getSource() == copyHTMLTest) {
			    StringSelection ss = new StringSelection(getTestCase());
			    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			}
			if (e.getSource() == exit) {
				System.exit(0);
				return;
			}
		}
	}

	/** The color used to highlight error lines. */
	static final Color errColor = new Color(255, 200, 200);
	/** The color used to highlight the character at which the error occurred. */
	static final Color errCharColor = new Color(255, 128, 128);
	/** The color used to highlight warning lines. */
	static final Color warnColor = new Color(255, 230, 128);
	/** The color used to highlight the character at which the warning occurred. */
	static final Color warnCharColor = new Color(255, 230, 128);

	/**
	 * Storage class to keep track of errors.
	 * 
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 5:53:44 PM
	 */
	class EWMark {
		/** True if this is an error, false if it is a warning. */
		boolean isError;
		/** The error or warning message. */
		String message;
		/** The line on which the error is reported */
		int line;
		/** The position of the error reported. */
		int pos;

		/**
		 * @param isErr
		 *            True if this is an error, false if it is a warning.
		 * @param msg
		 *            The error or warning message.
		 * @param ln
		 *            The line number concerned.
		 * @param p
		 *            The position of interest in the line, or -1 if the whole
		 *            line is concerned.
		 */
		EWMark(boolean isErr, String msg, int ln, int p) {
			isError = isErr;
			message = msg;
			line = ln;
			pos = p;
		}
	}
}
