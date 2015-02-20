import java.awt.image.BufferedImage;

import Magnet.ApplicationLayer.Utils.ResourceUtils;
import Magnet.GameView.Graphics.Texture;


public class ResourceBuffer {
	
	public static BufferedImage levelBg;
	
	public static Texture[] playerSprites;
	public static Texture[] playerWhite;
	
	public static Texture[] tileset;

	private static Thread loadGame;
	
	private static volatile boolean loaded = false;
	
	public static final void loadDefault(){
		playerSprites = Texture.loadTileset("/PlayerSprites.png", 64, false);
		playerWhite = Texture.loadTileset("/PlayerWhite.png", 64, false);
	}
	
	public static final void loadGame_thread(){
		loaded = false;
		loadGame = new Thread(){
			public void run(){
				levelBg = ResourceUtils.loadBufferedImage("/caveBG.jpg", false);
				tileset = Texture.loadTileset("/Tileset.png", 64, false);
				loaded = true;
			}
		};
		loadGame.start();
	}
	
	public static final boolean isLoaded(){
		return loaded;
	}
}
