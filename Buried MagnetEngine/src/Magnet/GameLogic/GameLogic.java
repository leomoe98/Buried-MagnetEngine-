package Magnet.GameLogic;

import Magnet.GameLogic.Actors.ActorManager;

public abstract class GameLogic{
	
	public ActorManager actorManager;
	
	public final void initLogic(){
		actorManager = new ActorManager();
		init();
	}
	
	public abstract void init();
	public abstract void update();
	
}
