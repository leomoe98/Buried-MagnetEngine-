import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Magnet.ApplicationLayer.Events.ActorCreationEvent;
import Magnet.ApplicationLayer.Events.ActorCreationRequestListener;
import Magnet.ApplicationLayer.Events.ActorMovementEvent;
import Magnet.ApplicationLayer.Events.ActorRemoveEvent;
import Magnet.ApplicationLayer.Events.ActorRemoveListener;
import Magnet.ApplicationLayer.Events.ActorRemoveRequestListener;
import Magnet.ApplicationLayer.Events.ActorVelocityRequestListener;
import Magnet.ApplicationLayer.Events.ActorPositionEvent;
import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Events.ObjectConstruct;
import Magnet.GameLogic.GameLogic;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.ActorManager;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Maps.TileMap;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Renderers.TileMapRenderer;

public class PlayLogic extends GameLogic{

	private String levelPath;
	
	private TileMap tileMap;
	private ActorManager am;
	
	private float g = 60f;
	private float maxFallSpeed = 24f;
	
	public PlayLogic(String levelPath){
		this.levelPath = levelPath;
	}
	
	@Override
	public void init() {
		am = new ActorManager();
		tileMap = new TileMap(levelPath, true, 64);
		
		ActorCreationRequestListener acrl = new ActorCreationRequestListener(){
			@Override
			public void actorCreationRequest(int actorID, ObjectConstruct actor) {
				Object[] params = new Object[actor.getObjectParams().length];
				for(int i = 0; i < params.length; i++){
					params[i] = actor.getObjectParams()[i];
					//Insert Changes for local Objects in here:
					if(params[i].getClass() == int.class && (int)params[i] == ObjectConstruct.LOCAL_TILEMAPRENDERER)params[i] = null;
				}
				
				//Insert different Actor Constructors in here:
				if(actor.getObjectClass() == Player.class){
					Player p = new Player((float)params[0], (float)params[1], (float)params[2], null);
					p.setID(actorID);
					am.addActor(p);
				}
				if(actor.getObjectClass() == Hook.class){
					Hook h = new Hook((float)params[0], (float)params[1],
							(float)params[2], (float)params[3],
							(boolean)params[4], (boolean)params[5], null, am, (int)params[8]);
					h.setID(actorID);
					am.addActor(h);
				}
				/*if(actor.getObjectClass() == Exit.class){
					Exit e = new Exit((float)params[0], (float)params[1],
							am, (int)params[3]);
					e.setID(actorID);
					am.addActor(e);
				}*/
				/*Constructor<?> ctor = null;
				try {
					//Insert different Actor Constructors in here:
					if(actor.getObjectClass() == Player.class)ctor = Player.class.getConstructor(new Class<?>[]{float.class, float.class, float.class, TileMapRenderer.class});
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				try {
					am.addActor((Actor) ctor.newInstance(params));
				} catch (InstantiationException | IllegalAccessException| IllegalArgumentException| InvocationTargetException e) {
					e.printStackTrace();
				}*/
				
				EventManager.queueEvent(new ActorCreationEvent(actorID, actor));
			}
		};
		//EventManager.addListener(acrl);
		
		ActorVelocityRequestListener avrl = new ActorVelocityRequestListener(){
			@Override
			public void actorVelocityRequest(int actorID, float velocity, boolean onXAxis) {
				
				Actor actor = am.getActor(actorID);
				if(actor == null)return;
				if(onXAxis){
					actor.setVelocityX(velocity);
				}else{
					if(actor.getClass() == Hook.class){
						if(onXAxis)actor.setVelocityX(velocity);
						else actor.setVelocityY(velocity);
					}else if(tileMap.checkCollision(actor, new Vector2f(actor.getX(), actor.getY() + 1))){
						actor.setJumpVelocity(velocity);
					}
				}
			}
		};
		//EventManager.addListener(avrl);
		
		ActorRemoveRequestListener arrl = new ActorRemoveRequestListener(){
			@Override
			public void actorRemoveRequest(int actorID){
				am.removeActor(actorID);
				EventManager.queueEvent(new ActorRemoveEvent(actorID));
			}
		};
		//EventManager.addListener(arrl);
		
	}
	
	private void calculatePhysics(){
		ArrayList<Actor> actors = am.getAllActors();
		
		for(int i = 0; i < actors.size(); i++){
			
			Actor actor = actors.get(i);
			if(actor.isGravityEnabled())actor.addToMovementTime((float)(1.0/60.0));
			
			if(!tileMap.checkCollision(actor, new Vector2f(actor.getX(), actor.getY() + 1))){
				if(actor.isGravityEnabled())actor.setVelocityY(g * actor.getMovementTimeY() * actor.getMovementTimeY() + actor.getJumpVelocity());
			}else{
				if(actor.jumpedOff() == false){
					
					actor.setVelocityY(actor.getJumpVelocity());
					actor.setJumpedOff(true);
				}else{
					if(actor.isGravityEnabled())actor.setVelocityY(0);
					actor.setJumpVelocity(0);
					actor.setJumpedOff(false);
				}
				actor.resetMovementTimeY();
			}
			
			if(actor.getVelocityY() > maxFallSpeed)actor.setVelocityY(maxFallSpeed);
			
			if(actor.getVelocityX() != 0)moveActor(actor, actor.getVelocityX(), true);
			if(actor.getVelocityY() != 0)moveActor(actor, actor.getVelocityY(), false);
		}
	}
	
	private void moveActor(Actor actor, double distance, boolean onXAxis){
		actor.setColliding(false);
		Vector2f newPos = new Vector2f(0, 0);
		if(onXAxis)newPos.x = (float) tileMap.possibleMovementX(actor, distance);
		else newPos.y = (float) tileMap.possibleMovementY(actor, distance);
		newPos.add(actor.getPosition());
		actor.setPosition(newPos);
		EventManager.queueEvent(new ActorMovementEvent(actor.getID(), newPos, actor.getVelocity()));
	}

	@Override
	public void update() {
		ArrayList<Actor> allActors = am.getAllActors();
		for(int i = 0; i < allActors.size(); i++){
			if(Updatable.class.isInstance(allActors.get(i))){
				((Updatable) allActors.get(i)).update();
			}
		}
		calculatePhysics();
	}

}
