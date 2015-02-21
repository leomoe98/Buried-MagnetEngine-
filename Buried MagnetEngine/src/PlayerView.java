
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import Magnet.ApplicationLayer.Input;
import Magnet.ApplicationLayer.Events.ActorCreationListener;
import Magnet.ApplicationLayer.Events.ActorCreationRequestEvent;
import Magnet.ApplicationLayer.Events.ActorMovementListener;
import Magnet.ApplicationLayer.Events.ActorPositionListener;
import Magnet.ApplicationLayer.Events.ActorRemoveListener;
import Magnet.ApplicationLayer.Events.ActorRemoveRequestEvent;
import Magnet.ApplicationLayer.Events.ActorVelocityRequestEvent;
import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Events.ObjectConstruct;
import Magnet.ApplicationLayer.Utils.ResourceUtils;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.ActorManager;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.GameView;
import Magnet.GameView.Graphics.ParallaxBackground;
import Magnet.GameView.Graphics.Texture;
import Magnet.GameView.Renderers.TileMapRenderer;

public class PlayerView extends GameView implements Renderable, Updatable{
	
	private TileMapRenderer tmr;
	private ActorManager am;
	
	private ParallaxBackground bg;
	private ParallaxBackground fog;
	
	private BufferedImage light;
	private AlphaComposite ac;
	
	private boolean hookActive = false;
	
	private float fogX;
	private float fogSpeed = 0.5f;
	private float fogDistance = 1.3f;
	
	private int playerID, hookID = -1;
	
	public PlayerView(String name) {
		super(name);
		
	}
	
	@Override
	public void init() {
		bg = new ParallaxBackground(new Texture(ResourceBuffer.levelBg, false), 1f, 1.5f, true);
		fog = new ParallaxBackground(new Texture(ResourceUtils.loadBufferedImage("/fog1.png", true), true), 3f, fogDistance, true);
		fogX = 0;
		
		light = ResourceUtils.loadBufferedImage("/light.png", true);
		ac = AlphaComposite.getInstance(AlphaComposite.DST_OUT);
		
		am = new ActorManager();
		
		tmr = new TileMapRenderer(TileMapRenderer.ORTHOGONAL_RENDERER, am, ResourceBuffer.tileset, 20, 1f, "/testLevel.txt", true);
		
		ObjectConstruct playerCon = new ObjectConstruct(Player.class, new Object[]{200f, 800f, 7.6f, ObjectConstruct.LOCAL_TILEMAPRENDERER});
		Player player = new Player(((Float) playerCon.getObjectParams()[0]).intValue(), ((Float) playerCon.getObjectParams()[1]).intValue(), ((Float) playerCon.getObjectParams()[2]).intValue(), tmr);
		
		EventManager.queueEvent(new ActorCreationRequestEvent(player.getID(), playerCon));
		playerID = player.getID();
		am.addActor(player);
		
		EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, player.getSpeed(), true));
		ActorCreationListener acl = new ActorCreationListener(){

			@Override
			public void actorCreated(int actorID, ObjectConstruct actor) {
				if(actorID != playerID && actorID != hookID){
					Object[] params = new Object[actor.getObjectParams().length];
					for(int i = 0; i < params.length; i++){
						params[i] = actor.getObjectParams()[i];
						//Insert Changes for local Objects in here
						if(params[i].getClass() == Integer.class && (int)params[i] == ObjectConstruct.LOCAL_TILEMAPRENDERER)params[i] = tmr;
					}
					
					//Insert different Actor Constructors in here:
					if(actor.getObjectClass() == Player.class)am.addActor(new Player((float)params[0], (float)params[1], (float)params[2], tmr));
					if(actor.getObjectClass() == Hook.class)am.addActor(new Hook((float)params[0], (float)params[1], (float)params[2], (float)params[3], (boolean)params[4], (boolean)params[5], tmr, am, (int)params[8]));
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
				}
			}
		};
		//EventManager.addListener(acl);
		
		ActorMovementListener aml = new ActorMovementListener(){
			@Override
			public void actorMovementChange(int actorID, Vector2f newPosition, Vector2f newVelocity) {
				Actor actor = am.getActor(actorID);
				actor.setPosition(newPosition);
				actor.setVelocity(newVelocity);
			}
		};
		//EventManager.addListener(aml);
		
		ActorRemoveListener arl = new ActorRemoveListener(){
			@Override
			public void actorRemoved(int actorID){
				am.removeActor(actorID);
				if(actorID == hookID){
					hookActive = false;
					hookID = -1;
				}
			}
		};
		//EventManager.addListener(arl);
	}

	@Override
	public void update() {
		
		boolean down = Input.isKeyDown(KeyEvent.VK_DOWN);
		boolean up = Input.isKeyDown(KeyEvent.VK_UP);
		boolean left = Input.isKeyDown(KeyEvent.VK_LEFT);
		boolean right = Input.isKeyDown(KeyEvent.VK_RIGHT);
		
		boolean w = Input.isKeyDown(KeyEvent.VK_W);
		boolean a = Input.isKeyDown(KeyEvent.VK_A);
		boolean s = Input.isKeyDown(KeyEvent.VK_S);
		boolean d = Input.isKeyDown(KeyEvent.VK_D);
		
		float speed = ((Player) am.getActor(playerID)).getSpeed();
		
		if(w)EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, -10, false));
		if(a)EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, -speed, true)); 
		else if(d)EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, speed, true));
		else EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, 0, true));
		
		if((up || left|| right )&& !hookActive){
			hookActive = true;
			
			boolean onXAxis = true;
			if(up){
				onXAxis = false;
			}
			
			boolean hRight = true;
			if(left){
				hRight = false;
			}
			
			ObjectConstruct hookCon = new ObjectConstruct(
					Hook.class, new Object[]{(float)am.getActor(playerID).getPosition().x,
					(float)am.getActor(playerID).getPosition().y,
					16f, 64f * 6f, onXAxis, hRight, ObjectConstruct.LOCAL_TILEMAPRENDERER, ObjectConstruct.LOCAL_TILEMAPRENDERER, playerID});
			
			Hook hook = new Hook((float)hookCon.getObjectParams()[0], (float)hookCon.getObjectParams()[1],
					(float)hookCon.getObjectParams()[2], (float)hookCon.getObjectParams()[3],
					(boolean)hookCon.getObjectParams()[4], (boolean)hookCon.getObjectParams()[5], tmr, am, playerID);
			
			hookID = hook.getID();
			
			am.addActor(hook);
			
			EventManager.queueEvent(new ActorCreationRequestEvent(hook.getID(), hookCon));
		}
		
		for(int i = 0; i < am.getAllActors().size(); i++){
			if(Updatable.class.isInstance(am.getAllActors().get(i)))((Updatable) am.getAllActors().get(i)).update();
		}
		
		tmr.cameraTrackActor(playerID, 0.1f, 0, 128);
		fogX += fogSpeed;
		if(fogX / fogDistance >= fog.getSize().x)fogX = 0;
	}
	
	

	@Override
	public void render(int offsetX, int offsetY) {
		
		//CLIP BG TO MAP
		int clipX = (int)(-tmr.getCameraPos().x);
		if(clipX < 0)clipX = 0;
		int clipY = (int)(-tmr.getCameraPos().y);
		if(clipY < 0)clipY = 0;
		int clipMaxX = (int)(tmr.getCameraPos().x + Main.getDisplayWidth());
		if(clipMaxX > tmr.getSize().x * tmr.getTileSize())clipMaxX = (int) (-tmr.getCameraPos().x + tmr.getSize().x * tmr.getTileSize());
		int clipMaxY = (int)(tmr.getCameraPos().y + Main.getDisplayHeight());
		if(clipMaxY > tmr.getSize().y * tmr.getTileSize())clipMaxY = (int) (-tmr.getCameraPos().y + tmr.getSize().y * tmr.getTileSize());
		
		//RENDER BG
		Main.getGraphics().setClip(clipX, clipY, clipMaxX, clipMaxY);
		bg.render(tmr.getCameraPos().x, tmr.getCameraPos().y);
		//fog.render(tmr.getCameraPos().x + fogX, tmr.getCameraPos().y);
		
		//LIGHTING EFFECT
		//ac = AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.5f);
		//Main.getGraphics().setComposite(ac);
		
		Main.getGraphics().drawImage(light, 0, 0, Main.getDisplayWidth(), Main.getDisplayHeight(), null);
		
		//ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		//Main.getGraphics().setComposite(ac);
		Main.getGraphics().setClip(0, 0, Main.getDisplayWidth(), Main.getDisplayHeight());
		
		
		//RENDER TILEMAP
		tmr.render();
	}

}
