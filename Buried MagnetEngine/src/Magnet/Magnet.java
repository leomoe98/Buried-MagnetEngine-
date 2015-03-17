package Magnet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import Magnet.ApplicationLayer.Input;
import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.GameLogic.GameState;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Window;

/**
 * The Base class of "Magnet Engine". 
 * 
 * @author Léon Möhring
 * @version 0.0
 *
 */
public abstract class Magnet implements Runnable{
	
	private static Window display;
	
	private static Thread thread;
	private static boolean running = false;
	private static int preferredUps = 60;
	public static int actualFps = 0;
	public static int actualUps = 0;
	
	private static Vector2f size;
	private static String title = "Magnet";
	private int bufferCount;
	
	public abstract void init();
	
	protected static ArrayList<GameState> gameStates = new ArrayList<GameState>();
	private static int currentState = -1;
	
	public static final int getCurrentGameState(){
		return currentState;
	}
	
	private final void initMagnet(){
		display = new Window(title, (int)size.x, (int)size.y, bufferCount);
		Input.init(display.getCanvas());
	}
	
	private static final void render(){
		getGraphics().setColor(Color.BLACK);
		getGraphics().fillRect(0, 0, (int)size.x, (int)size.y);
		if(!(currentState > gameStates.size()) && !(currentState < 0)){
			if(gameStates.get(currentState) != null)gameStates.get(currentState).render();
		}
		display.renderToScreen();
	}
	
	public static final void update(){
		Input.poll();
		
		if(!(currentState > gameStates.size()) && !(currentState < 0)){
			if(gameStates.get(currentState) != null)gameStates.get(currentState).update();
		}
		display.setTitle(title + "   |   " + actualFps + " fps    " + actualUps + " ups");
		EventManager.processEventQueue();
	}
	
	public static final void reloadCurrentGameState(){
		EventManager.clear();
		gameStates.get(currentState).init();
	}
	
	public static final void setCurrentGameState(int i){
		if(i == currentState)return;
		
		//start the new state
		if(i > gameStates.size() || i < 0)return;
		currentState = i;
		EventManager.clear();
		gameStates.get(currentState).init();
	}
	
	public static final void addGameState(GameState state){
		gameStates.add(state);
	}
	
	public static final void removeGameState(GameState state){
		gameStates.remove(state);
	}
	
	public final synchronized void start(int width, int height, int bufferCount){
		size = new Vector2f(width, height);
		this.bufferCount = bufferCount;
		running = true;
		thread = new Thread(this, "MagnetEngine_Main");
		System.out.println("MagnetEngine Main Thread has been STARTED!");
		thread.start();
	}
	
	public final synchronized void stop(){
		running = false;
		
		System.out.println("MagnetEngine Main Thread has been STOPPED!");
		System.exit(0);
	}
	
	public final void run(){
		initMagnet();
		init();
		
		int frames = 0;
		int updates = 0;
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0;
		
		running = true;
		
		while(running){
			
			//call the update method
			long now = System.nanoTime();
			double ns = 1000000000.0 / preferredUps;
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				update();
				updates++;
				delta--;
				
			}
			
			//call the render method 
			render();
			frames++;
			
			//refresh the actualUps and actualFps once every second
			if(System.currentTimeMillis() - timer > 1000){
				timer+=1000;
				actualUps = updates;
				actualFps = frames;
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	/**
	 * {@code public final void setPreferredUps(int ups)}
	 * 
	 * <p>
	 * 
	 * Sets the preferred number of calling the update method per second.
	 * The default value is 60.
	 * 
	 * @param ups
	 */
	public static final void setPreferredUps(int ups){
		preferredUps = ups;
	}
	
	public static final void setSize(int width, int height){
		size.x = width;
		size.y = height;
		//set size missing
	}
	
	public static final void setDisplayTitle(String displayTitle){
		title = displayTitle;
		display.setTitle(displayTitle);
	}
	
	public static final Graphics2D getGraphics(){
		return display.getGraphics();
	}
	
	public static final int getDisplayWidth(){
		return (int)size.x;
	}
	
	public static final int getDisplayHeight(){
		return (int)size.y;
	}
	
	public static final String getDisplayTitle(){
		return title;
	}

}
