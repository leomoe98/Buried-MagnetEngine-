import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Magnet.Magnet;

import Magnet.GameLogic.RemoteLogic;

public class Main extends Magnet{

	public static Main main;
	
	public static final int MENU_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int REMOTE_STATE = 2;
	
	public static String serverAddress = "localhost";
	public static int serverPort = 234;
	public static String clientName = "guest_" + (int)(Math.random()*100);
	
	private static boolean remoteLoaded = false;
	
	public Main(){
		
	}

	public static void main(String[] args) {
		main = new Main();
		main.start(1280, 1024, 2);
	}
	
	public static void testStuff(){
		
	}

	public void init() {
		testStuff();
		
		System.setProperty("sun.java2d.transaccel", "True");
		
		//System.setProperty("sun.java2d.opengl","True");
		System.setProperty("sun.java2d.trace", "timestamp,log,count");
		
		System.setProperty("sun.java2d.d3d", "True");
	    System.setProperty("sun.java2d.ddforcevram", "True");
		
		MenuLogic menuLogic = new MenuLogic();
		MenuView menuView = new MenuView("MenuView");
		MenuState menuState = new MenuState(menuLogic);
		menuState.addGameView(menuView);
		addGameState(menuState);
		
		PlayLogic playLogic = new PlayLogic("/Level1.png");
		PlayerView playerView = new PlayerView("PlayerView");
		PlayState playState = new PlayState(playLogic);
		playState.addGameView(playerView);
		addGameState(playState);
		
		setCurrentGameState(MENU_STATE);
	}
	
	public static void initRemoteState(){
		Thread thread = new Thread(){
			public void run(){
				JFrame frame = new JFrame("ClientConfig - Base");
				serverAddress = (String) JOptionPane.showInputDialog(frame, "Enter the Server IP:", "Client Configuration", JOptionPane.QUESTION_MESSAGE, null, null,serverAddress);
				serverPort = Integer.parseInt((String) JOptionPane.showInputDialog(frame, "Enter the Server Port:", "Client Configuration", JOptionPane.QUESTION_MESSAGE, null, null, serverPort));
				clientName = (String) JOptionPane.showInputDialog(frame, "Enter the your nickname:", "Client Configuration", JOptionPane.QUESTION_MESSAGE, null, null, clientName);
				
				RemoteLogic remoteLogic = new RemoteLogic(clientName, serverAddress, serverPort);
				PlayerView playerView = new PlayerView("PlayerView");
				PlayState remoteState = new PlayState(remoteLogic);
				remoteState.addGameView(playerView);
				addGameState(remoteState);
				remoteLoaded = true;
			}
		};
		thread.start();
	}
	
	public static final boolean isRemoteLoaded(){
		return remoteLoaded;
	}
	
}
