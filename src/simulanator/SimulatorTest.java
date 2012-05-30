package simulanator;

import java.awt.Component;
import java.io.ByteArrayInputStream;

import simulanator.Machine.URBANInputStream;
import simulanator.Machine.URBANOutputStream;
import ulutil.FrankenOutput;
import assemblernator.ErrorReporting.ErrorHandler;

/**
 * @author Josh Ventura
 * @date May 29, 2012; 11:33:43 AM
 */
public class SimulatorTest {
	/**
	 * Get HTML dump.
	 * 
	 * @author Josh Ventura
	 * @date May 30, 2012; 12:33:03 AM
	 * @param time
	 *            Eg, "Initial" or "Final".
	 * @param m
	 *            The machine to dump.
	 * @return The dump of the machine, in a scrollable div.
	 */
	private static String getDump(String time, Machine m) {
		return "<h2>"
				+ time
				+ " Machine Dump</h2><pre style=\"width:75%; height: 256px; overflow: auto; white-space:pre;\">"
				+ m.dump(3).replaceAll("\n\n", "<br>\n") + "</pre>";
	}

	/**
	 * @param loaderFile
	 *            The executable file to load.
	 * @param parentWindow
	 *            The component of the window which will own any input dialogs
	 *            spawned.
	 * @return The HTML text of the test case.
	 */
	public static String getTestCase(byte[] loaderFile, Component parentWindow) {
		FrankenOutput uos = new FrankenOutput(parentWindow);
		Machine m = new Machine(uos, uos, uos);

		Simulator.load(new ByteArrayInputStream(loaderFile), uos, uos, m);
		uos.writeHTML(getDump("Initial", m));
		m.runAnchored();
		uos.writeHTML(getDump("Final", m));

		return uos.appme;
	}

	/**
	 * @param m
	 *            The machine with the loaded executable file.
	 * @param parentWindow
	 *            The component of the window which will own any input dialogs
	 *            spawned.
	 * @return The HTML text of the test case.
	 */
	public static String getTestCase(Machine m, Component parentWindow) {
		final URBANOutputStream uos_orig = m.output;
		final URBANInputStream uis_orig = m.input;
		final ErrorHandler hErr_orig = m.hErr;
		final FrankenOutput fo = new FrankenOutput(parentWindow) {
			@Override public void reportError(String err, int pos1, int pos2) {
				super.reportError(err, pos1, pos2);
				hErr_orig.reportError(err, pos1, pos2);
			}

			@Override public void reportWarning(String warn, int pos1, int pos2) {
				super.reportWarning(warn, pos1, pos2);
				hErr_orig.reportWarning(warn, pos1, pos2);
			}

			@Override public void putString(String str) {
				super.putString(str);
				uos_orig.putString(str);
			}
		};
		m.output = fo;
		m.input = fo;
		m.hErr = fo;
		fo.writeHTML(getDump("Initial", m));
		m.runAnchored();
		fo.writeHTML(getDump("Final", m));
		m.output = uos_orig;
		m.input = uis_orig;
		m.hErr = hErr_orig;
		return fo.appme;
	}
}
