package guinator;

import java.awt.Graphics;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
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

	/** The tab pane in which we are enclosed */
	JTabbedPane tabPane;

	/** The name to which this file is saved */
	private String mahFileName = null;

	/**
	 * @return The absolute path and filename of the file open in this tab,
	 *         or null if no name is set.
	 */
	String getFileName() {
		return mahFileName;
	}

	/**
	 * @param fn
	 *            The new filename.
	 */
	void setFileName(String fn) {
		mahFileName = fn;
		int z = tabPane.getComponentZOrder(this);
		if (z != -1)
			tabPane.setTitleAt(z, new File(fn).getName());
	}

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
	 * 
	 * @param jtabp
	 *            The tab pane that will contain this.
	 */
	FileTab(JTabbedPane jtabp) {
		tabPane = jtabp;
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

		TableRowSorter<TableModel> trs;
		trs = new TableRowSorter<TableModel>(errorTable.getModel());
		Comparator<Integer> cmp = new Comparator<Integer>() {
			@Override public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		};
		trs.setComparator(1, cmp);
		errorTable.setRowSorter(trs);

		dispTabs = new JTabbedPane();
		dispTabs.add("Build Messages", buildMessageTab = new JScrollPane(
				errorTable));
		dispTabs.add("User Report", userReportTab = new JScrollPane(userReport));

		// Major hack to compensate for Swing's ABYSMAL table sizing API!
		sTable = new FullTable();


		sTable.getColumnModel().getColumn(0).setMinWidth(64);
		sTable.getColumnModel().getColumn(1).setPreferredWidth(42);
		sTable.getColumnModel().getColumn(1).setMinWidth(42);
		sTable.getColumnModel().getColumn(1).setMaxWidth(64);
		sTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		sTable.getColumnModel().getColumn(2).setMaxWidth(96);
		sTable.getColumnModel().getColumn(2).setMinWidth(80);
		sTable.getColumnModel().getColumn(3).setMinWidth(48);
		sTable.setRowSorter(new TableRowSorter<TableModel>(sTable.getModel()));

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
	ArrayList<EWMark> problems = new ArrayList<EWMark>();

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
		 *      java.awt.Insets, org.lateralgm.joshedit.JoshText.CodeMetrics,
		 *      int, int)
		 */
		@Override public void paint(Graphics g, Insets i, CodeMetrics gm,
				int line_start, int line_end) {
			if (jt.getLineCount() != codeSizeAtCompile) {
				codeSizeAtCompile = -1;
				return;
			}
			for (EWMark problem : problems)
				if (problem.line != -1) {
					g.setColor(problem.isError ? GUIMain.errColor
							: GUIMain.warnColor);
					g.fillRect(g.getClipBounds().x,
							(problem.line - 1) * gm.lineHeight(),
							g.getClipBounds().width, gm.lineHeight());
					if (problem.pos != -1) {
						g.setColor(problem.isError ? GUIMain.errCharColor
								: GUIMain.warnCharColor);
						g.drawRect(
								jt.line_wid_at(problem.line - 1, problem.pos),
								(problem.line - 1) * gm.lineHeight(),
								gm.glyphWidth(), gm.lineHeight());
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