package Magnet.GameLogic.Maps;

import java.awt.image.BufferedImage;

import Magnet.ApplicationLayer.Utils.ResourceUtils;
import Magnet.ApplicationLayer.Utils.TileMapUtils;
import Magnet.GameLogic.Actors.Actor;
import Magnet.GameLogic.Actors.Collideable;
import Magnet.GameLogic.Math.Vector2f;

public class TileMap {
	
	private String path;
	String name;
	private int tileSize;
	private Vector2f size;
	
	private boolean collisionMap[][];
	
	public static final Vector2f NORTH = new Vector2f(0, -1);
	public static final Vector2f NORTH_EAST = new Vector2f(1, -1);
	public static final Vector2f EAST = new Vector2f(1, 0);
	public static final Vector2f SOUTH_EAST = new Vector2f(1, 1);
	public static final Vector2f SOUTH = new Vector2f(0, 1);
	public static final Vector2f SOUTH_WEST = new Vector2f(-1, 1);
	public static final Vector2f WEST = new Vector2f(-1, 0);
	public static final Vector2f NORTH_WEST = new Vector2f(-1, -1);
	
	public TileMap(String path, boolean fromImage, int tileSize){
		this.path = path;
		if(!fromImage){
			name = TileMapUtils.loadName(path);
			this.tileSize = TileMapUtils.loadTileSize(path);
			size = TileMapUtils.loadSize(path);
			collisionMap = TileMapUtils.loadCollisionMap(path);
		}else{
			name = path;
			this.tileSize = tileSize;
			BufferedImage image = ResourceUtils.loadBufferedImage(path, false);
			size = new Vector2f(image.getWidth(), image.getHeight());
			int[][] map = TileMapUtils.loadMapFromImage(image);
			collisionMap = new boolean[(int)size.x][(int)size.y];
			for(int x = 0; x < size.x; x++){
				for(int y = 0; y < size.y; y++){
					if(map[x][y] == TileMapUtils.BLOCK || map[x][y] == TileMapUtils.BLOCK_2)collisionMap[x][y] = true;
					else collisionMap[x][y] = false;
					
				}
			}
		}
		
	}
	
	public final boolean isSolid(int x, int y){
		if(x < 0 || y < 0)return true;
		if(x >= size.x * tileSize || y >= size.y * tileSize)return true;
		return collisionMap[(int)(x / tileSize)][(int)(y / tileSize)];
	}
	
	public static final boolean intersects(Actor actor, Actor actor2){
		if(!Collideable.class.isInstance(actor))return false;
		if(!Collideable.class.isInstance(actor2))return false;
		
		Vector2f[] pins = new Vector2f[4];
		
		pins[0] = new Vector2f((actor.getPosition().x - ((Collideable) actor).getWidth()/2 + ((Collideable) actor).getShiftX()), 
				(int)(actor.getPosition().y + ((Collideable) actor).getHeight()/2 + ((Collideable) actor).getShiftY()));
		
		pins[1] = new Vector2f((actor.getPosition().x + ((Collideable) actor).getWidth()/2 + ((Collideable) actor).getShiftX()), 
				(int)(actor.getPosition().y - ((Collideable) actor).getHeight()/2 + ((Collideable) actor).getShiftY()));
		
		pins[2] = new Vector2f(pins[0].x, pins[1].y);
		pins[3] = new Vector2f(pins[1].x, pins[0].y);
		
		float x = (actor2.getPosition().x - ((Collideable) actor2).getWidth()/2 + ((Collideable) actor2).getShiftX());
		float y = (int)(actor2.getPosition().y - ((Collideable) actor2).getHeight()/2 + ((Collideable) actor2).getShiftY());
		float width = ((Collideable) actor2).getWidth();
		float height = ((Collideable) actor2).getHeight();
		
		for(int i = 0; i < pins.length; i++){
			if(pins[i].x > x && pins[i].y > y && pins[i].x < x + width && pins[i].y < y + height)return true;
		}
		return false;
	}
	
	public final boolean checkCollision(Actor actor, Vector2f newPosition){
		if(!Collideable.class.isInstance(actor))return false;
		
		Vector2f bottomLeft = new Vector2f((newPosition.x - ((Collideable) actor).getWidth()/2 + ((Collideable) actor).getShiftX()), 
				(int)(newPosition.y + ((Collideable) actor).getHeight()/2 + ((Collideable) actor).getShiftY()));
		
		Vector2f topRight = new Vector2f((newPosition.x + ((Collideable) actor).getWidth()/2 + ((Collideable) actor).getShiftX()), 
				(int)(newPosition.y - ((Collideable) actor).getHeight()/2 + ((Collideable) actor).getShiftY()));
		
		Vector2f topLeft = new Vector2f(bottomLeft.x, topRight.y);
		Vector2f bottomRight = new Vector2f(topRight.x, bottomLeft.y);
		
		if(isSolid((int)bottomLeft.x,(int)bottomLeft.y)){
			actor.setColliding(true);
			return true;
		}
		if(isSolid((int)topRight.x,(int)topRight.y)){
			actor.setColliding(true);
			return true;
		}
		if(isSolid((int)topLeft.x,(int)topLeft.y)){
			actor.setColliding(true);
			return true;
		}
		if(isSolid((int)bottomRight.x,(int)bottomRight.y)){
			actor.setColliding(true);
			return true;
		}
		
		return false;
	}
	
	public final boolean isSolid(Vector2f position){
		return isSolid((int)position.x, (int)position.y);
	}
	
	/*public final Vector2f possibleMovement(Actor actor, double distance, Vector2f direction){
		if(Collideable.class.isInstance(actor)){
			Vector2f possibleDistance = new Vector2f(0, 0);
			Vector2f nextPosition = possibleDistance;
			while(true){
				if(checkCollision(actor, new Vector2f((float)(actor.getX() + nextPosition.x), (float)(actor.getY() + nextPosition.y)))){
					return possibleDistance;
				}else if(Math.abs(nextPosition.hypot()) > distance){
					possibleDistance.add(applyDistance(distance - possibleDistance.hypot(), direction));
					return possibleDistance;
				}else{
					possibleDistance.add(direction);
				}
				nextPosition.add(direction);
			}
		}else{
			return applyDistance(distance, direction);
		}
	}*/
	
	public final double possibleMovementX(Actor actor, double distance){
		if(distance == 0)return 0;
		if(Collideable.class.isInstance(actor)){
			int direction = -1;
			if(distance > 0 )direction = 1;
			double possibleDistance = 0;
			double nextDistance = direction;
			while(true){
				if(checkCollision(actor, new Vector2f((float)(actor.getX() + nextDistance), actor.getY()))){
					return possibleDistance;
				}else if(Math.abs(nextDistance) >= Math.abs(distance)){
					return nextDistance;
				}else{
					possibleDistance = nextDistance;
					nextDistance += direction;
				}
			}
		}else{
			return distance;
		}
	}
	
	public final double possibleMovementY(Actor actor, double distance){
		if(distance == 0)return 0;
		if(Collideable.class.isInstance(actor)){
			int direction = -1;
			if(distance > 0 )direction = 1;
			double possibleDistance = 0;
			double nextDistance = direction;
			while(true){
				if(checkCollision(actor, new Vector2f(actor.getX(), (float)(actor.getY() + nextDistance)))){
					return possibleDistance;
				}else if(Math.abs(nextDistance) >= Math.abs(distance)){
					return nextDistance;
				}else{
					possibleDistance = nextDistance;
					nextDistance += direction;
				}
			}
		}else{
			return distance;
		}
	}
	
	private final Vector2f applyDistance(double distance, Vector2f direction){
		if(direction == NORTH_EAST || direction == SOUTH_EAST || direction == SOUTH_WEST || direction == NORTH_WEST){
			return new Vector2f((float)(direction.x * Math.sqrt(distance*distance/2.0)), (float)(direction.y * Math.sqrt(distance*distance/2.0)));
		}
		if(direction == NORTH|| direction == EAST || direction == SOUTH || direction == WEST){
			return new Vector2f((float)(direction.x * distance), (float)(direction.y * distance));
		}
		return null;
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
		return size;
	}

}
