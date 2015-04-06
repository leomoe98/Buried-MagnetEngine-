package Magnet.GameLogic;

public abstract class GameState {

	public GameState(){
		
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void render();
	
}
