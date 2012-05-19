package guinator;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

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
	 * @param extensions
	 *            The allowed extensions.
	 * @return The save filename.
	 */
	public static String getSaveFname(Component window,
			final String... extensions) {
		return backendFunc(false, false, window, extensions)[0];
	}

	/**
	 * Utility function to prompt for load filename.
	 * 
	 * @param window
	 *            The window to which the dialog belongs.
	 * @param extensions
	 *            The allowed extensions.
	 * @return The save filename.
	 */
	public static String getLoadFname(Component window,
			final String... extensions) {
		return backendFunc(false, true, window, extensions)[0];
	}

	/**
	 * Utility function to prompt for load filenames.
	 * 
	 * @param window
	 *            The window to which the dialog belongs.
	 * @param extensions
	 *            The allowed extensions.
	 * @return The save filename.
	 */
	public static String[] getLoadFnames(Component window,
			final String... extensions) {
		return backendFunc(true, true, window, extensions);
	}

	/**
	 * @param multi
	 *            True to allow multiple files.
	 * @param open
	 *            True if this is an open dialog, false if it's a save dialog.
	 * @param window
	 *            The parent window.
	 * @param extensions
	 *            Filter extensions.
	 * @return The selected filename(s).
	 */
	private static String[] backendFunc(boolean multi, boolean open,
			Component window, final String... extensions) {
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(multi);
		jfc.setFileFilter(new FileFilter() {
			@Override public String getDescription() {
				return "URBAN Assembly files (*.s, *.uls)";
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
			for (int i = 0; i < fs.length; ++i) {
				res[i] = fs[i].getAbsolutePath();
				if (!open) {
					String ap = res[i].toLowerCase();
					if (!ap.endsWith(".s") && !ap.endsWith(".uls"))
						res[i] += ".uls";
				}
			}
			return res;
		}
		String res = jfc.getSelectedFile().getAbsolutePath();
		if (!open) {
			String ap = res.toLowerCase();
			if (!ap.endsWith(".s") && !ap.endsWith(".uls"))
				res += ".uls";
		}
		return new String[] { res };
	}
}
