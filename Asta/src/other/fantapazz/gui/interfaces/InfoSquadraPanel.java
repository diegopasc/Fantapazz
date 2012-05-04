package other.fantapazz.gui.interfaces;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

public class InfoSquadraPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public InfoSquadraPanel() {
		setLayout(null);
		setMinimumSize(new Dimension(300, 100));
		setSize(300, 100);
		JButton button = new JButton("Ciao");
		add(button);
		Insets insets = getInsets();
		Dimension size = button.getPreferredSize();
		button.setBounds(25 + insets.left, 5 + insets.top, size.width, size.height);		
	}

}
