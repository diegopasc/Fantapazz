package other.fantapazz.gui.aste;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class AsteStatusCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		if ( value instanceof Component ) {
			return (Component) value;
		}
		
		if ( col == AsteStatusTableModel.COLUMN_STATUS ) {
			return new JLabel((ImageIcon) value);
		}
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	}

}
