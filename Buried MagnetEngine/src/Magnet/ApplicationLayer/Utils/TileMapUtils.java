package Magnet.ApplicationLayer.Utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Magnet.GameLogic.Math.Vector2f;

public class TileMapUtils {
	
	public static final String NAME_MARK = "<name>";
	public static final String SIZE_MARK = "<size>";
	public static final String TILE_SIZE_MARK = "<tileSize>";
	public static final String TEXTURE_MAP_MARK = "<textureMap>";
	public static final String COLLISION_MAP_MARK = "<collisionMap>";
	
	public static final int BLOCK = 1;
	public static final int BLOCK_2 = 2;
	public static final int AIR = 0;
	
	public static final int EMPTY = 0;
	public static final int FULL = 1;
	public static final int TOP = 2;
	public static final int LEFT = 3;
	public static final int BOTTOM = 4;
	public static final int RIGHT = 5;
	public static final int TOPLEFT = 6;
	public static final int TOPRIGHT = 7;
	public static final int BOTTOMLEFT = 8;
	public static final int BOTTOMRIGHT = 9;
	public static final int TOPBOTH = 10;
	public static final int LEFTBOTH = 11;
	public static final int BOTTOMBOTH = 12;
	public static final int RIGHTBOTH = 13;
	public static final int LEFTRIGHT = 14;
	public static final int TOPBOTTOM = 15;
	public static final int ALL = 16;
	
	private TileMapUtils(){
	}
	
	public static final int[][] loadMapFromImage(BufferedImage image){
		int[][] map = new int[image.getWidth()][image.getHeight()];
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				if(image.getRGB(x, y) == 0xff000000)map[x][y] = BLOCK;
				else if(image.getRGB(x, y) == 0xff3f3f3f)map[x][y] = BLOCK_2;
				else if(image.getRGB(x, y) == 0xffffffff)map[x][y] = AIR;
				else map[x][y] = AIR;
			}
		}
		
		return map;
	}
	
	public static final Vector2f loadPlayerFromImage(BufferedImage image, int tileSize){
		Vector2f pos = new Vector2f(0, 0);
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				if(image.getRGB(x, y) == 0xff0000ff){
					pos = new Vector2f(x * tileSize, y * tileSize);
					return pos;
				}
			}
		}
		return pos;
	}
	
	public static final Vector2f loadExitFromImage(BufferedImage image, int tileSize){
		Vector2f pos = new Vector2f(0, 0);
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				if(image.getRGB(x, y) == 0xff00ff00){
					pos = new Vector2f(x * tileSize, y * tileSize);
					return pos;
				}
			}
		}
		return pos;
	}
	
	public static final ArrayList<Vector2f> loadSpikesFromImage(BufferedImage image, int tileSize){
		ArrayList<Vector2f> pos = new ArrayList<Vector2f>();
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				if(image.getRGB(x, y) == 0xff00ff00){
					pos.add(new Vector2f(x * tileSize, y * tileSize));
				}
			}
		}
		return pos;
	}
	
	public static final int[][] createDetailedMap(int[][] map, int width, int height, int tilesetWidth){
		int[][] newMap = new int[width][height];
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				newMap[x][y] = map[x][y];
			}
		}
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(map[x][y] == BLOCK){
					boolean top = false;
					boolean bottom = false;
					boolean left = false;
					boolean right = false;
					
					if(y == 0)top = true;
					else if(y > 0 && map[x][y-1] != AIR)top = true;
					
					if(y == height-1)bottom = true;
					else if(y < height-1 && map[x][y+1] != AIR)bottom = true;
					
					if(x == 0)left = true;
					else if(x > 0 && map[x-1][y] != AIR)left = true;
					
					if(x == width-1)right = true;
					else if(x < width-1 && map[x+1][y] != AIR)right = true;
					
					newMap[x][y] = placeCorrectTile(top, left, bottom, right);
				}else if(map[x][y] == BLOCK_2){
					boolean top = false;
					boolean bottom = false;
					boolean left = false;
					boolean right = false;
					
					if(y == 0)top = true;
					else if(y > 0 && map[x][y-1] != AIR)top = true;
					
					if(y == height-1)bottom = true;
					else if(y < height-1 && map[x][y+1] != AIR)bottom = true;
					
					if(x == 0)left = true;
					else if(x > 0 && map[x-1][y] != AIR)left = true;
					
					if(x == width-1)right = true;
					else if(x < width-1 && map[x+1][y] != AIR)right = true;
					
					newMap[x][y] = placeCorrectTile(top, left, bottom, right) + tilesetWidth;
				}else{
					newMap[x][y] = EMPTY;
				}
			}
		}
		
		return newMap;
	}
	
	private static int placeCorrectTile(boolean top, boolean left, boolean bottom, boolean right){
		if(top && left && bottom & right)return FULL;
		
		else if(top && left && bottom)return RIGHT;
		else if(top && left && right)return BOTTOM;
		else if(bottom && left && right)return TOP;
		else if(right && top && bottom)return LEFT;
		
		else if(top && left)return BOTTOMRIGHT;
		else if(bottom && left)return TOPRIGHT;
		else if(top && right)return BOTTOMLEFT;
		else if(bottom && right)return TOPLEFT;
		
		else if(bottom && top)return LEFTRIGHT;
		else if(left && right)return TOPBOTTOM;
		
		else if(bottom)return TOPBOTH;
		else if(left)return RIGHTBOTH;
		else if(top)return BOTTOMBOTH;
		else if(right)return LEFTBOTH;
		
		else return ALL;
	}
	
	public static final int[][] loadTextureMap(String path) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(TileMapUtils.class.getResourceAsStream(path)));
			
			Vector2f size = loadSize(path);
			
			if(!scrollToMark(br, TEXTURE_MAP_MARK)){
				System.err.println("In " + path + " the mark " + TEXTURE_MAP_MARK + " is missing!");
				return null;
			}

			int textureMap[][] = new int[(int)size.x][(int)size.y];
			
			String delims = "\\s+";
			
			for (int y = 0; y < size.y; y++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				
				for (int x = 0; x < size.x; x++) {
					textureMap[x][y] = Integer.parseInt(tokens[x]);
				}
			}
			br.close();
			return textureMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final boolean[][] loadCollisionMap(String path) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(TileMapUtils.class.getResourceAsStream(path)));
			
			Vector2f size = loadSize(path);
			
			if(!scrollToMark(br, COLLISION_MAP_MARK)){
				System.err.println("In " + path + " the mark " + COLLISION_MAP_MARK + " is missing!");
				return null;
			}

			boolean collisionMap[][] = new boolean[(int)size.x][(int)size.y];
			
			String delims = "\\s+";
			
			for (int y = 0; y < size.y; y++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				
				for (int x = 0; x < size.x; x++) {
					if(Integer.parseInt(tokens[x]) == 0){
						collisionMap[x][y] = false;
					}else{
						collisionMap[x][y] = true;
					}
				}
			}
			br.close();
			return collisionMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final String loadName(String path){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(TileMapUtils.class.getResourceAsStream(path)));
			
			if(!scrollToMark(br, NAME_MARK)){
				System.err.println("In " + path + " the mark " + NAME_MARK + " is missing!");
				return null;
			}
			
			String name = br.readLine();
			
			br.close();
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final int loadTileSize(String path){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(TileMapUtils.class.getResourceAsStream(path)));
			
			if(!scrollToMark(br, TILE_SIZE_MARK)){
				System.err.println("In " + path + " the mark " + TILE_SIZE_MARK + " is missing!");
				return 0;
			}
			
			int tileSize = Integer.parseInt(br.readLine());
			
			br.close();
			return tileSize;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static final Vector2f loadSize(String path){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(TileMapUtils.class.getResourceAsStream(path)));
			
			if(!scrollToMark(br, SIZE_MARK)){
				System.err.println("In " + path + " the mark " + SIZE_MARK + " is missing!");
				return null;
			}
			
			Vector2f size = new Vector2f(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));
			
			br.close();
			return size;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final String loadMark(String path, String mark){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(TileMapUtils.class.getResourceAsStream(path)));
			
			if(!scrollToMark(br, mark)){
				System.err.println("In " + path + " the mark " + mark + " is missing!");
				return null;
			}
			
			String line = br.readLine();
			
			br.close();
			return line;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final boolean scrollToMark(BufferedReader br, String mark){
		String line;
		
		try {
			while((line = br.readLine()) != null){	
				if(line.contains(mark)){
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
