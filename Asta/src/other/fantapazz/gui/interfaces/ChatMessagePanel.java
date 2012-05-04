package other.fantapazz.gui.interfaces;

import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.SquadraComm;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatMessagePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JTextArea textArea;
	
	private JTextField textField;
	
	public ChatMessagePanel(final ChatListener listener) {
		super(new BorderLayout());
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setAutoscrolls(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setAutoscrolls(true);
		add(scrollPane, BorderLayout.CENTER);
		
		Box box = Box.createHorizontalBox();
		box.add(new JLabel("Invia:"));
		
		textField = new JTextField();
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// System.out.println("Code: " + e.getKeyCode() + ", char: " + e.getKeyChar() );
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					String text = textField.getText();
					if ( text.trim().equals("")) {
						return;
					}
					// System.out.println("Send: " + text);
					listener.sendTextMessage(text);
					textField.setText("");
				}
			}
		});
		box.add(textField);
		add(box, BorderLayout.SOUTH);
	}
	
	public void receiveMessage(String from, String text) {
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String date = format.format(new Date());

		try {
			SquadraComm squadra = Connector.instance().getSquadra(from);
			textArea.append("[" + date + "]" + squadra.getAlias() + ": " + text + "\n");
		} catch (ConnectionException e1) {
			textArea.append("[" + date + "]" + from + ": " + text + "\n");
		}
		
	}

}
