
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import Magnet.ApplicationLayer.Input;
import Magnet.ApplicationLayer.Utils.ResourceUtils;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Actors.Updatable;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.GameView;
import Magnet.GameView.Graphics.Button;
import Magnet.GameView.Graphics.Texture;

public class MenuView extends GameView implements Renderable, Updatable{
	
	private Button playSingle, playMulti;
	private BufferedImage bg;
	
	private Player anim;
	private Vector2f animPos;
	
	private boolean loading = false;
	private int nextGameState = 0;
	
	public MenuView(String name) {
		super(name);
	}

	@Override
	public void init() {
		ResourceBuffer.loadDefault();
		
		bg = ResourceUtils.loadBufferedImage("/menuBackground.png", false);
		
		playSingle = new Button(850, 350, 1, ResourceUtils.loadBufferedImage("/singleplayerButton.png", false)){
			@Override
			public void clicked() {
				if(!loading){
					initGameResources();
					nextGameState = Main.PLAY_STATE;
				}
			}
		};
		
		playMulti = new Button(880, 550, 1, ResourceUtils.loadBufferedImage("/multiplayerButton.png", false)){
			@Override
			public void clicked() {
				if(!loading){
					initGameResources();
					Main.initRemoteState();
					nextGameState = Main.REMOTE_STATE;
				}
			}
		};
		
		animPos = new Vector2f(1150, 900);
		anim = new Player(animPos.x, animPos.y, 8, null);
		anim.setPlayerSprites(ResourceBuffer.playerWhite);
		BufferedImage particle = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = (Graphics2D) particle.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, particle.getWidth(), particle.getHeight());
		g.dispose();
		
		anim.setParticleTexture(new Texture(particle, false));
		
	}
	
	private void initGameResources(){
		ResourceBuffer.loadGame_thread();
		loading = true;
	}

	@Override
	public void render(int offsetX, int offsetY) {
		Main.getGraphics().drawImage(bg, 0, 0, Main.getDisplayWidth(), Main.getDisplayHeight(), null);
		playSingle.render();
		playMulti.render();
		
		if(loading && ResourceBuffer.isLoaded()){
			anim.setVelocityX(0);
		}
		else if(loading){
			anim.setVelocityX(anim.getSpeed());
		}
		else{
			anim.setVelocityX(0);
		}
		anim.setVelocityY(0);
		anim.setPosition(animPos);
		anim.render(0, 0);
	}

	@Override
	public void update() {
		playSingle.update();
		playMulti.update();
		anim.update();
		//if(Input.isKeyDown(KeyEvent.VK_1))Main.setCurrentGameState(Main.EDITOR_STATE);
		if(loading && ResourceBuffer.isLoaded()){
			if(nextGameState == Main.REMOTE_STATE && Main.isRemoteLoaded())Main.setCurrentGameState(nextGameState);
			else if(nextGameState == Main.PLAY_STATE)Main.setCurrentGameState(nextGameState);
			
		}
	}

}
