package Magnet.GameLogic.Actors;

public class IDGenerator {
	
	private IDGenerator(){
	}
	
	static int counter = 100;
	
	public static int generateID(){
		counter++;
		return counter;
	}

}
