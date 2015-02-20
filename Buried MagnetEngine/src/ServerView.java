import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Network.ClientData;
import Magnet.ApplicationLayer.Network.Server;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;

public class ServerView extends Server implements Renderable, Updatable{

	public ServerView(int port) {
		super(port);
	}

	@Override
	public void handleCustom(byte[] data, ClientData client) {
		
	}

	@Override
	public void render(int offsetX, int offsetY) {
		Graphics2D g = ServerMain.getGraphics();
		g.setColor(Color.WHITE);
		g.setFont(new Font("Consolas", Font.PLAIN, 25));
		g.drawString("Running on IP: " + getIP() + " with port: " + getPort(), 20, 30);
		g.drawString("Clients on Server: " + getClientCount(), 20, 60);
		g.drawString("Events processed per Update: " + EventManager.getEventCountPerProcess(), 20, 90);
	}

	@Override
	public void update() {
		
	}

}
