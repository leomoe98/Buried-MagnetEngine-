package Magnet.GameView.Graphics;

import java.awt.image.BufferedImage;

import Magnet.Magnet;
import Magnet.ApplicationLayer.Input;

public abstract class Button{

	private BufferedImage[] images;
	private float x, y;
	private int width, height;
	private float scale;
	
	private int index = 0;
	
	public abstract void clicked();
	
	public Button(float x, float y, float scale, BufferedImage image) {
		this.x = x;
		this.y = y;
		this.scale = scale;
		this.width = image.getWidth() / 2;
		this.height = image.getHeight();
		images = new BufferedImage[]{image.getSubimage(0, 0, width, height), image.getSubimage(width, 0, width, height)};
	}
	
	public void render(){
		Magnet.getGraphics().drawImage(images[index], (int)x, (int)y, (int)(width * scale), (int)(height * scale), null);
	}
	
	public void update(){
		if(hover()){
			index = 1;
			if(Input.isLeftMouseButtonDown())clicked();
		}else{
			index = 0;
		}
	}
	
	private boolean hover(){
		float mouseX = Input.getMouseX();
		float mouseY = Input.getMouseY();
		if(mouseX > x && mouseY > y){
			if(mouseX < x + width * scale && mouseY < y + height * scale){
				return true;
			}
		}
		return false;
	}

}
