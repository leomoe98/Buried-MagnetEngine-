import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import Magnet.ApplicationLayer.Events.ActorRemoveRequestEvent;
import Magnet.ApplicationLayer.Events.ActorVelocityRequestEvent;
import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Utils.ResourceUtils;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.ActorManager;
import Magnet.GameLogic.Actors.Collideable;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Maps.TileMap;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Graphics.ParticleSystem;
import Magnet.GameView.Graphics.Quad;
import Magnet.GameView.Graphics.Texture;
import Magnet.GameView.Renderers.TileMapRenderer;

public class Hook extends Actor implements Updatable, Renderable, Collideable{
	
	private Vector2f size, shift;
	private BufferedImage sprite;
	private float speed, range;
	private boolean back, pullPlayer;
	
	private Vector2f startPos;
	
	private boolean onXAxis,right;
	
	private ActorManager am;
	private int playerID;
	private TileMapRenderer tmr;
	
	private ParticleSystem ps;
	
	public Hook(float x, float y, float hSpeed, float range, boolean onXAxis, boolean right, TileMapRenderer tmr, ActorManager am, int playerID){
		super(x, y);
		this.speed = hSpeed;
		this.onXAxis = onXAxis;
		this.am = am;
		this.playerID = playerID;
		this.tmr = tmr;
		this.right = right;
		this.range = range;
		
		if(!right && onXAxis) speed = speed*(-1);
		else if(!onXAxis)speed = speed*(-1);
		
		startPos = new Vector2f(x, y);
		
		if(!onXAxis){
			
			//Rotation Information
			double rotationRequired = Math.toRadians(-90);
			double locationX = sprite.getWidth() / 2;
			double locationY = sprite.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

			BufferedImage temp = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			// Drawing the rotated image at the required drawing locations
			((Graphics2D) temp.getGraphics()).setBackground(new Color(0,0,0,0));
			temp.getGraphics().drawImage(op.filter(sprite, null), 0, 0, null);
			
			sprite = temp;
		}
	}

	@Override
	public void init() {
		back = false;
		pullPlayer = false;
		
		size = new Vector2f(16, 16);
		shift = new Vector2f(0, 0);
		
		sprite = ResourceUtils.loadBufferedImage("/hook.png", false);
		
	}

	@Override
	public void render(int offsetX, int offsetY) {
		int Twidth;
		int Tx;
		
		if(!right){
			Twidth = (int) -size.x;
			Tx = (int) (getX()+size.x);
		}else{
			Twidth = (int) size.x;
			Tx = (int)getX();
		}
		//if(ps == null || ps.isDead())ps = new ParticleSystem(tmr, new Texture(new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB), false), (int)getX()-offsetX, (int)getY()-offsetY, 1.3f, 0.2f, new Vector2f((float) (-speed * 0.05), -3.7f), 0.5f, -10, 12, 0.5f, 0.05f, 0.3f);
		
		Main.getGraphics().drawImage(sprite, Tx - offsetX, (int)getY() - offsetY, Twidth, (int)size.y, null);
		if(ps != null)ps.render();
		Main.getGraphics().setStroke(new BasicStroke(3));
		Main.getGraphics().setColor(Color.BLACK);
		Main.getGraphics().drawLine((int)getX() + 8 - offsetX, (int)getY() + 8 - offsetY, (int)am.getActor(playerID).getX() - offsetX, (int)am.getActor(playerID).getY() - offsetY);
	}

	@Override
	public void update() {
		((Player) am.getActor(playerID)).setControlable(false);
		((Player) am.getActor(playerID)).enableGravity(false);
		am.getActor(playerID).setVelocityY(0);
		
		
		if(Math.abs(getX() - startPos.x) > range)back = true;
		if(Math.abs(getY() - startPos.y) > range)back = true;
		
		if(!pullPlayer){
			if(!back){
				EventManager.queueEvent(new ActorVelocityRequestEvent(getID(),speed, onXAxis));
			}else{
				EventManager.queueEvent(new ActorVelocityRequestEvent(getID(),-speed * 3, onXAxis));
			}
		}else{
			if(onXAxis)am.getActor(playerID).setVelocityX(speed);
			else{
				am.getActor(playerID).setVelocityY(speed);
			}
		}
		if(isColliding() && !pullPlayer){
			pullPlayer = true;
		}
		if(isDead()){
			((Player) am.getActor(playerID)).setControlable(true);
			((Player) am.getActor(playerID)).enableGravity(true);
			am.getActor(playerID).setVelocity(new Vector2f(0, 0));
			EventManager.queueEvent(new ActorRemoveRequestEvent(getID()));
		}
		if(ps != null)ps.update();
	}
	
	public boolean isDead(){
		if(!onXAxis && (am.getActor(playerID).getX() < getX() - (2 * 64) || am.getActor(playerID).getX() > getX() + (2 * 64)))return true;
		if(am.getActor(playerID).getY() < getY() - 64)return true;
		if((back || pullPlayer) && TileMap.intersects(this, am.getActor(playerID)))return true;
		else return false;
	}

	@Override
	public int getWidth() {
		return (int) (size.x - 1);
	}

	@Override
	public int getHeight() {
		return (int) (size.y - 1);
	}

	@Override
	public int getShiftX() {
		return (int) shift.x;
	}

	@Override
	public int getShiftY() {
		return (int) shift.y;
	}

	@Override
	public boolean isGravityEnabled() {
		return false;
	}

}
