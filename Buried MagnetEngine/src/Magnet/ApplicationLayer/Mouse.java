package Magnet.ApplicationLayer;

import java.awt.Canvas;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import Magnet.GameLogic.Math.Vector2f;

public class Mouse implements MouseListener{
	
	private Canvas display;
	private boolean leftButton, rightButton, middleButton;
	private Vector2f mousePos;
	
	public Mouse(Canvas display){
		this.display = display;
	}
	
	public void init(){
		mousePos = new Vector2f(0, 0);
		leftButton = false;
		rightButton = false;
		System.out.println("Mouse has been initialized!");
	}
	
	public final void poll(){
		Point pos = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(pos, display);
		mousePos.x = pos.x;
		mousePos.y = pos.y;
	}
	
	public final Vector2f getMousePos(){
		return mousePos;
	}
	
	public final int getMouseX(){
		return (int)mousePos.x;
	}
	
	public final int getMouseY(){
		return (int)mousePos.y;
	}
	
	public final boolean isLeftMouseButtonDown(){
		return leftButton;
	}
	
	public final boolean isRightMouseButtonDown(){
		return rightButton;
	}
	
	public final boolean isMiddleMouseButtonDown(){
		return middleButton;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e))leftButton = true;
		if(SwingUtilities.isRightMouseButton(e))rightButton = true;
		if(SwingUtilities.isMiddleMouseButton(e))middleButton = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e))leftButton = false;
		if(SwingUtilities.isRightMouseButton(e))rightButton = false;
		if(SwingUtilities.isMiddleMouseButton(e))middleButton = false;
	}

}

