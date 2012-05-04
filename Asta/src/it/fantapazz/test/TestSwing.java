package it.fantapazz.test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class TestSwing {

	public static void main(String[] args) {
		
		SpringLayout layout = new SpringLayout();
		JFrame frame = new JFrame();
		JPanel panel = new JPanel(layout);
		
		JPanel square = new JPanel();
		square.setBackground(Color.BLACK);
//		square.setMaximumSize(new Dimension(300, 300));
//		square.setPreferredSize(new Dimension(100, 100));
		
		panel.add(square);
		
		layout.putConstraint(SpringLayout.WEST, square, 5, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, square, 5, SpringLayout.NORTH, panel);
        
        SpringLayout.Constraints cButton = layout.getConstraints(square);
        Spring w = Spring.max(Spring.constant(300), cButton.getWidth());
        Spring h = Spring.max(Spring.constant(300), cButton.getHeight());
        cButton.setWidth(w);
        cButton.setWidth(h);
        
		frame.setContentPane(panel);
		frame.setVisible(true);
		
	}
	
}
