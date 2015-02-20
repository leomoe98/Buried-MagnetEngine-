package Magnet.ApplicationLayer.Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Magnet.ApplicationLayer.Events.Event;
import Magnet.ApplicationLayer.Utils.Serializer;

public abstract class Client {

	public static final byte[] NEWCONNECTION = {0, 1};
	public static final byte[] DISCONNECT = {0, 2};
	public static final byte[] EVENT = {1, 1};
	public static final byte[] CUSTOM = {2, 1};
	
	private volatile boolean running = false;
	private volatile boolean received = false;
	
	private String name;
	private String serverAddress;
	private InetAddress ip;
	private int serverPort;

	private DatagramSocket socket;
	private Thread send, receive, thread;

	public Client(String name, String serverAddress, int serverPort) {
		this.name = name;
		this.serverPort = serverPort;
		this.serverAddress = serverAddress;
		if (!openConnection(serverAddress, serverPort)) {
			System.err.println("Connection failed!");
			System.exit(0);
		}
	}

	private final boolean openConnection(String address, int port) {
		try {
			socket = new DatagramSocket();
			if(address.equals("localhost")){
				this.ip = InetAddress.getLocalHost();
			}else{
				ip = InetAddress.getByName(address);
			}
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public final void handlePacket(DatagramPacket packet){
		byte[] raw = packet.getData();
		byte[] head = new byte[2];
		head[0] = raw[0];
		head[1] = raw[1];
		byte[] data = new byte[raw.length - head.length];
		
		for(int i = 0; i < data.length; i++){
			data[i] = raw[i + head.length];
		}
		if(head[0] == NEWCONNECTION[0] && head[1] == NEWCONNECTION[1]){
			
		}else if(head[0] == DISCONNECT[0] && head[1] == DISCONNECT[1]){
			
		}else if(head[0] == EVENT[0] && head[1] == EVENT[1]){
			Object o = null;
			try {
				o = Serializer.deserialize(data);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("receiving event! -> " + o.getClass().toString());
			
			handleEvent((Event) o);
		}else if(head[0] == CUSTOM[0] && head[1] == CUSTOM[1]){
			handleCustom(data);
		}
	}
	
	public abstract void handleEvent(Event e);
	public abstract void handleCustom(byte[] data);
	
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
	
	public final synchronized void start(){
		running = true;
		thread = new Thread("MagnetEngine_ClientThread"){
			@Override
			public void run() {
				
				while(running){
					handlePacket(receive());
				}
			}
		};
		thread.start();
		System.out.println("Client Thread has been STARTED!");
	}
	
	public final synchronized void stop(){
		running = false;
		try{
			thread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("Client Thread has been STOPPED!");
	}

	public final void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, serverPort);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public final void sendEvent(final Event e) throws IOException {
		System.out.println("sending event! -> " + e.getClass().toString());
		final byte[] raw = Serializer.serialize(e);
		final byte[] data = new byte[EVENT.length + raw.length];
		data[0] = EVENT[0];
		data[1] = EVENT[1];
		for(int i = EVENT.length; i < data.length; i++){
			data[i] = raw[i - EVENT.length];
		}
		
		send = new Thread("SendEvent") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, serverPort);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public final String getName(){
		return name;
	}
	
	public final String getServerAddress(){
		return serverAddress;
	}
	
	public final int getServerPort(){
		return serverPort;
	}

}

