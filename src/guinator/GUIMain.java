package guinator;

import guinator.GUIMain.FileTab.ProblemMarker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.lateralgm.joshedit.JoshText;
import org.lateralgm.joshedit.JoshText.CodeMetrics;
import org.lateralgm.joshedit.JoshText.Marker;
import org.lateralgm.joshedit.QuickFind;

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
	/** Our exit menu item */
	static JMenuItem exit;

	// ===============================================================
	// == COMPONENTS =================================================
	// ===============================================================
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

		JFrame mainWindow = new JFrame();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabPane = new JTabbedPane();

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

		/** Column names for our error table. */
		String errTableColNames[] = { "T", "Line", "Pos", "Message" };

		/** An error marker to inform the user of error positions. */
		ProblemMarker problemMarker;

		/**
		 * Default constructor; build tab UI
		 */
		FileTab() {
			jt = new JoshText();
			JScrollPane sp = new JScrollPane(jt);
			QuickFind qf = new QuickFind(jt);
			JPanel jp = new JPanel();

			jt.finder = qf;
			jt.markers.add(0, problemMarker = new ProblemMarker());


			jp.setLayout(new BoxLayout(jp, BoxLayout.PAGE_AXIS));
			jp.add(sp);
			jp.add(qf);

			userReport = new JTextArea();
			userReport.setEditable(false);
			errorTable = new JTable(new DefaultTableModel(new String[0][4],
					errTableColNames));
			errorTable.getColumnModel().getColumn(0).setMaxWidth(16);
			errorTable.getColumnModel().getColumn(1).setMaxWidth(48);
			errorTable.getColumnModel().getColumn(2).setMaxWidth(48);
			dispTabs = new JTabbedPane();
			dispTabs.add("Build Messages", new JScrollPane(errorTable));
			dispTabs.add("User Report", new JScrollPane(userReport));

			sTable = new JTable();

			JSplitPane sjsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jp,
					dispTabs);
			this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			this.setLeftComponent(sjsp);
			this.setRightComponent(new JScrollPane(sTable));

			this.setOneTouchExpandable(true);
			sjsp.setOneTouchExpandable(true);
			this.setDividerLocation(540);
			sjsp.setDividerLocation(360);
			this.setResizeWeight(.7);
			sjsp.setResizeWeight(.8);
		}

		/**
		 * Compile the code in this tab.
		 * 
		 * @author Josh Ventura
		 * @date Apr 11, 2012; 11:38:15 PM
		 * @modified UNMODIFIED
		 * @tested UNTESTED
		 * @errors NO ERRORS REPORTED
		 * @codingStandards Awaiting signature
		 * @testingStandards Awaiting signature
		 * @specRef N/A
		 */
		void compile() {
			problems.clear();
			String str = jt.getText();
			Module mod = Assembler.parseString(str, new GUIErrorHandler());
			userReport.setText(mod.toString());
			String rawTable = mod.getSymbolTable().toString();
			String[] allData = rawTable.trim().split("(\r\n|\n|\r)");
			if (allData != null && allData.length > 0) {
				String[] columnNames = allData[0].split("\\:\t");
				String[][] data = new String[allData.length - 1][columnNames.length];
				for (int i = 1; i < allData.length; i++) {
					String dc[] = allData[i].split("\t");
					int iw;
					for (iw = 0; iw < dc.length && iw < data[i - 1].length; iw++)
						data[i - 1][iw] = dc[iw];
					while (iw < data[i - 1].length)
						data[i - 1][iw++] = "";
				}
				sTable.setModel(new DefaultTableModel(data, columnNames));
				sTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			}
			else
				sTable.setModel(null);
			problemMarker.update();
			genErrorTable();
		}

		/** The list of all problems to highlight and report. */
		ArrayList<EWMark> problems = new ArrayList<GUIMain.EWMark>();

		/**
		 * Draws a background behind lines containing errors.
		 * 
		 * @author Josh Ventura
		 * @date Apr 11, 2012; 5:39:53 PM
		 */
		class ProblemMarker implements Marker {
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
			 * @see org.lateralgm.joshedit.JoshText.Marker#paint(java.awt.Graphics,
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
		/** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
		@Override public void actionPerformed(ActionEvent e) {
			if (e.getSource() == compile) {
				compileActive();
				return;
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
