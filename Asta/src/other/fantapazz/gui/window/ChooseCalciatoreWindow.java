package other.fantapazz.gui.window;

import it.fantapazz.connector.bean.CalciatoreComm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import other.fantapazz.gui.interfaces.ChooseCalciatoreI;
import other.fantapazz.gui.model.CalciatoriTableModel;
import other.fantapazz.gui.utility.CenteredFrame;
import other.fantapazz.gui.utility.TimerComponent;

public class ChooseCalciatoreWindow extends CenteredFrame {

	private static final long serialVersionUID = 1L;

	private TimerComponent timer;
	
	private JTable table;
	
	private List<CalciatoreComm> calciatori;
	
	private boolean started;
	
	private ChooseCalciatoreI delegate;
		
	public void setCalciatori(List<CalciatoreComm> list) {
		calciatori.clear();
		calciatori.addAll(list);
	}

	public ChooseCalciatoreWindow() {
		
		setTitle("Scegli un calciatore");
		setSize(500, 300);
		
		started = false;
		setVisible(false);
		
		getContentPane().setLayout(new BorderLayout());

		// Header
		JLabel timeLabel = new JLabel();
		timer = new TimerComponent(timeLabel);
		Box upper = Box.createHorizontalBox();
		upper.add(timer);
		upper.add(timeLabel);
		getContentPane().add(upper, BorderLayout.PAGE_START);
		
		// Main table view
		calciatori = new ArrayList<CalciatoreComm>(); //  Fantapazz.instance().getCalciatori() );
		table = new JTable(new CalciatoriTableModel(calciatori));
		table.setFillsViewportHeight(true);		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setMinimumSize(new Dimension(100, 100));
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel bottom = new JPanel(new BorderLayout());

		JButton button = new JButton("Ho scelto questo giocatore");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ( ! started )
					return;
				int row = table.getSelectedRow();
				if ( row < 0 )
					return;
				stop();
				CalciatoreComm calciatore = calciatori.get(row);
				calciatori.remove(row);
				delegate.selectedCalciatore(calciatore);
			}
		});
		bottom.add(button, BorderLayout.CENTER);
		
		JButton pause = new JButton("Pausa (30 secondi)");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delegate.paused();
			}
		});
		bottom.add(pause, BorderLayout.PAGE_END);

		getContentPane().add(bottom, BorderLayout.PAGE_END);

	}
	
	public void start(long delay, ChooseCalciatoreI delegate) {
		if ( ! started ) {
			started = true;
			timer.start(delay);
			this.delegate = delegate;
			setVisible(true);
		}
	}
	
	public void stop() {
		if ( started ) {
			timer.stop();
			started = false;
			setVisible(false);
		}
	}
	
}
