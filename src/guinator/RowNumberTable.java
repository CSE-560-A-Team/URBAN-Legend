package guinator;

import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import ulutil.IOFormat;


/** JTable extension to give a column and row header. */
public class RowNumberTable extends JTable implements ChangeListener,
		PropertyChangeListener {
	/** Shut up, ECJ... */
	private static final long serialVersionUID = 1L;
	/** The content table we are adapting. */
	private JTable main;

	/**
	 * @param table
	 *            JTable to wrap.
	 */
	public RowNumberTable(JTable table) {
		main = table;
		main.addPropertyChangeListener(this);

		setFocusable(false);
		setAutoCreateColumnsFromModel(false);
		setModel(main.getModel());
		//setSelectionModel(main.getSelectionModel());
		main.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				RowNumberTable.this.repaint();
			}
		});
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				main.clearSelection();
				main.setColumnSelectionInterval(0, 15);
				int rows[] = RowNumberTable.this.getSelectedRows();
				for (int row: rows)
					main.addRowSelectionInterval(row, row);
				RowNumberTable.this.repaint();
			}
		});

		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		addColumn(column);
		column.setCellRenderer(new RowNumberRenderer());

		getColumnModel().getColumn(0).setPreferredWidth(50);
		setPreferredScrollableViewportSize(getPreferredSize());
	}

	/** @see javax.swing.JTable#addNotify() */
	@Override public void addNotify() {
		super.addNotify();

		Component c = getParent();

		// Keep scrolling of the row table in sync with the main table.

		if (c instanceof JViewport) {
			JViewport viewport = (JViewport) c;
			viewport.addChangeListener(this);
		}
	}

	/** Delegate method to main table */
	@Override public int getRowCount() {
		return main.getRowCount();
	}

	/** @see javax.swing.JTable#getRowHeight(int) */
	@Override public int getRowHeight(int row) {
		return main.getRowHeight(row);
	}

	/**
	 * This table does not use any data from the main TableModel,
	 * so just return a value based on the row parameter.
	 * 
	 * @see javax.swing.JTable#getValueAt(int, int)
	 */
	@Override public Object getValueAt(int row, int column) {
		return IOFormat.formatHexInteger(row, 3);
	}

	/** Don't edit data in the main TableModel by mistake */
	@Override public boolean isCellEditable(int row, int column) {
		return false;
	}

	/** @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent) */
	@Override public void stateChanged(ChangeEvent e) {
		// Keep the scrolling of the row table in sync with main table

		JViewport viewport = (JViewport) e.getSource();
		JScrollPane scrollPane = (JScrollPane) viewport.getParent();
		scrollPane.getVerticalScrollBar()
				.setValue(viewport.getViewPosition().y);
	}

	/** @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent) */
	@Override public void propertyChange(PropertyChangeEvent e) {
		// Keep the row table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName())) {
			setSelectionModel(main.getSelectionModel());
		}

		if ("model".equals(e.getPropertyName())) {
			setModel(main.getModel());
		}
	}

	/** Borrow the renderer from JDK1.4.2 table header */
	private class RowNumberRenderer extends DefaultTableCellRenderer {
		/** More of the same! */
		private static final long serialVersionUID = 1L;

		/** Default constructor... */
		public RowNumberRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
		}

		/**
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
		 *      java.lang.Object, boolean, boolean, int, int)
		 */
		@Override public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (table != null) {
				JTableHeader header = table.getTableHeader();

				if (header != null) {
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}

			int srows[] = main.getSelectedRows();
			for (int srow: srows) if (row == srow) {
				setFont(getFont().deriveFont(Font.BOLD));
				break;
			}

			setText((value == null) ? "" : value.toString());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));

			return this;
		}
	}
}