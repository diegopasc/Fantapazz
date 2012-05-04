package other.fantapazz.gui.model;

import it.fantapazz.asta.core.bean.CalciatoreCost;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CalciatoriWonTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	public static final int FIELD_NAME = 0;
	public static final int FIELD_CLUB = 1;
	public static final int FIELD_ROLE = 2;
	public static final int FIELD_COST = 3;
	
	private List<CalciatoreCost> list;
	
	public CalciatoriWonTableModel() {
		super();
		list = new LinkedList<CalciatoreCost>();
	}
	
	public void setCalciatori(List<CalciatoreCost> list) {
		this.list = list;
		fireTableDataChanged();
	}
	
	public void addCalciatore(CalciatoreCost calciatoreCost) {
		list.add(0, calciatoreCost);
		fireTableRowsInserted(0, 0);
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		if ( list == null )
			return 0;
		return list.size();
	}

	public Object getValueAt(int row, int col) {
		switch (col) {
		
		case FIELD_NAME :
			try {
				return Connector.instance().getCalciatore(list.get(row).getID()).getCalciatore();
			} catch (ConnectionException e) {
				return list.get(row).getID();
			}
		
		case FIELD_CLUB :
			try {
				return Connector.instance().getCalciatore(list.get(row).getID()).getClub();
			} catch (ConnectionException e) {
				return "?";
			}
			
		case FIELD_ROLE :
			try {
				return Connector.instance().getCalciatore(list.get(row).getID()).getRuolo_short();
			} catch (ConnectionException e) {
				return "?";
			}
			
		case FIELD_COST :
			return list.get(row).getCost();
				
		}
		return null;
	}

}
