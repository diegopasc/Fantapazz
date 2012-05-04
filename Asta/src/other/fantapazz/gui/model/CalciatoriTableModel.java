package other.fantapazz.gui.model;

import it.fantapazz.connector.bean.CalciatoreComm;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CalciatoriTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	public static final int FIELD_NAME = 0;
	public static final int FIELD_CLUB = 1;
	public static final int FIELD_ROLE = 2;
	
	private List<CalciatoreComm> calciatori;
	
	public CalciatoriTableModel(List<CalciatoreComm> calciatori) {
		this.calciatori = calciatori;
	}
	
	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return calciatori.size();
	}

	public Object getValueAt(int row, int col) {
		switch (col) {
		
		case FIELD_NAME :
			return calciatori.get(row).getCalciatore();
		
		case FIELD_CLUB :
			return calciatori.get(row).getClub();
			
		case FIELD_ROLE :
			return calciatori.get(row).getRuolo_short();
							
		}
		return null;
	}

}
