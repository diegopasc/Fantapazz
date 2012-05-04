package other.fantapazz.gui.window;

import it.fantapazz.asta.controller.AstaControllerServer;
import it.fantapazz.asta.controller.AsteListener;
import it.fantapazz.asta.controller.PlayerControlServer;
import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.bean.UserComm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import other.fantapazz.gui.aste.AsteStatusTableModel;
import other.fantapazz.gui.utility.CenteredFrame;

/**
 * Window with the list of all running aste and buttons to enter
 * in an Asta or create a new one.
 * 
 * @author Michele Mastrogiovanni
 */
public class AsteWindow extends CenteredFrame implements AsteListener {
	
	private static final long serialVersionUID = 1L;
	
	private UserComm user;
	
	private AsteStatusTableModel asteModel;
	
	private PlayerControlServer client;
	
	private AstaWindow astaWindow;
	
	private JButton enter;
	private JButton start;
	private JButton stop;
	
	private JTable table;

	public AsteWindow(UserComm user, String host) {
		super();
		
		this.user = user;
		
		setTitle("Fantapazz Asta Online - Entra!");
		setSize(850, 500);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		setIconifiable(true);
		
		getContentPane().setLayout(new BorderLayout());

		// Header
		Border raised = BorderFactory.createRaisedBevelBorder();
		Border lowered = BorderFactory.createLoweredBevelBorder();
		CompoundBorder border = BorderFactory.createCompoundBorder(raised, lowered);
		JLabel label = new JLabel("Benvenuto " + user.getName() + " nel portale delle aste online di Fantapazz!!!");
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(0, 400));
		panel.add(label);
		panel.setBorder(border);
		
		Box header = Box.createVerticalBox();
		header.add(panel);
				
		asteModel = new AsteStatusTableModel();
		
		// Main body
		table = new JTable(asteModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Disable auto resizing
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// Set the first visible column to 100 pixels wide
		table.getColumnModel().getColumn(AsteStatusTableModel.COLUMN_STATUS).setPreferredWidth(50);
		table.getColumnModel().getColumn(AsteStatusTableModel.COLUMN_NAME).setPreferredWidth(200);
		table.getColumnModel().getColumn(AsteStatusTableModel.COLUMN_PARTECIPANTS).setPreferredWidth(600);
				
		table.setRowHeight(50);

		// Enter in asta
		enter = new JButton("Entra nell'asta!");
		enter.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectionModel().getMinSelectionIndex();
				if ( index < 0 )
					return;
				AstaInfo info = asteModel.getAstaInfo(index);
				if ( info.getStatus() != AstaInfo.Status.RUNNING) {
					JOptionPane.showMessageDialog(AsteWindow.this, "Puoi entrare solo in un'asta avviata", "Attenzione", JOptionPane.WARNING_MESSAGE );
					return;
				}
				System.out.println("Entering asta: " + info.getID());
				if ( astaWindow == null ) {
					astaWindow = new AstaWindow(AsteWindow.this, client);
					getDesktopPane().add(astaWindow);
					astaWindow.center();
				}
				try {
					astaWindow.start(info);
					AsteWindow.this.setVisible(false);
				} catch (ConnectionException e1) {
					e1.printStackTrace();
				}
			}
			
		});
//		enter.setVisible(false);
		enter.setEnabled(false);

		start = new JButton("Avvia Asta");
		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectionModel().getMinSelectionIndex();
				if ( index < 0 )
					return;
				AstaInfo info = asteModel.getAstaInfo(index);
				System.out.println("Starting asta: " + info.getID());
				client.startAsta(info);
			}
			
		});
//		start.setVisible(false);
		start.setEnabled(false);

		stop = new JButton("Ferma Asta");
		stop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectionModel().getMinSelectionIndex();
				if ( index < 0 )
					return;
				AstaInfo info = asteModel.getAstaInfo(index);
				System.out.println("Stopping asta: " + info.getID());
				client.stopAsta(info);
			}
			
		});
//		stop.setVisible(false);
		stop.setEnabled(false);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				updateButtons();
			}
		});
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		JScrollPane scrollPane = new JScrollPane(table);
		
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		header.add(table.getTableHeader());

		getContentPane().add(header, BorderLayout.PAGE_START);

		Box vertical = Box.createHorizontalBox();
		
		vertical.add(enter);
		vertical.add(start);
		vertical.add(stop);

		getContentPane().add(vertical, BorderLayout.PAGE_END);

		// Start client connection
		client = new PlayerControlServer(user.getID(), host, AstaControllerServer.CONTROLLER_PORT, this);
		new Thread(client).start();

	}
	
	private void updateButtons() {
		
		int index = table.getSelectionModel().getMinSelectionIndex();

		enter.setEnabled(index >= 0);
		start.setEnabled(index >= 0);
		stop.setEnabled(index >= 0);
		
		if ( index < 0 )
			return;
		
		AstaInfo info = asteModel.getAstaInfo(index);
		stop.setEnabled(info.getStatus() == AstaInfo.Status.RUNNING);
		start.setEnabled(info.getStatus() == AstaInfo.Status.NOT_STARTED);
		enter.setEnabled(info.getStatus() == AstaInfo.Status.RUNNING);
		
	}

	public UserComm getUser() {
		return user;
	}

	public void updateAsta(AstaInfo astaInfo) {
		asteModel.updateAstaInfo(astaInfo);
		updateButtons();
	}

	public void removedAsta(AstaInfo astaInfo) {
		
	}

}
