package guinator;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Tab containing a simulator pane.
 * 
 * @author Josh Ventura
 * @date May 11, 2012; 12:06:23 AM
 */
public class SimulatorTab extends JPanel {
	/** Make ECJ shut up. */
	private static final long serialVersionUID = 1L;
	
	/** Default constructor. Lays out the UI. */
	public SimulatorTab() {
		GridLayout statusLayout = new GridLayout(9, 3);
		this.setLayout(statusLayout);
		add(new JTextField("Register 0"));
		add(new JTextField("Register 1"));
		add(new JTextField("Register 2"));
		add(new JTextField("Register 3"));
	}
}
