package com.snowneedle.placear;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

/*
 * This file is the essence of java
 */

public class Place {

	private Location _location;
	private String _address;
	private String _name;
	private ArrayList<String> _types;
	private boolean openNow;
	private double rating;
	private int priceLevel;
	private String reference;
	private PlaceDetail detail = null;
	
	public Place(){
		_location = new Location("");
		_location.setLatitude(Math.random()*100);
		_location.setLongitude(Math.random()*100);
		_address = "address";
		_name = "name";
		_types = new ArrayList<String>();
		_types.add("food");
	}
	
	public Place(JSONObject data){
		try {
//			Log.e("farts", data.toString());
			
			JSONObject geometry = data.getJSONObject("geometry");
			
			if(data.has("opening_hours")){
				JSONObject openingHours = data.getJSONObject("opening_hours");
				openNow = openingHours.getBoolean("open_now");
			}
			if(data.has("rating")){
				rating = data.getDouble("rating");
			}
			if(data.has("price_level")){
				priceLevel = data.getInt("price_level");
			}
			JSONObject location = geometry.getJSONObject("location");
			reference = data.getString("reference");
			_location = new Location("");
			_location.setLatitude(location.getDouble("lat"));
			_location.setLongitude(location.getDouble("lng"));
			_address = "address parsing not working";
			_name = data.getString("name");
			JSONArray typesData = data.getJSONArray("types");
			_types = new ArrayList<String>();
			for(int i=0; i<typesData.length(); i++){
				String type = (String) typesData.get(i);
				_types.add(type);
			}
			
			Log.e("self", toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void getDetail(){
		
	}
	
	public Location getLocation(){ return _location; }
	public String getAddress(){ return _address; }
	public String getName(){ return _name; }
	public ArrayList<String> getTypes(){ return _types; }
	public boolean isOpenNow() { return openNow; }
	public double getRating() { return rating; }
	public int getPriceLevel() { return priceLevel; }
	public String getReference() { return reference; }
	public void setDetail(PlaceDetail d) { detail = d; }
	
}