import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Magnet.Magnet;
import Magnet.GameLogic.RemoteLogic;

public class Main extends Magnet {

	public static Main main;

	public static final int MENU_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int REMOTE_STATE = 2;

	public static String serverAddress = "localhost";
	public static int serverPort = 234;
	public static String clientName = "guest_" + (int) (Math.random() * 100);

	public static int loadedLevel = 1;
	private static boolean remoteLoaded = false;

	public Main() {

	}

	public static void main(String[] args) {
		main = new Main();
		main.start(1280, 1024, 2);
	}

	public void init() {
		System.setProperty("sun.java2d.transaccel", "True");

		// System.setProperty("sun.java2d.opengl","True");
		System.setProperty("sun.java2d.trace", "timestamp,log,count");

		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");

		MenuState menuState = new MenuState();
		addGameState(menuState);

		PlayState playState = new PlayState("/Level1.png");
		addGameState(playState);

		setCurrentGameState(MENU_STATE);
	}

	public static void loadLevel(int level) {
		((PlayState) gameStates.get(PLAY_STATE)).close();
		PlayState playState = new PlayState("/Level" + level + ".png");
		gameStates.remove(PLAY_STATE);
		gameStates.add(PLAY_STATE, playState);

		loadedLevel = level;
		reloadCurrentGameState();
	}

	public static void initRemoteState() {
		Thread thread = new Thread() {
			public void run() {
				JFrame frame = new JFrame("ClientConfig - Base");
				serverAddress = (String) JOptionPane.showInputDialog(frame, "Enter the Server IP:", "Client Configuration", JOptionPane.QUESTION_MESSAGE, null, null, serverAddress);
				serverPort = Integer.parseInt((String) JOptionPane.showInputDialog(frame, "Enter the Server Port:", "Client Configuration", JOptionPane.QUESTION_MESSAGE, null, null, serverPort));
				clientName = (String) JOptionPane.showInputDialog(frame, "Enter the your nickname:", "Client Configuration", JOptionPane.QUESTION_MESSAGE, null, null, clientName);
				JOptionPane.showMessageDialog(frame, "Sorry. Multiplayer is not up to date in this version...");
				System.exit(0);
				/*RemoteLogic remoteLogic = new RemoteLogic(clientName, serverAddress, serverPort);
				PlayerView playerView = new PlayerView("PlayerView", "/Level1.png");
				PlayState remoteState = new PlayState(remoteLogic);
				remoteState.addGameView(playerView);
				addGameState(remoteState);*/
				remoteLoaded = true;
			}
		};
		thread.start();
	}

	public static final boolean isRemoteLoaded() {
		return remoteLoaded;
	}

}
