
import java.awt.AlphaComposite;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import Magnet.ApplicationLayer.Input;
import Magnet.ApplicationLayer.Events.ActorCreationListener;
import Magnet.ApplicationLayer.Events.ActorCreationRequestEvent;
import Magnet.ApplicationLayer.Events.ActorPositionListener;
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
	
	private float fogX;
	private float fogSpeed = 0.5f;
	private float fogDistance = 1.3f;
	
	private int playerID;
	
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
				if(actorID != playerID){
					Object[] params = new Object[actor.getObjectParams().length];
					for(int i = 0; i < params.length; i++){
						params[i] = actor.getObjectParams()[i];
						//Insert Changes for local Objects in here
						if((int)params[i] == ObjectConstruct.LOCAL_TILEMAPRENDERER)params[i] = tmr;
					}
					
					//Insert different Actor Constructors in here:
					if(actor.getObjectClass() == Player.class)am.addActor(new Player((float)params[0], (float)params[1], (float)params[2], tmr));
					
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
		
		ActorPositionListener apl = new ActorPositionListener(){
			@Override
			public void actorPositionChange(int actorID, Vector2f newPosition) {
				Actor actor = am.getActor(actorID);
				actor.setPosition(newPosition);
			}
		};
		//EventManager.addListener(apl);
		
	}

	@Override
	public void update() {
		boolean down = Input.isKeyDown(KeyEvent.VK_DOWN);
		boolean up = Input.isKeyDown(KeyEvent.VK_UP);
		boolean left = Input.isKeyDown(KeyEvent.VK_LEFT);
		boolean right = Input.isKeyDown(KeyEvent.VK_RIGHT);
		
		float speed = ((Player) am.getActor(playerID)).getSpeed();
		
		if(up)EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, -10, false));
		if(left)EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, -speed, true)); 
		else if(right)EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, speed, true));
		else EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, 0, true));
		
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
		fog.render(tmr.getCameraPos().x + fogX, tmr.getCameraPos().y);
		
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
