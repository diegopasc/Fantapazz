package it.fantapazz.utility;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtility {

	public static ImageIcon getScaledIconWithHeight(ImageIcon icon, int height) {
		Image image = icon.getImage();
		int imageHeight = image.getHeight(null);
		int imageWidth = image.getWidth(null);
		int width = ( height * imageWidth ) / imageHeight;
		image = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING); 
		return new ImageIcon(image);
	}

}
