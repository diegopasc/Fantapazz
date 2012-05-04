package it.fantapazz.connector.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectCache {
	
	private Map<Object, Map<String, ?> > cache;
	
	public ObjectCache() {
		cache = new HashMap<Object, Map<String, ?> >();
	}
	
	@SuppressWarnings("unchecked")
	public <T> void set(String ID, T object, Object context) {
		Map<String, T> map = (Map<String, T>) cache.get(context);
		if ( map == null ) {
			map = new HashMap<String, T>();
			cache.put(context, map);
		}
		map.put(ID, object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String ID, Object context) {
		Map<String, T> map = (Map<String, T>) cache.get(context);
		if ( map == null ) {
			return null;
		}
		return map.get(ID);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getObjectsForContext(String context) {
		Map<String, T> map = (Map<String, T>) cache.get(context);
		if ( map == null )
			return null;
		return map.values();
	}
	
}
