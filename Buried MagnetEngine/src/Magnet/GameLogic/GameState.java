package Magnet.GameLogic;

import java.util.ArrayList;

import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameView.GameView;

public abstract class GameState {
	
	private ArrayList<Object> gameViews = new ArrayList<Object>();
	private GameLogic gameLogic;

	public GameState(GameLogic gameLogic){
		this.gameLogic = gameLogic;
	}
	
	public final void init(){
		gameLogic.initLogic();
		for(int i = 0; i < gameViews.size(); i++){
			((GameView) gameViews.get(i)).init();
		}
	}
	
	public final void update(){
		gameLogic.update();
		for(int i = 0; i < gameViews.size(); i++){
			if(Updatable.class.isInstance(gameViews.get(i))){
				((Updatable) gameViews.get(i)).update();
			}
		}
	}
	
	public final void render(){
		for(int i = 0; i < gameViews.size(); i++){
			if(Renderable.class.isInstance(gameViews.get(i))){
				((Renderable) gameViews.get(i)).render(0, 0);
			}
		}
	}

	/**
	 * {@code public void addGameView(GameView gameView)}
	 * 
	 * <p>
	 * 
	 * Adds a Game View to the GameView list of the GameState. The GameViews which extend the WindowView will be rendered atomatically.
	 * 
	 * @param gameView
	 */
	public final void addGameView(GameView gameView){
		gameViews.add(gameView);
	}
	
	
	/**
	 * {@code public void removeGameView(GameView gameView)}
	 * 
	 * <p>
	 * 
	 *  Removes a Game View of the GameView list of the GameState.
	 * 
	 * @param gameView
	 */
	public final void removeGameView(GameView gameView){
		gameViews.remove(gameView);
	}
	
}
