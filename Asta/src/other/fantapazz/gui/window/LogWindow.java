package other.fantapazz.gui.window;


import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import other.fantapazz.gui.utility.CenteredFrame;

public class LogWindow extends AppenderSkeleton {

	private static final long serialVersionUID = 1L;

	private JTextArea textArea;
	
	private CenteredFrame window; 
	
	private static LogWindow instance;
	
	public static LogWindow instance() {
		if ( instance == null )
			instance = new LogWindow();
		return instance;
	}

	private LogWindow() {
		super();
		window = new CenteredFrame();
		window.setTitle("Log");
		window.setSize(300, 500);
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		window.add(scrollPane, BorderLayout.CENTER);
		window.setResizable(true);
		window.setIconifiable(true);
		window.setMaximizable(true);
		window.setClosable(true);
		layout = new PatternLayout("%-5p %c{1} - %m%n");
	}

	private void appendLog(String text) {
		textArea.append(text);
//		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//		String date = format.format(new Date());
//		textArea.append("[" + date + "] " + text + "\n");
	}
	
	public void setVisible(boolean flag) {
		window.setVisible(flag);
	}
	
	public void setDesktopPane(JDesktopPane desktop) {
		desktop.add(window);
	}

	@Override
	protected void append(LoggingEvent event) {
		String message = event.getLoggerName() + ": " + event.getRenderedMessage(); 
		if ( this.layout != null ) {
			 message = this.layout.format(event);
		}
		appendLog(message);
	}

	@Override
	public void close() {
		window.setVisible(false);
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}


}

