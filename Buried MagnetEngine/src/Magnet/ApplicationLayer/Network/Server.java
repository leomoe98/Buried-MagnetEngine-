package Magnet.ApplicationLayer.Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import Magnet.ApplicationLayer.Events.Event;
import Magnet.ApplicationLayer.Events.EventManager;
import Magnet.ApplicationLayer.Events.UniversalListener;
import Magnet.ApplicationLayer.Utils.Serializer;
import Magnet.GameView.GameView;

public abstract class Server extends GameView{
	
	public static final byte[] NEWCONNECTION = {0, 1};
	public static final byte[] DISCONNECT = {0, 2};
	public static final byte[] EVENT = {1, 1};
	public static final byte[] CUSTOM = {2, 1};
	
	private DatagramSocket socket;
	private Thread send, thread;
	private Thread receive;
	
	private UniversalListener ul;
	private volatile boolean running = false;
	private volatile boolean received = false;
	
	private String ip;
	private int port;
	
	private ArrayList<ClientData> clients;
	
	public Server(int port) {
		super("Server - Buried");
		this.port = port;
		
	}
	
	@Override
	public void init() {
		try {
			this.ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		if (!openConnection(port)) {
			System.err.println("Connection failed!");
			System.exit(0);
		}
		ul = new UniversalListener(){
			@Override
			public void universal(Event e) {
				try {
					sendEventToAll(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		};
		//EventManager.addListener(ul);
		clients = new ArrayList<ClientData>();
		start();
	}
	
	private final void handlePacket(DatagramPacket packet){
		byte[] raw = packet.getData();
		byte[] head = new byte[2];
		head[0] = raw[0];
		head[1] = raw[1];
		byte[] data = new byte[raw.length - head.length];
		
		for(int i = 0; i < data.length; i++){
			data[i] = raw[i + head.length];
		}
		
		if(head[0] == NEWCONNECTION[0] && head[1] == NEWCONNECTION[1]){
				boolean unregistered = true;
				int i = 0;
				while(unregistered){
					if(i > clients.size())break;
					//if(clients.get(i).getAddress() == packet.getAddress().getHostAddress()){
					//	unregistered = false;
					//	break;
					//}
					i++;
				}
				if(unregistered)clients.add(new ClientData(packet.getAddress().getHostName(), packet.getAddress().getHostAddress(), packet.getPort()));
		}
		
		ClientData client = null;
		
		for(int i = 0; i < clients.size(); i++){
			if(clients.get(i).ID == getID(packet.getAddress().getHostAddress(), packet.getPort())){
				client = clients.get(i);
			}
		}
		
		if(head[0] == DISCONNECT[0] && head[1] == DISCONNECT[1]){
			try {
				send(DISCONNECT, InetAddress.getByName(client.getAddress()), client.getPort());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			clients.remove(client);
		}else if(head[0] == EVENT[0] && head[1] == EVENT[1]){
			Event e = null;
			try {
				e = (Event) Serializer.deserialize(data);
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println("receiving event! -> " + e.getClass().toString());
			e.setSource(ul);
			EventManager.queueEvent(e);
		}else if(head[0] == CUSTOM[0] && head[1] == CUSTOM[1]){
			handleCustom(data, client);
		}
		
	}
	
	public final void send(final byte[] data, final InetAddress ip, final int clientPort) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, clientPort);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public final void sendEventToAll(final Event e) throws IOException {
		System.out.println("sending event to all! -> " + e.getClass().toString());
		final byte[] raw = Serializer.serialize(e);
		final byte[] data = new byte[EVENT.length + raw.length];
		data[0] = EVENT[0];
		data[1] = EVENT[1];
		for(int i = EVENT.length; i < data.length; i++){
			data[i] = raw[i - EVENT.length];
		}
		
		send = new Thread("SendEvent") {
			public void run() {
				InetAddress ip = null;
				for(int i = 0; i < clients.size(); i++){
					try {
						ip = InetAddress.getByName(clients.get(i).getAddress());
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
					
					DatagramPacket packet = new DatagramPacket(data, data.length, ip, clients.get(i).getPort());
					
					try {
						socket.send(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		send.start();
	}
	
	public abstract void handleCustom(byte[] data, ClientData client);
	
	public final synchronized void start(){
		running = true;
		thread = new Thread("MagnetEngine_ServerThread"){
			@Override
			public void run() {
				
				while(running){
					handlePacket(receive());
				}
			}
		};
		thread.start();
		System.out.println("Server Thread has been STARTED!");
	}
	
	public final synchronized void stop(){
		running = false;
		try{
			thread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("Server Thread has been STOPPED!");
	}

	private final boolean openConnection(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return true;
	}

	public final DatagramPacket receive() {
		byte[] data = new byte[1024];
		final DatagramPacket packet = new DatagramPacket(data, data.length);
		receive = new Thread("Receive") {
			public void run() {
				try {
					socket.receive(packet);
					received = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		receive.start();
		while(!received){
			try {
				thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		received = false;
		return packet;
	}
	
	public static final int getID(String address, int port){
		return ByteBuffer.wrap(address.getBytes()).getInt() + port;
	}
	
	public final String getIP(){
		return ip;
	}
	
	public final int getPort(){
		return port;
	}
	
	public final int getClientCount(){
		return clients.size();
	}

}

