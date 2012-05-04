package other.fantapazz.gui.window;

import it.fantapazz.ConfigSettings;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import other.fantapazz.gui.utility.CenteredFrame;

public class SettingsWindows extends CenteredFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField host;
	private JCheckBox useProxy;
	private JTextField proxyHost;
	private JTextField proxyPort;
	private JTextField proxyUser;
	private JTextField proxyPass;
	
	public SettingsWindows() {
		super();
		
		setResizable(false);
		setClosable(true);
		setMaximizable(false);
		
		setTitle("Impostazioni");
		getContentPane().setLayout(new GridLayout(6, 2));
		setSize(300, 200);
		
		host = new JTextField();
		useProxy = new JCheckBox();
		proxyHost = new JTextField();
		proxyPort = new JTextField();
		proxyUser = new JTextField();
		proxyPass = new JTextField();
		
		add(new JLabel("Host"));
		add(host);
		
		add(new JLabel("Usa Proxy"));
		add(useProxy);

		add(new JLabel("Proxy Host"));
		add(proxyHost);

		add(new JLabel("Proxy Port"));
		add(proxyPort);

		add(new JLabel("Proxy Username"));
		add(proxyUser);

		add(new JLabel("Proxy Password"));
		add(proxyPass);
		
		update();
		
		KeyAdapter adapter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				ConfigSettings.instance().setProxyEnabled( useProxy.isSelected() ); 
				ConfigSettings.instance().setProxyHost( proxyHost.getText() );
				ConfigSettings.instance().setProxyPort( proxyPort.getText() );
				ConfigSettings.instance().setProxyUsername( proxyUser.getText() );
				ConfigSettings.instance().setProxyPassword( proxyPass.getText() );
				ConfigSettings.instance().setHostConnection( host.getText() );
			}
		};
		
		proxyHost.addKeyListener(adapter);
		proxyPort.addKeyListener(adapter);
		proxyUser.addKeyListener(adapter);
		proxyPass.addKeyListener(adapter);
		host.addKeyListener(adapter);
		
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent arg0) {
				setVisible(false);
			}
		});		

	}
	
	private void update() {
		useProxy.setSelected(ConfigSettings.instance().isProxyEnabled());
		proxyHost.setText(ConfigSettings.instance().getProxyHost());
		proxyPort.setText(ConfigSettings.instance().getProxyPort());
		proxyUser.setText(ConfigSettings.instance().getProxyUsername());
		proxyPass.setText(ConfigSettings.instance().getProxyPassword());
		host.setText(ConfigSettings.instance().getHostConnection());
	}

}
