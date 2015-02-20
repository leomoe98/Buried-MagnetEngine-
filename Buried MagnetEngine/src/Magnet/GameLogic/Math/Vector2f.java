package Magnet.GameLogic.Math;

import java.io.Serializable;

public class Vector2f implements Serializable{
	
	public float x, y;
	
	public Vector2f(){
		x = 0.0f;
		y = 0.0f;
	}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector2f vector){
		x += vector.x;
		y += vector.y;
	}
	
	public double hypot(){
		return Math.hypot(x, y);
	}

}
