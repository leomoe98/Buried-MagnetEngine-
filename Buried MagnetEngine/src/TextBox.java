import java.awt.Color;
import java.awt.Font;

import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.Renderable;


public class TextBox extends Actor implements Renderable{

	private String text;
	private Font font;
	
	public TextBox(float x, float y, String text) {
		super(x, y);
		this.text = text;
		font = new Font("Helvetica", Font.BOLD, 32);
	}

	@Override
	public void render(int offsetX, int offsetY) {
		Main.getGraphics().setColor(Color.WHITE);
		Main.getGraphics().setFont(font);
		Main.getGraphics().drawString(text, getX() - offsetX, getY() - offsetY);
	}

	@Override
	public void init() {
		
	}

	@Override
	public boolean isGravityEnabled() {
		return false;
	}

}
