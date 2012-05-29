package guinator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import simulanator.SimulatorTest;
import assemblernator.AssemblerTest;
import assemblernator.Linker;
import assemblernator.LinkerModule;
import assemblernator.LinkerTest;
import assemblernator.Module;

/**
 * @author Josh Ventura
 * @date Apr 8, 2012; 5:53:40 PM
 */
public class GUIMain {

	// ===============================================================
	// == MENU ITEMS =================================================
	// ===============================================================
	/** Our New menu item */
	static JMenuItem m_new;
	/** Our Save menu item */
	static JMenuItem m_save;
	/** Our Save As menu item */
	static JMenuItem m_saveAs;
	/** Our Load menu item */
	static JMenuItem m_load;
	/** Our Parse menu item */
	static JMenuItem m_parse;
	/** Our Compile menu item */
	static JMenuItem m_compile;
	/** Our Compile menu item */
	static JMenuItem m_run;
	/** Menu item to generate a complete test case */
	static JMenuItem m_fulltestcase;
	/** A menu item to export the file as an HTML document */
	static JMenuItem m_writeHTML;
	/** A menu item to copy code, with HTML highlighting. */
	static JMenuItem m_copyFormatted;
	/** A menu item to export a test case as an HTML document */
	static JMenuItem m_writeHTMLTest;
	/** A menu item to copy a test case to the clipboard as HTML */
	static JMenuItem m_copyHTMLTest;
	/** Our exit menu item */
	static JMenuItem m_exit;

	/** Button to create a new linker tab. */
	static JMenuItem m_newlinker;
	/** A menu item to export a test case as an HTML document */
	static JMenuItem m_writeHTMLTest2;
	/** A menu item to copy a test case to the clipboard as HTML */
	static JMenuItem m_copyHTMLTest2;

	/** Button to create a new simulator tab. */
	static JMenuItem m_newsimulator;
	/** A menu item to export a test case as an HTML document */
	static JMenuItem m_writeHTMLTest3;
	/** A menu item to copy a test case to the clipboard as HTML */
	static JMenuItem m_copyHTMLTest3;

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
		tabPane.addMouseListener(new MouseListener() {
			@Override public void mouseClicked(MouseEvent event) {}

			@Override public void mouseEntered(MouseEvent e) {}

			@Override public void mouseExited(MouseEvent e) {}

			@Override public void mousePressed(MouseEvent e) {}

			@Override public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					System.out.println("Pressed");
					e.consume();
					if (e.getSource() instanceof JTabbedPane) {
						System.out.println("Tabbed pane");
						JTabbedPane jtp = (JTabbedPane) e.getSource();
						jtp.remove(jtp.getSelectedComponent());
					}
				}
			}
		});

		mainWindow.add(tabPane);
		mainWindow.setSize(720, 540);

		JMenuBar mb = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		GUIMenuListener ml = new GUIMenuListener();
		filemenu.add(m_new = new JMenuItem("New"));
		m_new.addActionListener(ml);
		filemenu.add(m_save = new JMenuItem("Save"));
		m_save.addActionListener(ml);
		filemenu.add(m_saveAs = new JMenuItem("Save As"));
		m_saveAs.addActionListener(ml);
		filemenu.add(m_load = new JMenuItem("Load"));
		m_load.addActionListener(ml);
		filemenu.addSeparator();

		filemenu.add(m_parse = new JMenuItem("Parse"));
		m_parse.addActionListener(ml);
		filemenu.add(m_compile = new JMenuItem("Compile"));
		m_compile.addActionListener(ml);
		filemenu.add(m_run = new JMenuItem("Run in New Simulator"));
		m_run.addActionListener(ml);
		filemenu.add(m_fulltestcase = new JMenuItem("Run NOW and copy"));
		m_fulltestcase.addActionListener(ml);
		filemenu.addSeparator();

		filemenu.add(m_writeHTML = new JMenuItem("Export as HTML"));
		m_writeHTML.addActionListener(ml);
		filemenu.add(m_copyFormatted = new JMenuItem(
				"Copy code with highlighting"));
		m_copyFormatted.addActionListener(ml);
		filemenu.add(m_writeHTMLTest = new JMenuItem("Write HTML Test Case"));
		m_writeHTMLTest.addActionListener(ml);
		filemenu.add(m_copyHTMLTest = new JMenuItem("Copy HTML Test Case"));
		m_copyHTMLTest.addActionListener(ml);
		filemenu.addSeparator();
		filemenu.add(m_exit = new JMenuItem("Exit"));
		m_exit.addActionListener(ml);


		JMenu linkmenu = new JMenu("Link");
		linkmenu.add(m_newlinker = new JMenuItem("New Linker Tab"));
		m_newlinker.addActionListener(ml);
		linkmenu.add(m_writeHTMLTest2 = new JMenuItem("Write HTML Test Case"));
		m_writeHTMLTest2.addActionListener(ml);
		linkmenu.add(m_copyHTMLTest2 = new JMenuItem("Copy HTML Test Case"));
		m_copyHTMLTest2.addActionListener(ml);

		JMenu simumenu = new JMenu("Simulator");
		simumenu.add(m_newsimulator = new JMenuItem("New Simulator Tab"));
		m_newsimulator.addActionListener(ml);
		simumenu.add(m_writeHTMLTest3 = new JMenuItem("Write HTML Test Case"));
		m_writeHTMLTest3.addActionListener(ml);
		simumenu.add(m_copyHTMLTest3 = new JMenuItem("Copy HTML Test Case"));
		m_copyHTMLTest3.addActionListener(ml);


		mb.add(filemenu);
		mb.add(linkmenu);
		mb.add(simumenu);
		mainWindow.setJMenuBar(mb);

		filemenu.addMenuListener(new MenuListener() {

			@Override public void menuSelected(MenuEvent arg0) {
				Component tab = tabPane.getSelectedComponent();
				System.out.println("assess asses");
				if (tab instanceof FileTab) {
					m_parse.setEnabled(true);
					m_compile.setEnabled(true);
					m_run.setEnabled(true);
					m_fulltestcase.setEnabled(true);
					m_copyFormatted.setEnabled(true);
					m_writeHTML.setEnabled(true);
				}
				else {
					m_parse.setEnabled(false);
					m_compile.setEnabled(false);
					m_run.setEnabled(false);
					m_fulltestcase.setEnabled(false);
					m_copyFormatted.setEnabled(false);
					m_writeHTML.setEnabled(false);
				}
			}

			@Override public void menuDeselected(MenuEvent arg0) {}

			@Override public void menuCanceled(MenuEvent arg0) {}
		});

		FileTab ft = new FileTab(tabPane);
		tabPane.addTab("Untitled", ft);
		LinkerTab lt = new LinkerTab();
		tabPane.addTab("Link", lt);
		SimulatorTab st = new SimulatorTab();
		tabPane.addTab("Run", st);
		mainWindow.setVisible(true);
		ft.jt.grabFocus();
	}

	/**
	 * Parses the active code, reporting any errors.
	 * 
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 10:41:57 PM
	 */
	void parseActive() {
		FileTab ft = (FileTab) tabPane.getSelectedComponent();
		if (ft == null)
			return;
		ft.compile();
	}

	/**
	 * Compiles the active code, reporting any errors, then builds
	 * an object file.
	 * 
	 * @author Josh Ventura
	 * @date Apr 11, 2012; 10:41:57 PM
	 */
	void compileActive() {
		FileTab ft = (FileTab) tabPane.getSelectedComponent();
		Module rm;
		if (ft == null)
			return;
		rm = ft.compile();

		if (ft.getFileName() == null) {
			String fn = GUIUtil.getSaveFname(mainWindow,
					"URBAN Assembly Files", ".s", ".uls");
			if (fn == null)
				return;
			ft.setFileName(fn);
		}

		String ofname = ft.getFileName();
		String cofn = ofname.toLowerCase();
		if (cofn.endsWith(".s"))
			ofname = ofname.substring(0, ofname.length() - 1) + "ulo";
		else if (cofn.endsWith(".uls"))
			ofname = ofname.substring(0, ofname.length() - 3) + "ulo";
		else
			ofname = ofname + ".ulo";

		try {
			rm.writeObjectFile(ofname);
		} catch (Exception e) {
			GUIUtil.showException(
					"An error occurred during object file generation", e,
					mainWindow);
		}
	}

	/**
	 * Compiles the active code, reporting any errors, then builds an object
	 * file in memory, "links" it to itself, and loads it into RAM in the
	 * simulator tab.
	 * 
	 * @author Josh Ventura
	 * @date May 24, 2012; 8:11:52 PM
	 */
	void runActive() {
		FileTab ft = (FileTab) tabPane.getSelectedComponent();

		if (ft == null)
			return;
		Module rm = ft.compile();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			rm.writeObjectFile(baos);

			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			LinkerModule lm = new LinkerModule(new Scanner(bais), ft.hErr);
			if (!lm.success) {
				GUIUtil.showError("General failure; bailing.", mainWindow);
				return;
			}

			baos = new ByteArrayOutputStream();
			Linker.link(new LinkerModule[] { lm }, baos, ft.hErr);

			SimulatorTab st = new SimulatorTab();
			tabPane.insertTab("Quick Run", null, st,
					"Running " + ft.getFileName(),
					tabPane.getComponentZOrder(ft) + 1);

			bais = new ByteArrayInputStream(baos.toByteArray());
			st.loadStream(bais);

			tabPane.setSelectedComponent(st);
		} catch (Exception e) {
			GUIUtil.showException(
					"An error occurred during object file generation", e,
					mainWindow);
			return;
		}
	}

	/**
	 * Compiles the active code, reporting any errors, then builds an object
	 * file in memory, "links" it to itself, and loads it into RAM in the
	 * simulator tab.
	 * 
	 * @author Josh Ventura
	 * @return The complete test case for this run.
	 * @date May 24, 2012; 8:11:52 PM
	 */
	String makeBigTestCase() {
		FileTab ft = (FileTab) tabPane.getSelectedComponent();

		if (ft == null)
			return "";

		String tcHTML = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Module mods[] = new Module[1];
			baos = new ByteArrayOutputStream();
			tcHTML = getBasicTestCase(mods, baos);

			// This is already printed by our previous test case generator
			byte objectFile[] = baos.toByteArray();

			LinkerModule[] lma = new LinkerModule[1];
			lma[0] = new LinkerModule(new Scanner(new ByteArrayInputStream(
					objectFile)), ft.hErr);

			if (!lma[0].success) {
				GUIUtil.showError("General failure; bailing.", mainWindow);
				return tcHTML
						+ lma[0].toString()
						+ "\n<br/>\n<br/>\n<p>Testing bailed due to prior errors.</p>";
			}

			baos = new ByteArrayOutputStream();
			tcHTML += LinkerTest.getTestCase(lma, baos);

			byte[] loaderFile = baos.toByteArray();


			tcHTML += "\n\n<h1>Simulator Output</h1>\n\n"
					+ SimulatorTest.getTestCase(loaderFile, mainWindow);

			return tcHTML;

		} catch (Exception e) {
			GUIUtil.showException(
					"An error occurred during object file generation", e,
					mainWindow);
			return tcHTML
					+ "\n\n<br/><br/><p>Bailing due to program errors. <i>This is bad</i>.</p>";
		}
	}

	/**
	 * Format compiler output as a test case.
	 * 
	 * @author Josh Ventura
	 * @date Apr 18, 2012; 3:41:55 AM
	 * @modified UNMODIFIEDO
	 * @param retmod
	 *            Array in which to output module; null for no return.
	 * @param objectfile
	 *            Output parameter for object file; null to use its own.
	 * @return HTML test case report.
	 */
	String getBasicTestCase(Module retmod[], ByteArrayOutputStream objectfile) {
		Component a = tabPane.getSelectedComponent();
		if (a == null)
			return "";

		if (a instanceof FileTab) {
			FileTab ft = (FileTab) a;
			String res = "<h1>Input</h1>\n<pre>";

			Module m = ft.compile();
			if (retmod != null)
				retmod[0] = m;

			if (m == null)
				return "";

			res += ft.jt.getHTML();
			res += "</pre>\n<br />\n";
			res += AssemblerTest.getTestCase(m, objectfile);

			return res;
		}
		else if (a instanceof LinkerTab) {
			LinkerTab lt = (LinkerTab) a;
			return LinkerTest.getTestCase(
					lt.linkMods.toArray(new LinkerModule[0]), null);
		}
		else if (a instanceof SimulatorTab) {
			SimulatorTab st = (SimulatorTab) a;
			return st.getOutputHTML();
		}
		else {
			GUIUtil.showError(
					"A test case cannot be generated for the current tab.",
					mainWindow);
		}

		return "";
	}

	/**
	 * Class to handle menu selections.
	 * 
	 * @author Josh Ventura
	 * @date Apr 9, 2012; 12:56:30 AM
	 */
	class GUIMenuListener implements ActionListener {


		/** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
		@Override public void actionPerformed(ActionEvent e) {
			if (e.getSource() == m_new) {
				FileTab ft = new FileTab(tabPane);
				tabPane.addTab("Untitled", ft);
				tabPane.setSelectedComponent(ft);
				return;
			}
			if (e.getSource() == m_save || e.getSource() == m_saveAs) {
				FileTab ft = (FileTab) tabPane.getSelectedComponent();
				if (ft.getFileName() == null || e.getSource() == m_saveAs) {
					String n = GUIUtil.getSaveFname(mainWindow,
							"URBAN Assembly Files", ".s", ".uls");
					if (n == null)
						return;
					ft.setFileName(n);
				}
				try {
					FileWriter fw = new FileWriter(ft.getFileName());
					fw.write(ft.jt.getText());
					fw.close();
				} catch (FileNotFoundException e1) {
					GUIUtil.showException(
							"An error occurred while trying to save the file",
							e1, mainWindow);
				} catch (IOException e2) {
					GUIUtil.showException(
							"An error occurred while trying to save the file",
							e2, mainWindow);
				}
				return;
			}
			if (e.getSource() == m_load) {
				String fn = GUIUtil.getLoadFname(mainWindow,
						"URBAN Assembly Files", ".s", ".uls");
				if (fn == null)
					return;
				try {
					File n = new File(fn);
					Scanner fw = new Scanner(n);
					FileTab ft = new FileTab(tabPane);
					ArrayList<String> lines = new ArrayList<String>();
					while (fw.hasNextLine())
						lines.add(fw.nextLine());
					ft.jt.setText(lines.toArray(new String[0]));
					ft.setFileName(n.getAbsolutePath());
					tabPane.add(n.getName(), ft);
					tabPane.setSelectedComponent(ft);
					fw.close();
				} catch (FileNotFoundException e1) {
					GUIUtil.showException(
							"An error occurred while trying to open the file",
							e1, mainWindow);
				}
				return;
			}
			if (e.getSource() == m_parse) {
				parseActive();
				return;
			}
			if (e.getSource() == m_compile) {
				compileActive();
				return;
			}
			if (e.getSource() == m_run) {
				runActive();
				return;
			}
			if (e.getSource() == m_fulltestcase) {
				StringSelection ss = new StringSelection(makeBigTestCase());
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(ss, null);
				return;
			}
			if (e.getSource() == m_writeHTML) {
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
			if (e.getSource() == m_writeHTMLTest
					|| e.getSource() == m_writeHTMLTest2
					|| e.getSource() == m_writeHTMLTest3) {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				if (jfc.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
					if (!f.exists()
							|| JOptionPane.showConfirmDialog(mainWindow,
									"Overwrite existing file?") == JOptionPane.YES_OPTION) {
						try {
							FileWriter fw = new FileWriter(f);
							fw.write("<html>\n<body>\n");
							fw.write(getBasicTestCase(null, null));
							fw.write("</body>\n</html>\n");
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
			if (e.getSource() == m_copyHTMLTest
					|| e.getSource() == m_copyHTMLTest2
					|| e.getSource() == m_copyHTMLTest3) {
				StringSelection ss = new StringSelection(getBasicTestCase(null,
						null));
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(ss, null);
				return;
			}
			if (e.getSource() == m_copyFormatted) {
				FileTab ft = (FileTab) tabPane.getSelectedComponent();
				if (ft != null) {
					HtmlSelection htmls = new HtmlSelection(ft.jt.getHTML());
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(htmls, null);
				}
				return;
			}
			if (e.getSource() == m_exit) {
				System.exit(0);
				return;
			}
			
			if (e.getSource() == m_newlinker) {
				LinkerTab lt = new LinkerTab();
				tabPane.add("Linker",lt);
				tabPane.setSelectedComponent(lt);
				return;
			}
			
			if (e.getSource() == m_newsimulator) {
				SimulatorTab lt = new SimulatorTab();
				tabPane.add("Simulator",lt);
				tabPane.setSelectedComponent(lt);
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
	 * @author elliotth
	 * @date May 6, 2012; 11:55:13 PM
	 */
	public static class HtmlSelection implements Transferable {
		/** Our flavors... */
		private static ArrayList<DataFlavor> htmlFlavors = new ArrayList<DataFlavor>();
		static {
			try {
				htmlFlavors.add(new DataFlavor(
						"text/html;class=java.lang.String"));
				htmlFlavors
						.add(new DataFlavor("text/html;class=java.io.Reader"));

			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		/** Our HTML text */
		private String html;

		/**
		 * @param html
		 *            The HTML-formatted text to transfer.
		 */
		public HtmlSelection(String html) {
			this.html = html;
		}


		/** @see java.awt.datatransfer.Transferable#getTransferDataFlavors() */
		@Override public DataFlavor[] getTransferDataFlavors() {
			return (DataFlavor[]) htmlFlavors
					.toArray(new DataFlavor[htmlFlavors.size()]);
		}


		/** @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor) */
		@Override public boolean isDataFlavorSupported(DataFlavor flavor) {
			return htmlFlavors.contains(flavor);
		}


		/** @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor) */
		@Override public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (String.class.equals(flavor.getRepresentationClass()))
				return html;
			else if (Reader.class.equals(flavor.getRepresentationClass()))
				return new StringReader(html);
			throw new UnsupportedFlavorException(flavor);
		}
	}
}
