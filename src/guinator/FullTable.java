package guinator;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author Josh Ventura
 * @date May 18, 2012; 8:09:20 PM
 */
public class FullTable extends JTable {
	/** Shut up, ECJ. */
	private static final long serialVersionUID = 1L;

	/** Be greedy with sizes. */
	@Override public boolean getScrollableTracksViewportWidth() {
		return getPreferredSize().width < getParent().getWidth();
	}

	/**
	 * Construct with a new default table model
	 * 
	 * @param data
	 *            The data to display.
	 * @param colnames
	 *            The names of the columns.
	 */
	public FullTable(String[][] data, String[] colnames) {
		super(new DefaultTableModel(data, colnames));
	}

	/**
	 * @param tableModel The table model to use.
	 */
	public FullTable(DefaultTableModel tableModel) {
		super(tableModel);
	}
};