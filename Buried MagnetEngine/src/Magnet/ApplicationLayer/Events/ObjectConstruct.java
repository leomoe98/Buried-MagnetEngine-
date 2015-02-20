package Magnet.ApplicationLayer.Events;

import java.io.Serializable;

public class ObjectConstruct implements Serializable{
	
	public static final int LOCAL_TILEMAPRENDERER = 31;
	
	private Class<?> objectClass;
	private Object[] objectParams;
	
	public ObjectConstruct(Class<?> objectClass, Object[] objectParams){
		this.objectClass = objectClass;
		this.objectParams = new Object[objectParams.length];
		for(int i = 0; i < objectParams.length; i++){
			this.objectParams[i] = objectParams[i];
		}
	}
	
	public Class<?> getObjectClass(){
		return objectClass;
	}
	
	public Object[] getObjectParams(){
		return objectParams;
	}

}
