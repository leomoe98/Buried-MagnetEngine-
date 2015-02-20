package Magnet.GameView.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import Magnet.ApplicationLayer.Utils.ResourceUtils;

public class Texture{
	
	private transient BufferedImage texture;
	
	public Texture(String path, boolean translucent){
		texture = ResourceUtils.loadBufferedImage(path, translucent);
	}
	
	public Texture(BufferedImage image, boolean translucent){
		texture = ResourceUtils.boostBufferedImagePerformance(image, translucent);
	}
	
	public static final Texture[] loadTileset(String path, int tileSize, boolean translucent){
		BufferedImage image = null;
		try {
			image = ImageIO.read(Texture.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int tilesX = image.getWidth() / tileSize;
		int tilesY = image.getHeight() / tileSize;
		
		Texture[] tileset = new Texture[tilesX * tilesY];
		int i = 0;
		for(int y = 0; y < tilesY; y++){
			for(int x = 0; x < tilesX; x++){
				tileset[i] = new Texture(image.getSubimage(x*tileSize, y * tileSize, tileSize, tileSize), translucent);
				i++;
			}
		}
		return tileset;
	}
	
	public void setTexture(String path, boolean translucent){
		texture = ResourceUtils.loadBufferedImage(path, translucent);
	}
	
	public void setTexture(BufferedImage image, boolean translucent){
		texture = ResourceUtils.boostBufferedImagePerformance(image, translucent);
	}
	
	public int getWidth(){
		return texture.getWidth();
	}
	
	public int getHeight(){
		return texture.getHeight();
	}
	
	public BufferedImage getBufferedImage(){
		return texture;
	}

}
