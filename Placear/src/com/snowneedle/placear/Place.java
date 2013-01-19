package com.snowneedle.placear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.util.Log;

/*
 * This file is the essence of java
 */

public class Place {

	private Location _location;
	private String _address;
	private String _name;
	private ArrayList<String> _types;
	
	public Place(JSONObject data){
		try {
			JSONObject geometry = data.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			String reference = data.getString("reference");
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
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public Location getLocation(){ return _location; }
	public String getAddress(){ return _address; }
	public String getName(){ return _name; }
	public ArrayList<String> getTypes(){ return _types; }
	
	private class PlaceHours {
		
		private HashMap<Integer, String> dayMap;
		private HashMap<String, HashMap<String, Timestamp>> storeHours; // {Sunday: {open: t1, close: t2}}
		
		public PlaceHours(JSONObject openingHours) {
			dayMap.put(0, "Sunday");
			dayMap.put(1, "Monday");
			dayMap.put(2, "Tuesday");
			dayMap.put(3, "Wednesday");
			dayMap.put(4, "Thursday");
			dayMap.put(5, "Friday");
			dayMap.put(6, "Saturday");
			try {
				JSONArray periods = openingHours.getJSONArray("periods");
				JSONObject obj, close, open;
				String closeDay, openDay;
				String closeTime, openTime;
				Timestamp closeStamp, openStamp;
				for(int i = 0; i < periods.length(); i++) {
					obj = periods.getJSONObject(i);
					
					close = obj.getJSONObject("close");
					closeDay = dayMap.get(close.getInt("day"));
					closeTime = close.getString("time");
					closeStamp = Timestamp.valueOf(new SimpleDateFormat("Hm").format(new Date()).concat(closeTime));
					
					open = obj.getJSONObject("open");
					openDay = dayMap.get(open.getInt("day"));
					openTime = open.getString("time");
					openStamp = Timestamp.valueOf(new SimpleDateFormat("Hm").format(new Date()).concat(openTime));
					
					HashMap<String, Timestamp> closeTimeMap = new HashMap<String, Timestamp>();
					closeTimeMap.put("close", closeStamp);
					storeHours.put(closeDay, closeTimeMap);
					
					HashMap<String, Timestamp> openTimeMap = new HashMap<String, Timestamp>();
					openTimeMap.put("open", openStamp);
					storeHours.put("open", openTimeMap);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		public HashMap<String, Timestamp> getHoursForDay(String day) {
			return storeHours.get(day);
		}
		
		public String convertIntToDay(Integer i) {
			if(i > 0 && i < 7) {
				return dayMap.get(i);
			} else {
				return "";
			}
		}
		
	}
	
	private class DetailAPI {
		
		private String gToken;
		private AndroidHttpClient gClient;
		
		public DetailAPI() {
			gToken = getGoogleAccessToken();
			gClient = AndroidHttpClient.newInstance("PlaceAR");
			
		}
		
		private String getGoogleAccessToken() {
			String token = "";
			InputStream in;
			try {
				in = getAssets().open("google_token");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				token = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Log.w("access token", token);
			
			return token;
		}
	}
	
}