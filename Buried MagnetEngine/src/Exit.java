import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;

import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Events.GameStateEvent;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.ActorManager;
import Magnet.GameLogic.Actors.Collideable;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Maps.TileMap;


public class Exit extends Actor implements Updatable, Renderable, Collideable{

	private BufferedImage image;
	private ActorManager am;
	private int playerID;
	
	public Exit(float x, float y, ActorManager am, int playerID) {
		super(x, y);
		image = ResourceBuffer.exit;
		this.am =am;
		this.playerID = playerID;
	}

	@Override
	public int getWidth() {
		return 96;
	}

	@Override
	public int getHeight() {
		return 10;
	}

	@Override
	public int getShiftX() {
		return 64;
	}

	@Override
	public int getShiftY() {
		return 64;
	}

	@Override
	public void render(int offsetX, int offsetY) {
		Main.getGraphics().drawImage(image, (int)getX() - offsetX, (int)getY() - offsetY, image.getWidth(), image.getHeight(),null);
		Main.getGraphics().setStroke(new BasicStroke(5));
		Main.getGraphics().setColor(Color.BLACK);
		Main.getGraphics().drawLine((int)getX() + 64 - offsetX, (int)getY() + 33 - offsetY, (int)getX() + 64 - offsetX, (int)getY() - offsetY - 9999);
	
	}

	@Override
	public void update() {
		if(TileMap.intersects(am.getActor(playerID), this)){
			Main.loadLevel(2);
		}
	}

	@Override
	public void init() {
		
	}

	@Override
	public boolean isGravityEnabled() {
		return false;
	}

}
