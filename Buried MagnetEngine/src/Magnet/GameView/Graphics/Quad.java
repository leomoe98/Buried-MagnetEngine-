package Magnet.GameView.Graphics;

import java.awt.Graphics2D;

import Magnet.Magnet;
import Magnet.GameLogic.Math.Vector2f;

public class Quad {
	
	private Graphics2D g;
	private Texture texture[] = new Texture[1];
	private boolean animated;
	private float animSpeed;
	private int animTimer;
	private int currentIndex;
	private boolean playAnim;
	
	private float x, y;
	private float width, height;
	private boolean flippedHorizontally = false;
	private boolean flippedVertically = false;
	
	public Quad(float x, float y, float scale, Texture texture){
		this.x = x;
		this.y = y;
		this.width = texture.getWidth() * scale;
		this.height = texture.getHeight() * scale;
		this.texture[0] = texture;
		animated = false;
		g = Magnet.getGraphics();
	}
	
	public Quad(float x, float y, float scale, Texture[] texture, float animSpeed){
		this.x = x;
		this.y = y;
		this.width = texture[0].getWidth() * scale;
		this.height = texture[0].getHeight() * scale;
		this.texture = texture;
		animated = true;
		this.animSpeed = animSpeed;
		animTimer = 0;
		currentIndex = 0;
		playAnim = true;
		g = Magnet.getGraphics();
	}
	
	public final void render(){
		int Twidth, Theight;
		int Tx, Ty;
		
		if(flippedHorizontally){
			Twidth = (int) -width;
			Tx = (int) (x+width);
		}else{
			Twidth = (int) width;
			Tx = (int)x;
		}
		
		if(flippedVertically){
			Theight = (int) -height;
			Ty = (int) (y+height);
		}else{
			Theight = (int) height;
			Ty = (int)y;
		}
		if(animated){
			g.drawImage(texture[currentIndex].getBufferedImage(), Tx, Ty, Twidth, Theight, null);
		}else{
			g.drawImage(texture[0].getBufferedImage(), Tx, Ty, Twidth, Theight, null);
		}
	}
	
	public final void update(){
		if(animated){
			if(animTimer / 100.0 >= 1.0 - animSpeed){
				animTimer = 0;
				currentIndex++;
				if(currentIndex >= texture.length)currentIndex = 0;
			}
		}
		if(playAnim)animTimer++;
	}
	
	public final void setSize(float width, float height){
		this.width = width;
		this.height = height;
	}
	
	public final void setPosition(float x, float y){
		Vector2f pos = new Vector2f(x, y);
		this.x = pos.x;
		this.y = pos.y;
	}
	
	public final Vector2f getSize(){
		return new Vector2f(width, height);
	}
	
	public final Vector2f getPosition(){
		return new Vector2f(x, y);
	}
	public final void flippedHorizontally(boolean flippedHorizontally){
		this.flippedHorizontally = flippedHorizontally;
	}
	public final void flippedVertically(boolean flippedVertically){
		this.flippedVertically = flippedVertically;
	}
	public final void setCurrentIndex(int index){
		this.currentIndex = index;
	}
	public final int getCurrentIndex(){
		return currentIndex;
	}
	public final void setTextures(Texture[] textures){
		this.texture = textures;
	}
	
	public final void playAnim(boolean play){
		this.playAnim = play;
	}

}
