package guinator;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Utility functions for UI work.
 * 
 * @author Josh Ventura
 * @date May 18, 2012; 10:53:49 PM
 */
public class GUIUtil {
	/**
	 * Utility function to prompt for save filename.
	 * 
	 * @param window
	 *            The window to which the dialog belongs.
	 * @param filtername
	 *            The name of the filter matching the given extensions.
	 * @param extensions
	 *            The allowed extensions.
	 * @return The save filename.
	 */
	public static String getSaveFname(Component window, String filtername,
			final String... extensions) {
		return backendFunc(false, false, window, filtername, extensions)[0];
	}

	/**
	 * Utility function to prompt for load filename.
	 * 
	 * @param window
	 *            The window to which the dialog belongs.
	 * @param filtername
	 *            The name of the filter matching the given extensions.
	 * @param extensions
	 *            The allowed extensions.
	 * @return The save filename.
	 */
	public static String getLoadFname(Component window, String filtername,
			final String... extensions) {
		return backendFunc(false, true, window, filtername, extensions)[0];
	}

	/**
	 * Utility function to prompt for load filenames.
	 * 
	 * @param window
	 *            The window to which the dialog belongs.
	 * @param filtername
	 *            The name of the filter matching the given extensions.
	 * @param extensions
	 *            The allowed extensions.
	 * @return The save filename.
	 */
	public static String[] getLoadFnames(Component window, String filtername,
			final String... extensions) {
		return backendFunc(true, true, window, filtername, extensions);
	}

	/**
	 * @param multi
	 *            True to allow multiple files.
	 * @param open
	 *            True if this is an open dialog, false if it's a save dialog.
	 * @param window
	 *            The parent window.
	 * @param filtName
	 *            The name of the filter for the given extensions.
	 * @param extensions
	 *            Filter extensions.
	 * @return The selected filename(s).
	 */
	private static String[] backendFunc(boolean multi, boolean open,
			Component window, final String filtName, final String... extensions) {
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(multi);
		jfc.setFileFilter(new FileFilter() {
			@Override public String getDescription() {
				return filtName;
			}

			@Override public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				String ap = f.getAbsolutePath().toLowerCase();
				for (String i : extensions)
					if (ap.endsWith(i))
						return true;
				return false;
			}
		});
		if ((open ? jfc.showOpenDialog(window) : jfc.showSaveDialog(window)) != JFileChooser.APPROVE_OPTION)
			return multi ? new String[0] : new String[] { null };
		if (multi) {
			File[] fs = jfc.getSelectedFiles();
			String res[] = new String[fs.length];
			fnames:
			for (int i = 0; i < fs.length; ++i) {
				res[i] = fs[i].getAbsolutePath();
				if (!open) {
					String ap = res[i].toLowerCase();
					for (String ext : extensions)
						if (ap.endsWith(ext))
							continue fnames;
					res[i] += extensions[extensions.length - 1];
				}
			}
			return res;
		}
		String res = jfc.getSelectedFile().getAbsolutePath();
		if (!open) {
			String ap = res.toLowerCase();
			for (String ext : extensions)
				if (ap.endsWith(ext))
					return new String[] { res };
			res += extensions[extensions.length - 1];
		}
		return new String[] { res };
	}

	/**
	 * @author Josh Ventura
	 * @date May 23, 2012; 12:09:48 AM
	 * @param e
	 *            The exception to convert.
	 * @return A string representation of the exception, similar to what
	 *         e.printStackTrace() would give.
	 */
	public static String getExceptionString(Exception e) {
		String res = e.getMessage() + "\n\n" + e.getClass().toString() + ":\n";
		int sec = 0;
		for (StackTraceElement el : e.getStackTrace())
			if (sec++ < 12)
				res += "  at " + el.toString() + "\n";
		return res;
	}

	/**
	 * Display an exception to the user.
	 * 
	 * @author Josh Ventura
	 * @date May 7, 2012; 2:30:11 AM
	 * @param basicErr
	 *            The error string to display at the top.
	 * @param e
	 *            The exception to display.
	 * @param mainWindow
	 *            The window that owns this message.
	 */
	public static void showException(String basicErr, Exception e,
			Component mainWindow) {
		String errmsg = basicErr + ":\n" + getExceptionString(e);
		JOptionPane.showMessageDialog(mainWindow, errmsg, "Exception Error",
				JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}

	/**
	 * Display an error to the user.
	 * 
	 * @author Josh Ventura
	 * @date May 24, 2012; 8:58:39 PM
	 * @param errmsg
	 *            The error string to display.
	 * @param mainWindow
	 *            The window that owns this message.
	 */
	public static void showError(String errmsg, Component mainWindow) {
		JOptionPane.showMessageDialog(mainWindow, errmsg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @date May 22, 2012; 11:58:59 PM
	 * @param table
	 *            The table to pack.
	 */
	public static void packTable(JTable table) {
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
				.getColumnModel();
		for (int coln = 0; coln < 16; ++coln) {
			TableColumn col = colModel.getColumn(coln);
			int width = 0;

			// Get width of column header
			TableCellRenderer renderer = col.getHeaderRenderer();
			if (renderer == null) {
				renderer = table.getTableHeader().getDefaultRenderer();
			}
			Component comp = renderer.getTableCellRendererComponent(table,
					col.getHeaderValue(), false, false, 0, 0);
			width = comp.getPreferredSize().width;

			// Get maximum width of column data
			for (int r = 0; r < table.getRowCount(); r++) {
				renderer = table.getCellRenderer(r, coln);
				comp = renderer.getTableCellRendererComponent(table,
						table.getValueAt(r, coln), false, false, r, coln);
				width = Math.max(width, comp.getPreferredSize().width);
			}

			// Add margin
			width += 4;

			// Set the width
			col.setPreferredWidth(width);
		}
	}
}
