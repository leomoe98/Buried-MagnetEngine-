import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Magnet.Magnet;

public class ServerMain extends Magnet{
	
	public static ServerMain serverMain;
	public static int port = 234;
	
	public static final int SERVER_STATE = 0;
	
	public ServerMain(){
		
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("ServerConfig - Base");
		port = Integer.parseInt((String) JOptionPane.showInputDialog(frame, "Enter the port:", "Server Configuration", JOptionPane.QUESTION_MESSAGE, null, null,"234"));
		serverMain = new ServerMain();
		serverMain.start(1366, 768, 2);
	}

	public void init() {
		System.setProperty("sun.java2d.transaccel", "True");
		
		//System.setProperty("sun.java2d.opengl","True");
		System.setProperty("sun.java2d.trace", "timestamp,log,count");
		
		System.setProperty("sun.java2d.d3d", "True");
	    System.setProperty("sun.java2d.ddforcevram", "True");
	    
	    ResourceBuffer.loadDefault();
		
		PlayLogic playLogic = new PlayLogic("/testLevel.txt");
		ServerView serverView = new ServerView(port);
		ServerState serverState = new ServerState(playLogic);
		serverState.addGameView(serverView);
		addGameState(serverState);
		
		setCurrentGameState(SERVER_STATE);
	}
	
}
