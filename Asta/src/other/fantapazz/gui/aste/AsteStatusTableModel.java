package other.fantapazz.gui.aste;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.LegaComm;
import it.fantapazz.connector.bean.SquadraComm;
import it.fantapazz.utility.ImageUtility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AsteStatusTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(AsteStatusTableModel.class);

	public static final int COLUMN_STATUS = 0;
	public static final int COLUMN_NAME = 1;
	public static final int COLUMN_PARTECIPANTS = 2;
	public static final int COLUMN_ONLINE = 3;
	public static final int COLUMN_COMPLETITION = 4;
	
	private List<AstaInfo> astaInfo;
	
	public AsteStatusTableModel() {
		super();
		astaInfo = new LinkedList<AstaInfo>();
		addColumn("Stato");
		addColumn("Nome Lega");
		addColumn("Partecipanti");
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	public AstaInfo getAstaInfo(int row) {
		return astaInfo.get(row);
	}
	
	public void updateAstaInfo(AstaInfo info) {
		// Download lega informations
		try {
			Connector.instance().getLega(info.getIDLega());
			for ( String playerID : info.getPlayers()) {
				Connector.instance().getSquadra(playerID);
			}
		} catch (ConnectionException e) {
			logger.error("Impossible to get lega informations: " + info.getIDLega(), e);
		}
		int index = -1;
		for ( index = 0; index < astaInfo.size(); index ++) {
			if ( astaInfo.get(index).getID().equals(info.getID())) {
				break;
			}
		}
		if ( index == astaInfo.size() ) {
			astaInfo.add(info);
			this.fireTableRowsInserted(astaInfo.size() - 1, astaInfo.size() - 1);
		}
		else {
			astaInfo.set(index, info);
			this.fireTableRowsUpdated(index, index);
		}
		
		
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case COLUMN_STATUS :
			return ImageIcon.class;
		default:
			return String.class;
		}
	}

	public int getRowCount() {
		if ( astaInfo == null )
			return 0;
		return astaInfo.size();
	}
		
	public Object getValueAt(int row, int col) {
		
		AstaInfo data = astaInfo.get(row);

		switch (col) {

		case COLUMN_STATUS :
			switch ( data.getStatus() ) {
			case NOT_STARTED :
				return getSizedIcon(32, "circle_not_started.png");
			case RUNNING :
				return getSizedIcon(32, "circle_green.png");
			case TERMINATED:
				return getSizedIcon(32, "circle_red.png");
			}

		case COLUMN_NAME : 
			// Download lega informations
			try {
				LegaComm lega = Connector.instance().getLega(data.getIDLega());
				return lega.getNomeLega();
			} catch (ConnectionException e) {
				logger.error("Impossible to get lega informations: " + data.getIDLega(), e);
			}
			return data.getIDLega();

		case COLUMN_PARTECIPANTS : 
			return getPartecipants(data);

		}

		return null;

	}
	
	private String getPartecipants(AstaInfo data) {
		String ret = "";
		for ( String playerID : data.getPlayers() ) {
			SquadraComm squadra;
			try {
				squadra = Connector.instance().getSquadra(playerID);
				ret += squadra.getAlias() + ", ";
			} catch (ConnectionException e) {
				ret += playerID + ", ";
			}
		}
		if ( ret.length() > 0 ) {
			ret = ret.substring(0, ret.length() - 2);
		}
		return ret;
	}

	private ImageIcon getSizedIcon(int size, String name) {
		InputStream input = getClass().getResourceAsStream(name);
		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(input);
		} catch (IOException e) {
			return null;
		}
		return ImageUtility.getScaledIconWithHeight(new ImageIcon( myPicture ), size);
	}

}
