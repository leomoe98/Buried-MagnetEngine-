package Magnet.ApplicationLayer.Network;

public class ClientData {
	
	private String clientName;
	private String address;
	private int port;
	
	public final int ID;
	
	public ClientData(String clientName, String address, int port){
		this.clientName = clientName;
		this.address = address;
		this.port = port;
		this.ID = Server.getID(address, port);
	}
	
	public final String getClientName(){
		return clientName;
	}
	
	public final String getAddress(){
		return address;
	}
	
	public final int getPort(){
		return port;
	}

}
