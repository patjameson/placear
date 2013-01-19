package com.snowneedle.placear;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PlaceDetail {
		
	private HashMap<Integer, String> dayMap;
	private HashMap<String, HashMap<String, String>> storeHours; // {Sunday: {open: t1, close: t2}}
	private String telephoneNumber;
	
	public PlaceDetail(JSONObject result) {
		dayMap = new HashMap<Integer, String>();
		storeHours = new HashMap<String, HashMap<String, String>>();
		dayMap.put(0, "Sunday");
		dayMap.put(1, "Monday");
		dayMap.put(2, "Tuesday");
		dayMap.put(3, "Wednesday");
		dayMap.put(4, "Thursday");
		dayMap.put(5, "Friday");
		dayMap.put(6, "Saturday");
		try {
			if (result.has("international_phone_number")){
				telephoneNumber = result.getString("international_phone_number");
			}
			if (result.has("opening_hours")){
				JSONObject openingHours = result.getJSONObject("opening_hours");
				JSONArray periods = openingHours.getJSONArray("periods");
				JSONObject obj, close, open;
				String closeDay, openDay;
				String closeTime, openTime;
				String closeStamp, openStamp;
				for(int i = 0; i < periods.length(); i++) {
					obj = periods.getJSONObject(i);
					
					close = obj.getJSONObject("close");
					closeDay = dayMap.get(close.getInt("day"));
					closeTime = close.getString("time");
					
					int cTimeH = Integer.parseInt(closeTime.substring(0, 2));
					int cTimeM = Integer.parseInt(closeTime.substring(2, 4));
					closeStamp = createTimeString(cTimeH, cTimeM);
										
					open = obj.getJSONObject("open");
					openDay = dayMap.get(open.getInt("day"));
					openTime = open.getString("time");
					int oTimeH = Integer.parseInt(openTime.substring(0, 2));
					int oTimeM = Integer.parseInt(openTime.substring(2, 4));
					openStamp = createTimeString(oTimeH, oTimeM);
					
					HashMap<String, String> closeTimeMap = new HashMap<String, String>();
					closeTimeMap.put("close", closeStamp);
					if(storeHours.containsKey(closeDay)) {
						HashMap<String, String> temp = storeHours.get(closeDay);
						temp.put("close", closeStamp);
						storeHours.put(closeDay, temp);
					} else {
						storeHours.put(closeDay, closeTimeMap);
					}
					
					HashMap<String, String> openTimeMap = new HashMap<String, String>();
					openTimeMap.put("open", openStamp);
					if(storeHours.containsKey(openDay)) {
						HashMap<String, String> temp = storeHours.get(openDay);
						temp.put("open", openStamp);
						storeHours.put(openDay, temp);
					} else {
						storeHours.put(openDay, openTimeMap);
					}
					
				}
				Log.e("API", storeHours.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}
	
	public HashMap<String, String> getHoursForDay(String day) {
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
	
	private String createTimeString(int h, int m) {
		boolean am = true;
		String hour = "";
		if(h <= 12) {
			if(h == 0) {
				h = 12;
			}
			hour = "" + h;
		} else {
			am = false;
			hour = "" + (h - 12);
		}
		String min = "";
		if(m < 10) {
			min = "0" + m;
		}
		String ret = hour + ":" + min;
		if(am) return ret + "AM";
		else return ret + "PM";
	}
	
}