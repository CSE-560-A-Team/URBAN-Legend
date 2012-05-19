package guinator;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import assemblernator.Module;

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
	
	/** Construct with a new default table model */
	public FullTable() {
		super(new DefaultTableModel(new String[0][],
				new Module.SymbolTable().toStringTable()[0]));
	}
};