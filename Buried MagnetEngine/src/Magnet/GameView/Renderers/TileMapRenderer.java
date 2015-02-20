package Magnet.GameView.Renderers;

import java.util.ArrayList;

import Magnet.ApplicationLayer.Utils.TileMapUtils;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.ActorManager;
import Magnet.GameLogic.Actors.Renderable;
import Magnet.GameLogic.Math.Vector2f;
import Magnet.GameView.Graphics.Texture;
import Magnet.GameView.Graphics.Quad;
import Magnet.Magnet;

public class TileMapRenderer {
	
	public static final int ORTHOGONAL_RENDERER = 1;
	public static final int ISOMETRIC_RENDERER = 2;
	
	private ActorManager actorManager;
	
	private String name;
	private int tileSize;
	private int cameraPosX = 0, cameraPosY = 0;
	private Vector2f mapSize;
	
	private float tileScale;
	private String path;
	private int textureMap[][];
	private Texture[] tileset;
	private int type;
	
	private Quad[][] tilemap;
	
	public final void setMapValue(int x, int y, int value){
		textureMap[x][y] = value;
	}
	
	public final int[][] getMap(){
		return textureMap;
	}

	public TileMapRenderer(int type, ActorManager actorManager, Texture[] tileset, int tilesetWidth, float tileScale, String path, boolean createDetailedMap){
		this.type = type;
		this.actorManager = actorManager;
		this.tileset = tileset;
		this.tileScale = tileScale;
		this.path = path;
		name = TileMapUtils.loadName(path);
		if(!createDetailedMap){
			textureMap = TileMapUtils.loadTextureMap(path);
		}else{
			Vector2f size = TileMapUtils.loadSize(path);
			int[][] buffer = new int[(int) size.x][(int) size.y];
			buffer = TileMapUtils.loadTextureMap(path);
			
			for(int x = 0; x < size.x; x++){
				for(int y = 0; y < size.y; y++){
					if(buffer[x][y] == 1)buffer[x][y] = TileMapUtils.BLOCK;
					else if(buffer[x][y] == 2)buffer[x][y] = TileMapUtils.BLOCK_2;
					else buffer[x][y] = TileMapUtils.AIR;
				}
			}
			textureMap = TileMapUtils.createDetailedMap(buffer, (int)size.x, (int)size.y, tilesetWidth);
		}
		mapSize = TileMapUtils.loadSize(path);
		tileSize = (int)(tileset[0].getWidth() * tileScale);
		
		createMap();
	}
	
	public void createMap(){
		Quad[][] map = new Quad[(int) mapSize.x][(int) mapSize.y];
		for(int x = 0; x < mapSize.x; x++){
			for(int y = 0; y < mapSize.y; y++){
				if(textureMap[x][y] == -1)continue;
				map[x][y] = new Quad(0f, 0f, tileScale, tileset[textureMap[x][y]]);
			}
		}
		tilemap = map;
	}

	public final void render(){
		if(type == ORTHOGONAL_RENDERER){
			renderOrthogonal();
		}else if(type == ISOMETRIC_RENDERER){
			renderIsometric();
		}
		if(actorManager != null)renderActors();
	}
	
	private final void renderOrthogonal(){
		int offsetX = cameraPosX / tileSize;
		int offsetY = cameraPosY / tileSize;
		
		int tilesOnScreenX = Magnet.getDisplayWidth() / tileSize+3;
		int tilesOnScreenY = Magnet.getDisplayHeight() / tileSize+3;
		
		for(int x = offsetX; x < offsetX + tilesOnScreenX; x++){
			for(int y = offsetY; y < offsetY + tilesOnScreenY; y++){
				if(x > mapSize.x - 1|| y > mapSize.y - 1)break;
				if(x < 0 || y < 0)continue;
				if(textureMap[x][y] == -1)continue;
				Vector2f pos = new Vector2f(x * tileSize - cameraPosX, y * tileSize - cameraPosY);
				tilemap[x][y].setPosition(pos.x, pos.y);
				tilemap[x][y].render();
			}
		}
	}

	private final void renderIsometric(){
		//////////////outdated, because of opengl viewport!!!!
		for(int x = 0; x < 4; x++){
			for(int y = 3; y >= 0; y--){
				if(x > mapSize.x - 1|| y > mapSize.y - 1)break;
				if(x < 0 || y < 0)continue;
				if(textureMap[x][y] == -1)continue;
				tilemap[x][y].render(); //(int)orthoToIso(x, y).x - cameraPosX, (int)orthoToIso(x, y).y - cameraPosY, null);
			}
		}
	}
	
	public final void renderActors(){
		ArrayList<Actor> actors = actorManager.getAllActors();
		for(int i = 0; i < actors.size(); i++){
			if(Renderable.class.isInstance(actors.get(i)))((Renderable) actors.get(i)).render(cameraPosX, cameraPosY);
		}
	}
	
	public final void cameraTrackActor(int actorID, float damper, int offsetX, int offsetY){
		Vector2f target = actorManager.getActor(actorID).getPosition();
		float targetX = (float) (target.x - offsetX - Magnet.getDisplayWidth()/2.0);
		float targetY = (float) (target.y - offsetY - Magnet.getDisplayHeight()/2.0);
		float camMoveX = (targetX - cameraPosX)*damper;
		float camMoveY = (targetY - cameraPosY)*damper;
		moveCamera(new Vector2f(camMoveX, camMoveY));
	}
	
	public final void moveCamera(int x, int y){
		cameraPosX += x;
		cameraPosY += y;
	}
	
	public final void moveCamera(Vector2f vector){
		cameraPosX += vector.x;
		cameraPosY += vector.y;
	}
	
	public final void setCameraPosition(int x, int y){
		cameraPosX = x;
		cameraPosY = y;
	}
	
	public final void setCameraPosition(Vector2f position){
		cameraPosX = (int) position.x;
		cameraPosY = (int) position.y;
	}
	
	public static final Vector2f viewportToPixel(Vector2f vector){
		float x = (int)(Magnet.getDisplayWidth() * (vector.x + 1) / 2);    
		float y = (int)(Magnet.getDisplayHeight() * ((vector.y *(-1)) + 1) / 2);
		
		return new Vector2f(x, y);
	}
	
	public static final Vector2f pixelToViewport(Vector2f vector){
		float x = vector.x * 2 / Magnet.getDisplayWidth() - 1;    
		float y = (2 * vector.y / Magnet.getDisplayHeight() - 1)*(-1);
		
		return new Vector2f(x, y);
	}
	
	public final Vector2f isoToOrtho(Vector2f vector){
		int x = (int) ((vector.x / tileSize / 2 + vector.y / tileSize / 2) / 2);
		int y = (int) (vector.y / tileSize / 2 - (vector.x / tileSize / 2) / 2);
		return new Vector2f(x, y);
	}
	
	public final Vector2f orthoToIso(Vector2f vector){
		int x = (int) ((vector.x - vector.y) * tileSize / 2);
		int y = (int) ((vector.x + vector.y) * tileSize / 2);
		return new Vector2f(x, y);
	}
	
	public final Vector2f isoToOrtho(int x, int y){
		int nx = (int) ((x / tileSize / 2 + y / tileSize / 2) / 2);
		int ny = (int) (y / tileSize / 2 - (x / tileSize / 2) / 2);
		return new Vector2f(nx, ny);
	}
	
	public final Vector2f orthoToIso(int x, int y){
		int nx = (int) ((x - y) * tileSize / 2);
		int ny = (int) ((x + y) * tileSize / 2);
		return new Vector2f(nx, ny);
	}
	
	public final Vector2f getCameraPos(){
		return new Vector2f(cameraPosX, cameraPosY);
	}
	
	public final String getPath(){
		return path;
	}
	
	public final int getTileSize(){
		return tileSize;
	}
	
	public final String getName(){
		return name;
	}
	
	public final Vector2f getSize(){
		return mapSize;
	}
}
