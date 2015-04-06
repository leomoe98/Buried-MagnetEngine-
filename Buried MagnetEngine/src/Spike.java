import java.awt.image.BufferedImage;

import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.ActorManager;
import Magnet.GameLogic.Actors.Collideable;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Maps.TileMap;
import Magnet.GameLogic.Math.Vector2f;

public class Spike extends Actor implements Renderable, Updatable, Collideable{

	private ActorManager am;
	private int playerID;
	private BufferedImage sprite;
	private Vector2f playerSpawn;
	
	public Spike(float x, float y, ActorManager am, int playerID, Vector2f playerSpawn) {
		super(x, y);
		this.am = am;
		this.playerID = playerID;
		this.playerSpawn = playerSpawn;
	}

	@Override
	public int getWidth() {
		return 36;
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public int getShiftX() {
		return 34;
	}

	@Override
	public int getShiftY() {
		return 34;
	}

	@Override
	public void update() {
		if(TileMap.intersects(this, am.getActor(playerID)))am.getActor(playerID).setPosition(playerSpawn);
	}

	@Override
	public void render(int offsetX, int offsetY) {
		Main.getGraphics().drawImage(sprite, (int)getX() - offsetX, (int)getY() - offsetY + 4, null);
	}

	@Override
	public void init() {
		sprite  = ResourceBuffer.spike;
	}

	@Override
	public boolean isGravityEnabled() {
		return false;
	}

}
