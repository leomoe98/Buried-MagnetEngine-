package Magnet.GameLogic.Actors;

import java.util.ArrayList;

public class ActorManager {
	
	private ArrayList<Actor> actors;
	
	public ActorManager(){
		actors = new ArrayList<Actor>();
	}
	
	public final void addActor(Actor actor){
		actors.add(actor);
	}
	
	public final void removeActor(Actor actor){
		actors.remove(actor);
	}
	
	public final void removeActor(int ID){
		Actor actor = getActor(ID);
		if(actor != null){
			actors.remove(actor);
		}else{
			System.out.println("not found");
		}
	}
	
	public final Actor getActor(int ID){
		for(int i = 0; i < actors.size(); i++){
			if(actors.get(i).getID() == ID)return actors.get(i);
		}
		return null;
	}
	
	public final ArrayList<Actor> getAllActors(){
		return actors;
	}

}
