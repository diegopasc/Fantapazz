package it.fantapazz;

import org.json.JSONException;
import org.json.JSONObject;

public class MyJSONObject extends JSONObject {
	
	private JSONObject object;
	
	public MyJSONObject(JSONObject object) {
		super();
		this.object = object;
	}

	public Object get(String name) throws JSONException {
		if (!name.contains(".")) {
			return object.get(name);
		}
		String[] keys = name.split("\\.");
		if ( keys.length == 1 ) {
			return object.get(keys[0]);
		}
		else {
			JSONObject tmpObject = object;
			for ( int i = 0; i < keys.length - 1; i ++ ) {
				tmpObject = tmpObject.getJSONObject(keys[i]);
			}
			return tmpObject.get(keys[keys.length - 1]);
		}
	}

}
