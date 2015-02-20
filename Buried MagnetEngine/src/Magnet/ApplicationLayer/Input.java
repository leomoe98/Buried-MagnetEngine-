package Magnet.ApplicationLayer;

import java.awt.Canvas;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import Magnet.GameLogic.Math.Vector2f;

public class Input{
	
	public static Mouse mouse;
	
	private static boolean[] keys;
	
	private Input(){
		
	}
	
	public static void init(Canvas display){
		keys = new boolean[256];
		for(int i = 0; i < keys.length; i++){
			keys[i] = false;
		}
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
		      @Override
		      public boolean dispatchKeyEvent(KeyEvent e) {
		        if(e.getID() == KeyEvent.KEY_PRESSED){
		        	keys[e.getKeyCode()] = true;
		        }
		        else if(e.getID() == KeyEvent.KEY_RELEASED){
		        	keys[e.getKeyCode()] = false;
		        }
		        return false;
		      }
		});
		System.out.println("Keyboard has been initialized!");
		
		mouse = new Mouse(display);
		mouse.init();
		
		display.addMouseListener(mouse);
	}
	
	public static final void poll(){
		mouse.poll();
	}
	
	public static final boolean isKeyDown(int key){
		if(key >= 0 && key < 256)return keys[key];
		 return false;
	}
	
	public static final Vector2f getMousePos(){
		return mouse.getMousePos();
	}
	
	public static final int getMouseX(){
		return mouse.getMouseX();
	}
	
	public static final int getMouseY(){
		return mouse.getMouseY();
	}
	
	public static final boolean isLeftMouseButtonDown(){
		return mouse.isLeftMouseButtonDown();
	}
	
	public static final boolean isRightMouseButtonDown(){
		return mouse.isRightMouseButtonDown();
	}
	
	public static final boolean isMiddleMouseButtonDown(){
		return mouse.isMiddleMouseButtonDown();
	}

}
