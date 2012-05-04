package other.fantapazz.gui.window;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.SquadraComm;
import it.fantapazz.connector.bean.UserComm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;

import other.fantapazz.gui.utility.CenteredFrame;

public class FakeLoginWindow extends CenteredFrame {

	private static final long serialVersionUID = 1L;
	
	private Map<String, SquadraComm> squadre;
	
	public FakeLoginWindow(final String host) throws ConnectionException {
		
		setTitle("Aste - Login");
		setSize(300, 400);		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		squadre = new HashMap<String, SquadraComm>();
		for ( AstaInfo asta : Connector.instance().getAste()) {
			for ( String squadraID : asta.getPlayers()) {
				if ( squadre.get(squadraID) == null ) {
					squadre.put(squadraID, Connector.instance().getSquadra(squadraID));
				}
			}
		}
		final List<String> keys = new LinkedList<String>(squadre.keySet());
		final JTable table = new JTable(new AbstractTableModel() {
			private static final long serialVersionUID = 1L;
			public int getColumnCount() {
				return 1;
			}
			public int getRowCount() {
				return keys.size();
			}
			public Object getValueAt(int rows, int cols) {
				return squadre.get(keys.get(rows)).getAlias();
			}
		});
		getContentPane().add(table.getTableHeader(), BorderLayout.PAGE_START);
		getContentPane().add(table, BorderLayout.CENTER);
		JButton select = new JButton("Seleziona");
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if (index < 0 )
					return;
				SquadraComm squadra = squadre.get(keys.get(index));
				UserComm user;
				try {
					user = Connector.instance().getUser(squadra.getUidPresidente());
					AsteWindow window = new AsteWindow(user, host);
					window.setVisible(true);
					getDesktopPane().add(window);
					window.center();
					setVisible(false);
				} catch (ConnectionException e) {
					e.printStackTrace();
				}

			}
		});
		getContentPane().add(select, BorderLayout.PAGE_END);
		
		setClosable(true);
		setMaximizable(false);
		setResizable(false);
		
	}

}
