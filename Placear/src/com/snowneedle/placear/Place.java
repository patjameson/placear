package com.snowneedle.placear;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

/*
 * This file is the essence of java
 */

public class Place {
//	private Double _latitude;
//	private Double _longitude;
	private Location _location;
	private String _address;
	private String _name;
	private ArrayList<String> _types;
	
	public Place(JSONObject data){
		try {
			JSONObject geometry = data.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
//			_latitude = location.getDouble("lat");
//			_longitude = location.getDouble("lng");
			_location = new Location("");
			_location.setLatitude(location.getDouble("lat"));
			_location.setLongitude(location.getDouble("lng"));
//			_address = data.getString("formatted_address");
			_address = "address parsing not working";
			_name = data.getString("name");
			JSONArray typesData = data.getJSONArray("types");
			_types = new ArrayList<String>();
			for(int i=0; i<typesData.length(); i++){
				String type = (String) typesData.get(i);
				_types.add(type);
			}
			
			Log.w("debug", _types.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public Location getLocation(){ return _location; }
//	public Double getLatitude(){ return _latitude; }
//	public Double getLongitude(){ return _longitude; }
	public String getAddress(){ return _address; }
	public String getName(){ return _name; }
	public ArrayList<String> getTypes(){ return _types; }
}