package Magnet.ApplicationLayer.Events;

public class GameStateEvent extends Event{
	
	private int gameState;
	
	public GameStateEvent(int gameState){
		this.gameState = gameState;
	}
	
	public final int getGameState(){
		return gameState;
	}
}
