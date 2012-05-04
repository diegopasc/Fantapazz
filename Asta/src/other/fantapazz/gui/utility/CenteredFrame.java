package other.fantapazz.gui.utility;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

public class CenteredFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	public void center() {
		if ( getDesktopPane() == null )
			return;
		Dimension screenSize = getDesktopPane().getSize();
	    int screenHeight = (int) (screenSize.height - getSize().getHeight());
	    int screenWidth = (int) (screenSize.width - getSize().getWidth());
	    setLocation(screenWidth / 2, screenHeight / 2);
	}

}
