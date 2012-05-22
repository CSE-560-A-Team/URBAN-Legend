package guinator;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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

	/** Output Stream Display */
	private JTextArea outputBox;

	/** Default constructor. Lays out the UI. */
	public SimulatorTab() {

		String columns[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		String rows[][] = new String[4096 / 16][16];
		for (int row = 0; row < rows.length; row++)
			for (int col = 0; col < 16; ++col)
				rows[row][col] = "00";

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		memTable = new JTable(new DefaultTableModel(rows, columns));
		memTable.setMinimumSize(new Dimension(540, 320));
		JScrollPane scrollPane = new JScrollPane(memTable);
		RowNumberTable rnt = new RowNumberTable(memTable);

		scrollPane.setRowHeaderView(rnt);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
				rnt.getTableHeader());

		top.add(scrollPane);

		JPanel registerGrid = new JPanel();
		GridLayout registerLayout = new GridLayout(8, 2);
		registerGrid.setLayout(registerLayout);
		for (int i = 0; i < 8; ++i)
			registerGrid.add(new JTextField("Register " + i));
		JTextField dead = new JTextField("");
		registerGrid.add(dead);
		dead.setEnabled(false);
		for (int i = 1; i < 8; ++i)
			registerGrid.add(new JTextField("Index Register " + i));

		setOrientation(VERTICAL_SPLIT);
		setLeftComponent(top);
		setRightComponent(outputBox = new JTextArea());
	}
}
