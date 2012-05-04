package other.fantapazz.gui.utility;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

public class GUIUtility {

	public static void moveToFront(final JInternalFrame fr) {
        if (fr != null) {
        	javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    fr.moveToFront();
                    fr.setVisible(true);
                    try {
                        fr.setSelected(true);
                        if (fr.isIcon()) {
                            fr.setIcon(false);
                        }
                        fr.setSelected(true);
                    } catch (PropertyVetoException ex) {
                        ex.printStackTrace();
                    }
                    fr.requestFocus();
                    fr.toFront();
                }
            });		        	
        }
    }
	
}
