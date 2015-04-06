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
import Magnet.ApplicationLayer.Utils.TileMapUtils;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.ActorManager;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Audio;
import Magnet.GameView.GameView;
import Magnet.GameView.Graphics.ParallaxBackground;
import Magnet.GameView.Graphics.Texture;
import Magnet.GameView.Renderers.TileMapRenderer;

public class PlayerView extends GameView implements Renderable, Updatable {

	public PlayerView(String name, String levelPath) {
		super(name);

	}

	@Override
	public void init() {

		EventManager.queueEvent(new ActorVelocityRequestEvent(playerID, player.getSpeed(), true));
		ActorCreationListener acl = new ActorCreationListener() {

			@Override
			public void actorCreated(int actorID, ObjectConstruct actor) {
				if (actorID != playerID && actorID != hookID) {
					Object[] params = new Object[actor.getObjectParams().length];
					for (int i = 0; i < params.length; i++) {
						params[i] = actor.getObjectParams()[i];
						// Insert Changes for local Objects in here
						if (params[i].getClass() == Integer.class && (int) params[i] == ObjectConstruct.LOCAL_TILEMAPRENDERER) params[i] = tmr;
					}

					// Insert different Actor Constructors in here:
					if (actor.getObjectClass() == Player.class) am.addActor(new Player((float) params[0], (float) params[1], (float) params[2], tmr));
					if (actor.getObjectClass() == Hook.class) am.addActor(new Hook((float) params[0], (float) params[1], (float) params[2], (float) params[3], (boolean) params[4], (boolean) params[5], tmr, am, (int) params[8]));
					/*
					 * Constructor<?> ctor = null; try { //Insert different
					 * Actor Constructors in here: if(actor.getObjectClass() ==
					 * Player.class)ctor = Player.class.getConstructor(new
					 * Class<?>[]{float.class, float.class, float.class,
					 * TileMapRenderer.class}); } catch (NoSuchMethodException |
					 * SecurityException e) { e.printStackTrace(); } try {
					 * am.addActor((Actor) ctor.newInstance(params)); } catch
					 * (InstantiationException | IllegalAccessException|
					 * IllegalArgumentException| InvocationTargetException e) {
					 * e.printStackTrace(); }
					 */
				}
			}
		};
		// EventManager.addListener(acl);

		ActorMovementListener aml = new ActorMovementListener() {
			@Override
			public void actorMovementChange(int actorID, Vector2f newPosition, Vector2f newVelocity) {
				Actor actor = am.getActor(actorID);
				actor.setPosition(newPosition);
				actor.setVelocity(newVelocity);
			}
		};
		// EventManager.addListener(aml);

		ActorRemoveListener arl = new ActorRemoveListener() {
			@Override
			public void actorRemoved(int actorID) {
				am.removeActor(actorID);
				if (actorID == hookID) {
					hookActive = false;
					hookID = -1;
				}
			}
		};
		// EventManager.addListener(arl);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(int offsetX, int offsetY) {

	}

}
