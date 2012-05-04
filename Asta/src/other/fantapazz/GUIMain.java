package other.fantapazz;

import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.cache.Cache;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import other.fantapazz.gui.window.FakeLoginWindow;
import other.fantapazz.gui.window.LogWindow;
import other.fantapazz.gui.window.LoginWindow;
import other.fantapazz.gui.window.SettingsWindows;

public class GUIMain extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	public static JDesktopPane desktop;
	
	private SettingsWindows settings;
	
	public static void main(String[] args) {
		
		Logger.getRootLogger().addAppender(LogWindow.instance());
		
		Cache.instance().setEnabled(false);
		
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });		
	}

	private static void createAndShowGUI() {
		//Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        GUIMain frame = new GUIMain();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
	}

	public GUIMain() {
        super("InternalFrameDemo");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset * 2,
                  screenSize.width  - 2 * inset,
                  screenSize.height - 4 * inset);

        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        // createFrame(); //create first "window"
        setContentPane(desktop);
        setJMenuBar(createMenuBar());

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

    }
	
	protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the lone menu.
        JMenu menu = new JMenu("Connection");
        menu.setMnemonic(KeyEvent.VK_N);
        menuBar.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Configurazione");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("proxy");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Set up the second menu item.
        menuItem = new JMenuItem("Avvia connessione");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("connect");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Avvia connessione Test");
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("connect-test");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Set up the lone menu.
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);

        //Set up the first menu item.
        menuItem = new JMenuItem("Finestra di log");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("log");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

	public void actionPerformed(ActionEvent event) {
		/*
		if ( event.getActionCommand().equals("connect")) {
			LoginWindow login = new LoginWindow(ConfigSettings.hostConnection);
			login.setVisible(true);
			login.moveToFront();
			desktop.add(login);
			login.center();
			if ( ConfigSettings.autoLogin ) {
				login.tryLogin(ConfigSettings.loginUsername, ConfigSettings.loginPassword);
			}
			return;
		}
		
		if ( event.getActionCommand().equals("connect-test")) {
			FakeLoginWindow login;
			try {
				login = new FakeLoginWindow(ConfigSettings.hostConnection);
				desktop.add(login);
				login.setVisible(true);
				login.moveToFront();
				login.center();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if ( event.getActionCommand().equals("proxy")) {
			if ( settings == null ) {
				settings = new SettingsWindows();
			}
			if (settings.getDesktopPane() == null) {
				desktop.add(settings);
			}
			settings.setVisible(true);
		}
		
		if ( event.getActionCommand().equals("log")) {
			LogWindow.instance().setDesktopPane(desktop);
			LogWindow.instance().setVisible(true);
		} 
		*/
	}	
}
