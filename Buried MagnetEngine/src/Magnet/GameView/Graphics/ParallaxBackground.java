package Magnet.GameView.Graphics;

import Magnet.Magnet;
import Magnet.GameLogic.Math.Vector2f;

public class ParallaxBackground {
	
	private Quad bg;
	private float distance;
	private boolean repeat;
	
	private Vector2f size;
	
	public ParallaxBackground(Texture texture, float scale, float distance, boolean repeat){
		this.distance = distance;
		this.repeat = repeat;
		
		size = new Vector2f(texture.getWidth() * scale, texture.getHeight() * scale);
		bg = new Quad(0, 0, scale, texture);
	}
	
	public void render(float cameraPosX, float cameraPosY){
		if(!repeat){
			Vector2f pos = new Vector2f(-cameraPosX / distance, -cameraPosY / distance);
			bg.setPosition(pos.x, pos.y);
			bg.render();
		}else{
			int offsetX = (int) ((cameraPosX / distance) / size.x);
			int offsetY = (int) ((cameraPosY / distance) / size.y);
			
			int tilesOnScreenX = (int) (Magnet.getDisplayWidth() / size.x+2);
			int tilesOnScreenY = (int) (Magnet.getDisplayHeight() / size.y+2);
			
			for(int x = offsetX; x < offsetX + tilesOnScreenX; x++){
				for(int y = offsetY; y < offsetY + tilesOnScreenY; y++){
					Vector2f pos = new Vector2f(x * size.x - cameraPosX / distance, y * size.y - cameraPosY / distance);
					bg.setPosition(pos.x, pos.y);
					bg.render();
				}
			}
		}
	}
	
	public final void setDistance(float newDistance){
		this.distance = newDistance;
	}
	
	public Vector2f getSize(){
		return size;
	}

}
