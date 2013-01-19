package com.snowneedle.placear;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceDetail {
		
	private HashMap<Integer, String> dayMap;
	private HashMap<String, HashMap<String, Timestamp>> storeHours; // {Sunday: {open: t1, close: t2}}
	private String telephoneNumber;
	
	public PlaceDetail(JSONObject result) {
		dayMap.put(0, "Sunday");
		dayMap.put(1, "Monday");
		dayMap.put(2, "Tuesday");
		dayMap.put(3, "Wednesday");
		dayMap.put(4, "Thursday");
		dayMap.put(5, "Friday");
		dayMap.put(6, "Saturday");
		try {
			telephoneNumber = result.getString("international_phone_number");
			JSONObject openingHours = result.getJSONObject("opening_hours");
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
			JSONArray addressComps = result.getJSONArray("address_components");
			
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
	
	public String getPhoneNumber() {
		return telephoneNumber;
	}
	
}