import java.awt.event.KeyEvent;

import Magnet.ApplicationLayer.Input;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameView.GameView;
import Magnet.GameView.Graphics.Quad;
import Magnet.GameView.Graphics.Texture;
import Magnet.GameView.Renderers.TileMapRenderer;

public class EditorView extends GameView implements Renderable{
	
	private boolean colEditing = false;
	
	private int speed = 4;
	private int currentTile = 0;
	private int tileScroll = 0;
	private TileMapRenderer tex, col;
	private Quad[] tileset;
	private Texture[] tileTex;
	private Quad selected;

	public EditorView(String name) {
		super(name);
	}
	
	@Override
	public void init() {
		tileTex = Texture.loadTileset("/Tileset.png", 256, false);
		tex = new TileMapRenderer(TileMapRenderer.ORTHOGONAL_RENDERER, null, tileTex , 20, 0.5f, "/newLevel.txt", false);
		col = new TileMapRenderer(TileMapRenderer.ORTHOGONAL_RENDERER, null, Texture.loadTileset("/CollisionSet.png", 256, true), 20, 0.5f, "/newLevel.txt", false);
		tileset = new Quad[tileTex.length];
		for(int i = 0; i < tileTex.length; i++){
			tileset[i] = new Quad(1001, i * 128, 0.5f, tileTex[i]);
		}
		selected = new Quad(1001, 128, 0.5f, Texture.loadTileset("/CollisionSet.png", 256, true)[0]);
		
	}

	@Override
	public void render(int offsetX, int offsetY) {
		if(Input.isKeyDown(KeyEvent.VK_1))colEditing = true;
		if(Input.isKeyDown(KeyEvent.VK_2))colEditing = false;
		///////////////         -TILEMAP-              /////////////////
		if(Input.getMouseX() <= 1000){
			if(Input.isKeyDown(KeyEvent.VK_UP)){
				tex.moveCamera(0, -speed);
				col.moveCamera(0, -speed);
			}
			if(Input.isKeyDown(KeyEvent.VK_DOWN)){
				tex.moveCamera(0, speed);
				col.moveCamera(0, speed);
			}
			if(Input.isKeyDown(KeyEvent.VK_LEFT)){
				tex.moveCamera(-speed, 0);
				col.moveCamera(-speed, 0);
			}
			if(Input.isKeyDown(KeyEvent.VK_RIGHT)){
				tex.moveCamera(speed, 0);
				col.moveCamera(speed, 0);
			}
			
			if(Input.isLeftMouseButtonDown()){
				if(colEditing){
					int x = (int) ((Input.getMouseX() + col.getCameraPos().x) / col.getTileSize());
					int y = (int) ((Input.getMouseY()  + col.getCameraPos().y) / col.getTileSize());

					if(x >= 0 || x < col.getSize().x){
						if(y >= 0 || y < col.getSize().y){
							col.setMapValue(x, y, 0);
							col.createMap();
						}
					}
				}else{
					int x = (int) ((Input.getMouseX() + tex.getCameraPos().x) / tex.getTileSize());
					int y = (int) ((Input.getMouseY()  + tex.getCameraPos().y) / tex.getTileSize());

					if(x >= 0 || x < tex.getSize().x){
						if(y >= 0 || y < tex.getSize().y){
							tex.setMapValue(x, y, currentTile);
							tex.createMap();
						}
					}
				}
			}
			
			if(Input.isRightMouseButtonDown()){
				if(colEditing){
					int x = (int) ((Input.getMouseX() + col.getCameraPos().x) / col.getTileSize());
					int y = (int) ((Input.getMouseY()  + col.getCameraPos().y) / col.getTileSize());
					
					if(x >= 0 || x < col.getSize().x){
						if(y >= 0 || y < col.getSize().y){
							col.setMapValue(x, y, 1);
							col.createMap();
						}
					}
				}
			}
		}else{
			///////////////         -TILESET-              /////////////////
			if(Input.isKeyDown(KeyEvent.VK_UP)){
				tileScroll-=speed*3;
			}
			if(Input.isKeyDown(KeyEvent.VK_DOWN)){
				tileScroll+=speed*3;
			}
			
			
			if(Input.isLeftMouseButtonDown()){
					int y = (int) ((Input.getMouseY() + tileScroll) / col.getTileSize());
						if(y >= 0 || y < tileset.length * 128){
							currentTile = y;
					}
			}
		}
		tex.render();
		if(colEditing){
			col.render();
		}
		for(int i = 0; i < tileset.length; i++){
			tileset[i].setPosition(1001, i * 128 - tileScroll);
			if(i * 128 - tileScroll < Main.getDisplayHeight() || i * 128 - tileScroll > 0)tileset[i].render();
		}
		selected.setPosition(1001, currentTile * 128 - tileScroll);
		selected.render();
		
	}

}
