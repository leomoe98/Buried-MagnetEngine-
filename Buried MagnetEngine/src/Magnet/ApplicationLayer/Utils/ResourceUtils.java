package Magnet.ApplicationLayer.Utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ResourceUtils{
	
	private ResourceUtils(){
	}
	
	public static final BufferedImage loadBufferedImage(String path,  boolean translucent){
		try {
			return boostBufferedImagePerformance(ImageIO.read(ResourceUtils.class.getResourceAsStream(path)), translucent);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final BufferedImage boostBufferedImagePerformance(BufferedImage image, boolean translucent)
	{
		GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		if (image.getColorModel().equals(gfx_config.getColorModel()))return image;
		BufferedImage newImage;
		if(translucent){
			newImage = gfx_config.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		}else{
			newImage = gfx_config.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.BITMASK);
		}
		Graphics2D g = (Graphics2D) newImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage; 
	}

}
