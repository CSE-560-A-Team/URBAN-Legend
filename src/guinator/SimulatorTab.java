package guinator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import simulanator.Machine;
import simulanator.Machine.URBANInputStream;
import simulanator.Machine.URBANOutputStream;
import simulanator.Simulator;
import assemblernator.IOFormat;

/**
 * Tab containing a simulator pane.
 * 
 * @author Josh Ventura
 * @date May 11, 2012; 12:06:23 AM
 */
public class SimulatorTab extends JSplitPane {
	/** Make ECJ shut up. */
	private static final long serialVersionUID = 1L;

	/** Hex editor pane for our memory. */
	private JTable memTable;

	/** A table of all active threads */
	private JTable threadList;

	/** Our running toolbar */
	private JToolBar toolbar;
	/** The load button */
	private JButton loadButton;
	/** Button to reset the module */
	private JButton runButton;
	/** How long to sleep after each instruction */
	private JSpinner delaySpinbox;

	/** The text box into which the user can type */
	private JLTextField inputField;
	/** Output Stream Display */
	private IOPane outputBox;

	/** The machine we are running in this simulator. */
	Machine machine;

	/**
	 * @author Josh Ventura
	 * @date May 22, 2012; 4:34:04 PM
	 */
	class JLTextField extends JTextField implements ActionListener {
		/** Shut up, ECJ */
		private static final long serialVersionUID = 1L;
		/** True if this lock is set */
		private boolean isLocked = false;
		/** Assigned true when the user presses enter */
		public boolean submitted;

		/** Grab the lock */
		public synchronized void lock() {
			while (isLocked) {
				try {
					wait();
				} catch (InterruptedException e) {}
			}
			isLocked = true;
		}

		/** Release the lock */
		public synchronized void unlock() {
			isLocked = false;
			notify();
		}

		/** Enter pressed */
		@Override public void actionPerformed(ActionEvent e) {
			submitted = true;
		}

		/** Default constructor */
		public JLTextField() {
			super();
			addActionListener(this);
		}
	}

	/** Default constructor. Lays out the UI. */
	public SimulatorTab() {

		// ===================================================================
		// == Top Panel ======================================================
		// ===================================================================

		String columns[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		String rows[][] = new String[4096 / 16][16];
		for (int row = 0; row < rows.length; row++)
			for (int col = 0; col < 16; ++col)
				rows[row][col] = "00000000";

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

		memTable = new FullTable(rows, columns);
		JScrollPane scrollPane = new JScrollPane(memTable);
		RowNumberTable rnt = new RowNumberTable(memTable);
		memTable.setShowGrid(false);
		memTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		memTable.setFont(new Font(Font.MONOSPACED, 0, 12));

		GUIUtil.packTable(memTable);

		scrollPane.setRowHeaderView(rnt);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
				rnt.getTableHeader());

		JPanel registerGrid = new JPanel();
		GridLayout registerLayout = new GridLayout(8, 2);
		registerGrid.setLayout(registerLayout);
		registerGrid.add(new JTextField("Register 0"));
		JTextField dead = new JTextField("");
		registerGrid.add(dead);
		dead.setEnabled(false);
		for (int i = 1; i < 8; ++i) {
			registerGrid.add(new JTextField("Register " + i));
			registerGrid.add(new JTextField("Index Register " + i));
		}

		JPanel otherStats = new JPanel();
		otherStats.setLayout(new BoxLayout(otherStats, BoxLayout.Y_AXIS));
		otherStats.add(registerGrid);
		otherStats.add(new JScrollPane(threadList = new JTable(
				new DefaultTableModel(new String[] { "ID", "LC" }, 0))));
		threadList.setFillsViewportHeight(true);

		otherStats.setMaximumSize(new Dimension(216, Integer.MAX_VALUE));
		otherStats.setPreferredSize(new Dimension(216, -1));
		top.add(scrollPane);
		top.add(otherStats);

		setOrientation(VERTICAL_SPLIT);


		// ===================================================================
		// == Bottom Panel ===================================================
		// ===================================================================

		JPanel bottom = new JPanel();
		toolbar = new JToolBar();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		toolbar.add(loadButton = new JButton("Load"));
		toolbar.add(runButton = new JButton("Run"));
		toolbar.add(delaySpinbox = new JSpinner(new SpinnerNumberModel(400, 0,
				4000, 10)));
		toolbar.add(inputField = new JLTextField());
		toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		bottom.add(new JScrollPane(outputBox = new IOPane()));
		toolbar.setFloatable(false);
		bottom.add(toolbar);

		setLeftComponent(top);
		setRightComponent(bottom);

		SimulatorListener sl = new SimulatorListener();
		loadButton.addActionListener(sl);
		runButton.addActionListener(sl);
		memTable.setCellSelectionEnabled(true);
		memTable.setRowSelectionAllowed(true);


		// ===================================================================
		// == The Machine ====================================================
		// ===================================================================

		machine = new Machine(outputBox.hErr, inputStream, outputStream);
		machine.addThreadListener(myUIUpdater);
		machine.addMemoryListener(myUIUpdater);
		machine.addRegisterListener(myUIUpdater);
		inputField.setEnabled(false);
		setOneTouchExpandable(true);
		setDividerLocation(320);
		setResizeWeight(.9);
	}


	/**
	 * @author Josh Ventura
	 * @date May 21, 2012; 11:37:40 PM
	 */
	public class SimulatorListener implements ActionListener {
		/** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
		@Override public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loadButton) {
				String fname = GUIUtil.getLoadFname(SimulatorTab.this,
						"URBAN Legend Executable Files", ".ulx");
				if (fname == null)
					return;
				try {
					InputStream is = new FileInputStream(new File(fname));
					Simulator.load(is, outputBox.hErr, machine);
					for (int addr = 0; addr < Machine.memorySizeInWords; ++addr)
						memTable.setValueAt(
								IOFormat.formatHexInteger(
										machine.getMemory(addr), 8), addr / 16,
								addr % 16);
				} catch (Exception ex) {
					GUIUtil.showException("Error loading executable file", ex,
							SimulatorTab.this);
				}
			}
			else if (e.getSource() == runButton) {
				outputBox.append("<i>Starting main thread...</i><br/>");
				machine.runThread(machine.getLC());
			}
		}
	}

	/**
	 * @author Josh Ventura
	 * @date May 22, 2012; 2:31:27 PM
	 */
	class OutputPrinter implements URBANOutputStream {
		/** @see simulanator.Machine.URBANOutputStream#putString(java.lang.String) */
		@Override public void putString(String str) {
			outputBox.appendPlain(str);
		}
	}

	/** Our output printer. */
	OutputPrinter outputStream = new OutputPrinter();

	/**
	 * @author Josh Ventura
	 * @date May 22, 2012; 2:31:27 PM
	 */
	class InputReader implements URBANInputStream {
		/** @see simulanator.Machine.URBANInputStream#getString() */
		@Override public String getString() {
			inputField.lock();
			try {
				inputField.setEnabled(true);
				while (!inputField.submitted)
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				String ret = inputField.getText();
				inputField.setText("");
				inputField.setEnabled(false);
				return ret;
			} finally {
				inputField.unlock();
			}
		}
	}

	/** Our input stream. */
	InputReader inputStream = new InputReader();

	/**
	 * Updates the UI as our machine state changes.
	 * 
	 * @author Josh Ventura
	 * @date May 23, 2012; 2:50:52 PM
	 */
	class UIUpdater implements Machine.ThreadListener, Machine.MemoryListener,
			Machine.RegisterListener {

		/** Allows us to pause or sleep */
		@Override public void fetchDecodeExecute() {
			int d = (Integer) delaySpinbox.getValue();
			if (d > 0)
				try {
					Thread.sleep(d);
				} catch (InterruptedException e) {}
		}

		/** Update register display */
		@Override public void updatedRegisters(boolean index,
				int firstRegister, int lastRegister) {
			// TODO Auto-generated method stub

		}

		/** Update memory table */
		@Override public void updatedMemory(int startAddr, int endAddr) {
			for (int addr = startAddr; addr < endAddr; ++addr) {
				memTable.setValueAt(
						IOFormat.formatHexInteger(machine.getMemory(addr), 8),
						addr / 16, addr % 16);
			}
		}

		/** Update thread location counter */
		@Override public void updatedLC(int threadID, int newlc) {
			DefaultTableModel tm = (DefaultTableModel) threadList.getModel();
			if (threadID >= 0 && threadID < tm.getRowCount()) {
				tm.setValueAt(threadID, threadID, 0);
				tm.setValueAt(newlc, threadID, 1);
			}
		}

		/** Add new thread to our list */
		@Override public void createThread(int threadID) {
			DefaultTableModel tm = (DefaultTableModel) threadList.getModel();
			SortedMap<Integer, Machine> threads = machine.getThreadData();
			tm.setRowCount(threads.size());
			int row = 0;
			for (Entry<Integer, Machine> thread : threads.entrySet()) {
				tm.setValueAt(thread.getKey(), row, 0);
				tm.setValueAt(thread.getValue().getLC(), row, 1);
				++row;
			}
		}

		/** Add new thread to our list */
		@Override public void destroyThread(int threadID) {
			// TODO Auto-generated method stub

		}
	}

	/** Updates this UI as machine state changes. */
	UIUpdater myUIUpdater = new UIUpdater();
}
