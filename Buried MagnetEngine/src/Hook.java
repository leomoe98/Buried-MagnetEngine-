import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import Magnet.ApplicationLayer.Events.ActorVelocityRequestEvent;
import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Utils.ResourceUtils;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.Collideable;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Graphics.Quad;
import Magnet.GameView.Renderers.TileMapRenderer;

public class Hook extends Actor implements Updatable, Renderable, Collideable{
	
	private Vector2f size, shift;
	private BufferedImage sprite;
	private float speed, range;
	private boolean back;
	
	private Vector2f startPos;
	
	private boolean onXAxis,right;
	
	private TileMapRenderer tmr;
	
	public Hook(float x, float y, float speed, float range, boolean onXAxis, boolean right, TileMapRenderer tmr){
		super(x, y);
		this.speed = speed;
		this.onXAxis = onXAxis;
		this.tmr = tmr;
		this.right = right;
		this.range = range;
		
		startPos = new Vector2f(x, y);
		
		if(!right) speed = speed*(-1);
	}

	@Override
	public void init() {
		back = false;
		
		size = new Vector2f(44, 56);
		shift = new Vector2f(0, 0);
		
		sprite = ResourceUtils.loadBufferedImage("/hook.png", false);
		
		if(!onXAxis){
			//Rotation Information
			double rotationRequired = Math.toRadians(45);
			double locationX = sprite.getWidth() / 2;
			double locationY = sprite.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

			// Drawing the rotated image at the required drawing locations
			sprite.getGraphics().drawImage(op.filter(sprite, null), 0, 0, null);
		}
		
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
		
		Main.getGraphics().drawImage(sprite, Tx, (int)getY(), Twidth, (int)size.y, null);
	}

	@Override
	public void update() {
		if(Math.abs(getX() - startPos.x) > range)back = true;
		if(Math.abs(getY() - startPos.y) > range)back = true;
		
		if(!back){
			EventManager.queueEvent(new ActorVelocityRequestEvent(getID(),speed, onXAxis));
		}else{
			EventManager.queueEvent(new ActorVelocityRequestEvent(getID(),-speed * 3, onXAxis));
		}
		
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
