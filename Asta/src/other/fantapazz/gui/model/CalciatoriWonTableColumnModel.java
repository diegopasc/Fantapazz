package other.fantapazz.gui.model;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class CalciatoriWonTableColumnModel extends DefaultTableColumnModel {
	
	private static final long serialVersionUID = 1L;

	public CalciatoriWonTableColumnModel() {
		addColumn(new TableColumn(CalciatoriWonTableModel.FIELD_NAME, 150));
		addColumn(new TableColumn(CalciatoriWonTableModel.FIELD_CLUB, 100));
		addColumn(new TableColumn(CalciatoriWonTableModel.FIELD_ROLE, 50));
		addColumn(new TableColumn(CalciatoriWonTableModel.FIELD_COST, 50));
	}

}
