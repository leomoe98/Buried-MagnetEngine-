package Magnet.GameView;

public abstract class GameView{
	
	private String name;
	
	public GameView(String name){
		this.name = name;
	}
	
	public abstract void init();
	
	public final String getName(){
		return name;
	}
	
}
