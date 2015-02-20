package Magnet.GameView.Graphics;

import java.util.ArrayList;
import java.util.Random;

import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Renderers.TileMapRenderer;

public class ParticleSystem {
	
	private Random r;
	
	private TileMapRenderer tmr;
	private Texture texture;
	private Vector2f position;
	private float scale;
	private Vector2f velocity;
	private float rVelocity;
	private float rScale;
	private float gravity;
	private int amount;
	private float rAmount;
	private float lifeTime;
	private float rLifeTime;
	
	private float timer;
	
	private Vector2f lastMapPos;
	
	private ArrayList<Quad> particles;
	private ArrayList<Vector2f> velocities;
	private ArrayList<Float> lifeTimes;
	
	public ParticleSystem(TileMapRenderer tmr, Texture texture, int x, int y, float scale, float randomScale, Vector2f velocity, float randomVelocity, float gravity, int amount, float randomAmount, float lifeTime, float randomLifeTime){
		this.tmr = tmr;
		r = new Random();
		this.texture = texture;
		position = new Vector2f(x, y);
		this.scale = scale;
		this.velocity = velocity;
		this.gravity = gravity;
		rVelocity = randomVelocity;
		rScale = randomScale;
		this.amount = amount;
		rAmount = randomAmount;
		this.lifeTime = lifeTime;
		rLifeTime = randomLifeTime;
		
		particles = new ArrayList<Quad>();
		
		timer = 0;
		
		for(int i = 0; i < amount + amount * r.nextGaussian() * rAmount; i++){
			particles.add(new Quad(position.x, position.y, (float) (scale + scale * r.nextGaussian() * rScale), texture));
		}
		
		velocities = new ArrayList<Vector2f>();
		
		for(int i = 0; i < particles.size(); i++){
			velocities.add(new Vector2f((float)(velocity.x + velocity.x * r.nextGaussian() * rVelocity), (float)(velocity.y + velocity.y * r.nextGaussian() * rVelocity)));
		}
		
		lifeTimes = new ArrayList<Float>();
		
		for(int i = 0; i < particles.size(); i++){
			lifeTimes.add(new Float(lifeTime + lifeTime * r.nextGaussian() * rLifeTime));
		}
	}
	
	public void render(){
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).render();
		}
	}
	
	public void update(){
		timer += 1.0/60.0;
		
		for(int i = 0; i < particles.size(); i++){
			if(timer > lifeTimes.get(i)){
				lifeTimes.remove(i);
				velocities.remove(i);
				particles.remove(i);
				i--;
			}
		}
		
		
		
		for(int i = 0; i < particles.size(); i++){
			if(lastMapPos != null && tmr != null){
				particles.get(i).setPosition(particles.get(i).getPosition().x + velocities.get(i).x - (tmr.getCameraPos().x - lastMapPos.x), particles.get(i).getPosition().y + velocities.get(i).y - gravity * timer * timer - (tmr.getCameraPos().y - lastMapPos.y));
			}
			else{
				particles.get(i).setPosition(particles.get(i).getPosition().x + velocities.get(i).x, particles.get(i).getPosition().y + velocities.get(i).y - gravity * timer * timer);
			}
		}
		
		if(tmr != null)lastMapPos = tmr.getCameraPos();
	}
	
	public final void setPosition(int x, int y){
		position.x = x;
		position.y = y;
	}
	
	public final boolean isDead(){
		if(particles.size() <= 0)return true;
		else return false;
	}

}
