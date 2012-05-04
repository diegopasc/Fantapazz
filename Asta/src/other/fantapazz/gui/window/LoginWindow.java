package other.fantapazz.gui.window;

import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.UserComm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import other.fantapazz.gui.utility.CenteredFrame;
import other.fantapazz.gui.utility.SpringUtilities;
import other.fantapazz.gui.utility.WaitingPanel;

/**
 * Window user for login: username and password.
 * It will redirect to AsteWindow.
 * 
 * @author Michele Mastrogiovanni
 */
public class LoginWindow extends CenteredFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(LoginWindow.class);

	private JButton SUBMIT;
	private JPanel panel;
	private JLabel label1,label2;
	private final JTextField  text1,text2;
	
	private String host;

	public LoginWindow(String host) {
		this.host = host;
		
		setTitle("Aste - Login");
		setSize(300, 150);		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		label1 = new JLabel();
		label1.setText("Username:");
		text1 = new JTextField(15);

		label2 = new JLabel();
		label2.setText("Password:");
		text2 = new JPasswordField(15);

		SUBMIT=new JButton("Entra");

		SpringLayout layout = new SpringLayout();
		panel = new JPanel(layout);
		
		panel.add(label1);
		panel.add(text1);
		panel.add(label2);
		panel.add(text2);
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(panel,
		                                2, 2,  		// rows, cols
		                                6, 6,       // initX, initY
		                                6, 6);      // xPad, yPad
		
		add(panel, BorderLayout.CENTER);
		add(SUBMIT, BorderLayout.SOUTH);
		
		SUBMIT.addActionListener(this);

		setClosable(true);
		setMaximizable(false);
		setResizable(false);
		
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		setVisible(false);
		WaitingPanel.start(getDesktopPane());
		WaitingPanel.setMessage("Sto verificando le tue credenziali");
		
		new Thread() {
			
			public void run() {
				
				String username = text1.getText();
				String password = text2.getText();

				WaitingPanel.stop();
				
				tryLogin(username, password);
			}
			
		}.start();
	}
	
	public void tryLogin(String username, String password) {

		UserComm user = null;
		
		try {
			user = Connector.getInfo().getUser(username, password);
		} catch (ConnectionException e) {
			logger.error("Ciao", e);
			e.printStackTrace();
			JOptionPane.showMessageDialog(LoginWindow.this, "Problemi nella connessione al server", "Errore", JOptionPane.ERROR_MESSAGE );
			setVisible(true);
			return;
		}

		if ( user == null ) {
			JOptionPane.showMessageDialog(LoginWindow.this, "Username o password errati", "Errore", JOptionPane.ERROR_MESSAGE );
			setVisible(true);
		}
		else {
			AsteWindow window = new AsteWindow(user, host);
			window.setVisible(true);
			getDesktopPane().add(window);
			window.center();
			setVisible(false);
		}

	}
}
