import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.Collideable;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Graphics.ParticleSystem;
import Magnet.GameView.Graphics.Quad;
import Magnet.GameView.Graphics.Texture;
import Magnet.GameView.Renderers.TileMapRenderer;

public class Player extends Actor implements Updatable, Renderable, Collideable{
	
	private Vector2f size, shift;
	private Quad player;
	private float speed;
	private int anim = IDLEANIM;
	
	private static final int JUMPANIM = 11;
	private static final int RUNANIM = 12;
	private static final int IDLEANIM = 13;
	
	private Texture[] idleAnim, runAnim, jumpAnim;
	private Texture particleTexture;
	
	private ArrayList<ParticleSystem> particles;
	private TileMapRenderer tmr;
	
	private float lastVelY = 0;
	
	public Player(float x, float y, float speed, TileMapRenderer tmr) {
		super(x, y);
		this.speed = speed;
		this.tmr = tmr;
		
	}
	
	@Override
	public void init() {
		size = new Vector2f(44, 56);
		shift = new Vector2f(0, 0);
		
		setPlayerSprites(ResourceBuffer.playerSprites);
		setParticleTexture(new Texture(new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB), false));
		
		particles = new ArrayList<ParticleSystem>();
		anim = IDLEANIM;
	}
	
	public void setPlayerSprites(Texture[] playerSprites){
		idleAnim = new Texture[4];
		
		for(int i = 0; i < idleAnim.length; i++){
			idleAnim[i] = playerSprites[i + 10];
		}
		
		runAnim = new Texture[5];
		for(int i = 0; i < runAnim.length; i++){
			runAnim[i] = playerSprites[i];
		}
		
		jumpAnim = new Texture[3];
		for(int i = 0; i < jumpAnim.length; i++){
			jumpAnim[i] = playerSprites[i + 20];
		}
		
		player = new Quad(getX(), getY(), 1f, idleAnim, 0.95f);
	}
	
	public void setParticleTexture(Texture texture){
		particleTexture = texture;
	}

	@Override
	public void render(int offsetX, int offsetY) {
		Vector2f pos = new Vector2f(getX() - 64/2 - offsetX, getY() - 64/2 - offsetY);
		
		if(player.getCurrentIndex() >= 1 && anim == JUMPANIM){
			player.playAnim(false);
		}
		
		if(anim == JUMPANIM && getVelocityY() == 0){
			player.setCurrentIndex(0);
			player.setTextures(idleAnim);
			anim = IDLEANIM;
			player.playAnim(true);
			
			int velX = -1;
			if(getVelocityX() < 0)velX *= -1;
			
			particles.add(new ParticleSystem(tmr, particleTexture, (int)(player.getPosition().x + player.getSize().x / 2.0), (int)(player.getPosition().y + player.getSize().y / 2.0), 1.3f, 0.2f, new Vector2f(velX, 0.7f * (float) -Math.sqrt(Math.abs(lastVelY))), 0.5f, -24, (int)Math.abs(lastVelY) * 3, 0.3f, 0.6f, 0.3f));
		}
		
		if(getVelocityY() != 0 && anim != JUMPANIM){
			player.setCurrentIndex(0);
			player.setTextures(jumpAnim);
			anim = JUMPANIM;
		}
		else if(getVelocityX() != 0 && anim != RUNANIM && anim != JUMPANIM){
			player.setCurrentIndex(0);
			player.setTextures(runAnim);
			anim = RUNANIM;
		}
		else if(anim != IDLEANIM && getVelocityX() == 0 && getVelocityY() == 0 && anim != JUMPANIM){
			player.setCurrentIndex(0);
			player.setTextures(idleAnim);
			anim = IDLEANIM;
		}
	
		player.setPosition(pos.x, pos.y);
		if(getVelocityX() > 0){
			player.flippedHorizontally(false);
		}else if(getVelocityX() < 0){
			player.flippedHorizontally(true);
		}
		player.render();
		
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).render();
		}
	}

	@Override
	public void update() {
		player.update();
		
		for(int i = 0; i < particles.size(); i++){
			if(particles.get(i).isDead()){
				particles.remove(i);
				i--;
			}
		}
		
		int x = (int)(player.getPosition().x + speed * 4);
		int y = (int)player.getPosition().y + (int)player.getSize().y;
		if(getVelocityX() < 0)x += (int)(player.getSize().x - speed * 8);
		
		if(particles.size() <= 0 && getVelocityY() == 0 && getVelocityX() != 0)particles.add(new ParticleSystem(tmr, particleTexture, x, y, 1.3f, 0.2f, new Vector2f((float) (-getVelocityX() * 0.3), -1.7f), 0.5f, -10, 24, 0.5f, 0.2f, 0.3f));
		
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).update();
		}
		
		lastVelY = getVelocityY();
	}

	@Override
	public int getWidth() {
		return (int)size.x - 1;
	}

	@Override
	public int getHeight() {
		return (int)size.y - 1;
	}

	@Override
	public int getShiftX() {
		return (int)shift.x;
	}

	@Override
	public int getShiftY() {
		return (int)shift.y;
	}
	
	public float getSpeed(){
		return speed;
	}

	@Override
	public boolean isGravityEnabled() {
		return true;
	}

}
