package other.fantapazz.gui.interfaces;

import it.fantapazz.utility.ImageUtility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import other.fantapazz.gui.utility.SpringUtilities;
import other.fantapazz.gui.utility.TimerComponent;

public class PlayerPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private PlayerReceiver listener;
	
	private Date lastStatusMessage;
	
	private JLabel statusMessage;
	
	private JLabel giocatoreName;
	private JLabel giocatoreRuolo;
	private JLabel giocatoreClub;
	private JLabel offerta;
	private JLabel offerer;
	private TimerComponent timer;
	private JLabel timeLabel;
	
	private JButton plusOne;
	private JButton plusFive;
	private JButton plusTen;
	
	private JPanel giocatorePanel; 
	private JPanel rilanciPanel;

	public PlayerPanel(PlayerReceiver listener) {
		
		setLayout(new BorderLayout());

		this.listener = listener;
		
		JPanel upper = new JPanel(new SpringLayout());
		
		try {
			upper.add(getImage("player.jpg"));
		} catch (IOException e) {
		}
		upper.add(getGiocatorePanel());
		upper.add(getRilanciPanel());
		
		SpringUtilities.makeCompactGrid(upper,
                1, 3, 		// rows, cols
                6, 6,       // initX, initY
                6, 6);      // xPad, yPad
		
		Box lower = Box.createHorizontalBox();
		timeLabel = new JLabel();
		timer = new TimerComponent(timeLabel);
		lower.add(timer);
		lower.add(timeLabel);
		
		statusMessage = new JLabel();
		
		add(upper, BorderLayout.PAGE_START);
		add(statusMessage, BorderLayout.CENTER);
		add(lower, BorderLayout.PAGE_END);
		
		setGiocatore("-", "-", "-");
		setOfferta("-", "-");
		
	}
	
	public void setStatusMessage(String message) {
		if ( lastStatusMessage == null ) {
			lastStatusMessage = new Date();
			statusMessage.setText(message);
		}
		else {
			Date now = new Date();
			if ( now.getTime() - lastStatusMessage.getTime() < 2000) {
				if (!statusMessage.getText().trim().equals("")) {
					statusMessage.setText(statusMessage.getText() + "; " + message);
				}
				else {
					statusMessage.setText(message);
				}
			}
			else {
				statusMessage.setText(message);				
			}
			lastStatusMessage = now;
		}
	}
	
	public void enableOffer(boolean enabled) {
		plusOne.setEnabled(enabled);
		plusFive.setEnabled(enabled);
		plusTen.setEnabled(enabled);
	}
	
	public void startTimer(long delay) {
		if ( delay <= 0 )
			return;
		timer.start(delay);
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	public void setGiocatore(String name, String ruolo, String club) {
		giocatoreName.setText(name);
		giocatoreRuolo.setText(ruolo);
		giocatoreClub.setText(club);
	}
	
	public void setOfferta(String user, String offer) {
		offerer.setText(user);
		offerta.setText(offer);
	}
	
	private JPanel getGiocatorePanel() {
		if ( giocatorePanel == null ) {
			giocatorePanel = new JPanel(new SpringLayout());
			
			giocatorePanel.setPreferredSize(new Dimension(500, 200));
			giocatorePanel.setMinimumSize(new Dimension(250, 200));
			
			giocatoreName = new JLabel();
			giocatoreRuolo = new JLabel();
			giocatoreClub = new JLabel();
			offerer = new JLabel();
			offerta = new JLabel();

			giocatorePanel.add(new JLabel("Calciatore:"));
			giocatorePanel.add(giocatoreName);
			
			giocatorePanel.add(new JLabel("Ruolo:"));
			giocatorePanel.add(giocatoreRuolo);
			
			giocatorePanel.add(new JLabel("Club:"));
			giocatorePanel.add(giocatoreClub);

			giocatorePanel.add(new JLabel("Offerente:"));
			giocatorePanel.add(offerer);
			
			giocatorePanel.add(new JLabel("Offerta:"));
			giocatorePanel.add(offerta);
						
			SpringUtilities.makeCompactGrid(giocatorePanel,
                    5, 2, 		// rows, cols
                    6, 6,       // initX, initY
                    6, 6);      // xPad, yPad
			
			giocatorePanel.setBackground(Color.LIGHT_GRAY);
			giocatorePanel.setBorder(BorderFactory.createTitledBorder("Stato asta:"));
			
		}
		return giocatorePanel;
	}
		
	private Component getRilanciPanel() {
		if ( rilanciPanel == null ) {
						
			rilanciPanel = new JPanel(new SpringLayout());
			rilanciPanel.setPreferredSize(new Dimension(200, 200));
			rilanciPanel.setMinimumSize(new Dimension(200, 200));
						
			plusOne = new JButton("+ 1");
			plusFive = new JButton("+ 5");
			plusTen = new JButton("+ 10");
			
			rilanciPanel.add(plusOne);
			rilanciPanel.add(plusFive);
			rilanciPanel.add(plusTen);
						
			plusOne.addActionListener(this);
			plusFive.addActionListener(this);
			plusTen.addActionListener(this);

			SpringUtilities.makeCompactGrid(rilanciPanel,
                    3, 1, 		// rows, cols
                    6, 6,       // initX, initY
                    6, 6);      // xPad, yPad

			rilanciPanel.setBorder(BorderFactory.createTitledBorder("Rilancia"));
			
		}
		return rilanciPanel;
	}
	
	private JLabel getImage(String fileName) throws IOException {
		InputStream input = getClass().getResourceAsStream(fileName);
		BufferedImage myPicture = ImageIO.read(input);
		ImageIcon icon = new ImageIcon( myPicture );
		icon = ImageUtility.getScaledIconWithHeight(icon, 200);
		JLabel picLabel = new JLabel(icon);
		return picLabel;
	}

	public void actionPerformed(ActionEvent event) {
		if ( event.getSource() == plusOne ) {
			listener.pushedPlusOne();
			return;
		}
		if ( event.getSource() == plusFive ) {
			listener.pushedPlusFive();
			return;
		}
		if ( event.getSource() == plusTen ) {
			listener.pushedPlusTen();
			return;
		}
	}

}
