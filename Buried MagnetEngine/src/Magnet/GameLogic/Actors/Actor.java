package Magnet.GameLogic.Actors;

import Magnet.GameLogic.Math.Vector2f;

public abstract class Actor{
	
	private int ID;
	private Vector2f position;
	private Vector2f velocity;
	private Vector2f movementTimer;
	
	private boolean jumpedOff;
	private float jumpVelocity;
	
	public Actor(float x, float y){
		ID = IDGenerator.generateID();
		position = new Vector2f(x, y);
		velocity = new Vector2f(0, 0);
		movementTimer = new Vector2f(0, 0);
		jumpedOff = false;
		init();
	}
	
	public final void setID(int newID){
		this.ID = newID;
	}
	
	public abstract void init();
	
	public final int getID(){
		return ID;
	}
	
	public final void setX(float x){
		position.x = x;
	}
	
	public final void setY(float y){
		position.y = y;
	}
	
	public final void setPosition(Vector2f position){
		this.position = position;
	}
	
	public final float getX(){
		return position.x;
	}
	
	public final float getY(){
		return position.y;
	}
	
	public final Vector2f getPosition(){
		return position;
	}
	
	public final void setVelocity(Vector2f velocity){
		this.velocity = velocity;
	}
	
	public final void setVelocityX(float velocity){
		this.velocity.x = velocity;
	}
	
	public final void setVelocityY(float velocity){
		this.velocity.y = velocity;
	}
	
	public final Vector2f getVelocity(){
		return velocity;
	}
	
	public final float getVelocityX(){
		return velocity.x;
	}
	
	public final float getVelocityY(){
		return velocity.y;
	}
	
	public final void resetMovementTimeX(){
		movementTimer.x = 0;
	}
	
	public final void resetMovementTimeY(){
		movementTimer.y = 0;
	}
	
	public final void addToMovementTime(float time){
		movementTimer.x += time;
		movementTimer.y += time;
	}
	
	public final float getMovementTimeX(){
		return movementTimer.x;
	}
	
	public final float getMovementTimeY(){
		return movementTimer.y;
	}
	
	public final void setJumpVelocity(float jumpVelocity){
		this.jumpVelocity = jumpVelocity;
	}
	
	public final float getJumpVelocity(){
		return jumpVelocity;
	}
	
	public final void setJumpedOff(boolean jumpedOff){
		this.jumpedOff = jumpedOff;
	}
	
	public final boolean jumpedOff(){
		return jumpedOff;
	}
	
	public abstract boolean isGravityEnabled();

}
