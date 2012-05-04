package other.fantapazz.gui.utility;


import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class WaitingPanel extends CenteredFrame {
	
	private static final long serialVersionUID = 1L;

	private JLabel label;
	
	private JProgressBar bar;
	
	private static WaitingPanel instance;
	
	public static WaitingPanel instance() {
		if ( instance == null )
			instance = new WaitingPanel();
		return instance;
	}
	
	private WaitingPanel() {
		label = new JLabel("Ciao mamma");
		bar = new JProgressBar(0, 100);
		setSize(300, 200);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(label, BorderLayout.PAGE_START);
		getContentPane().add(bar, BorderLayout.PAGE_END);
	}
	
	public static void start(JDesktopPane desktop) {
		desktop.add(instance());
		instance()._start();
	}

	public static void stop() {
		instance()._stop();
		instance().getDesktopPane().remove(instance());
	}

	public static void setMessage(String message) {
		instance()._setMessage(message);
	}

	private void _start() {
		setVisible(true);
		bar.setIndeterminate(true);
		bar.setValue(50);
	}
	
	private void _stop() {
		setVisible(false);
		bar.setIndeterminate(false);
	}
	
	public void _setMessage(String message) {
		label.setText(message);
	}

}
