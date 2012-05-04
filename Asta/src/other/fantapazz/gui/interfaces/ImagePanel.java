package other.fantapazz.gui.interfaces;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

    public ImagePanel(InputStream in) {
       try {                
          image = ImageIO.read(in);
       } catch (IOException ex) {
       }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
    
    @Override
    public Dimension getMinimumSize() {
    	return new Dimension(image.getWidth(), image.getHeight());
    }

}