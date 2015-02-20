package Magnet.GameView;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Window {
	
	private JFrame frame;
	private Canvas display;
	private Graphics2D graphicsToScreen, graphics;
	private BufferedImage image;
	
	private String title;
	private Dimension size;
	private int bufferCount;
	
	private BufferStrategy bs;
	
	public Window(String title, int width, int height, int bufferCount){
		this.title = title;
		this.bufferCount = bufferCount;
		size = new Dimension(width, height);
		
		createWindow();
	}
	
	private final void createWindow(){
		frame = new JFrame(title);
		frame.setMinimumSize(size);
		frame.setMaximumSize(size);
		frame.setPreferredSize(size);
		
		display = new Canvas();
		display.setMinimumSize(size);
		display.setMaximumSize(size);
		display.setPreferredSize(size);
		
		image = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) image.getGraphics();
		
		frame.add(display);
		//frame.setUndecorated(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		
		//GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		//gd.setFullScreenWindow(frame);
		
		
		frame.requestFocusInWindow();
		
	}
	
	public final void renderToScreen(){
		bs = display.getBufferStrategy();
		if(bs==null){
			display.createBufferStrategy(bufferCount);
			return;
		}
		graphicsToScreen = (Graphics2D) bs.getDrawGraphics();
		graphicsToScreen.drawImage(image, 0, 0, display.getWidth(), display.getHeight(), null);
		graphicsToScreen.dispose();
		bs.show();
		Toolkit.getDefaultToolkit().sync();
	}
	
	public Graphics2D getGraphics(){
		return graphics;
	}
	
	public int getWidth(){
		return (int)size.getWidth();
	}
	
	public int getHeight(){
		return (int)size.getHeight();
	}
	
	public String getTitle(){
		return title;
	}
	
	public Canvas getCanvas(){
		return display;
	}
	
	public void setTitle(String title){
		this.title = title;
		frame.setTitle(title);
	}
	
	public void setBufferCount(int bufferCount){
		this.bufferCount = bufferCount;
	}
	
	public void addMouseListener(MouseListener mouseListener){
		display.addMouseListener(mouseListener);
	}

}
