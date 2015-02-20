package Magnet.GameLogic;

import java.io.IOException;

import Magnet.ApplicationLayer.Events.Event;
import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Events.UniversalListener;
import Magnet.ApplicationLayer.Network.Client;

public class RemoteLogic extends GameLogic{
	
	private String name;
	private String serverAddress;
	private int serverPort;
	
	private Client client;
	
	public RemoteLogic(String name, String serverAddress, int serverPort){
		this.name = name;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	public final void disconnect(){
		client.send(Client.DISCONNECT);
	}

	@Override
	public void init() {
		final UniversalListener ul = new UniversalListener(){
			@Override
			public void universal(Event e) {
				try {
					client.sendEvent(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		
		client = new Client(name, serverAddress, serverPort){
			@Override
			public void handleEvent(Event e) {
				e.setSource(ul);
				EventManager.queueEvent(e);
			}

			@Override
			public void handleCustom(byte[] data) {
				
			}
			
		};
		
		//EventManager.addListener(ul);
		
		client.start();
		
		client.send(Client.NEWCONNECTION);
	}

	@Override
	public void update() {
		
	}

	
	
	

}
