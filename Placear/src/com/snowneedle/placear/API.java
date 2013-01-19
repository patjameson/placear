package com.snowneedle.placear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class API {
	
	private String _googleToken;
//	private AndroidHttpClient _facebookClient;
	private AndroidHttpClient _googleClient;
//	private URI _facebookRoot;
	
	private final String _googleRoot = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	
	public API(String googleToken){
		_googleToken = googleToken;
		_googleClient = AndroidHttpClient.newInstance("PlaceAR");
	}
	
	private URI createQueryString(String baseUrl, HashMap<String, String> params) {
		String ret = baseUrl;
		if(params.size() > 0) {
			ret += "?";
		}
		Log.v("API", "Building query.");
		Object[] keys = params.keySet().toArray();
		for(int i = 0; i < keys.length; i++) {
			String s = keys[i].toString();
			if(i == keys.length - 1) {
				ret += (s + "=" + params.get(s));
			} else {
				ret += (s + "=" + params.get(s) + "&");
			}
		}
		try {
			Log.v("API", "URI Created: " + ret);
			return new URI(ret);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private JSONObject createJSONFromResponse(HttpResponse resp) {
		String responseBody = "";
		JSONObject json = null;
		InputStream stream;
		try {
			stream = resp.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
			    builder.append(line + "\n");
			}
			responseBody = builder.toString();
			System.out.println(responseBody);
			json = new JSONObject(responseBody);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	private abstract class APIWorker extends Observable implements Runnable {
		
		abstract AndroidHttpClient getClient();
		abstract URI getQuery();
		abstract JSONObject processResponse(HttpResponse r);
		abstract Object processJSON(JSONObject j);

		@Override
		public void run() {
			URI query = getQuery();
			HttpGet request = new HttpGet(query);
			HttpResponse response;
			JSONObject json = null;
			Log.v("API", "Starting thread.");
			while(true) {
				try {
					Log.v("API", "Executing the req.");
					response = getClient().execute(request);
					Log.v("API", "Processing the resp.");
					json = processResponse(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setChanged();
				Log.v("API", "Notifying.");
				notifyObservers(processJSON(json));
				try {
					Thread.currentThread().sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	public class PlaceWorker extends APIWorker {
		private Location _location;
		
		public PlaceWorker(Location location) {
			_location = location;
		}
		
		private ArrayList<Place> JSONToPlaces(JSONObject object) {
			ArrayList<Place> places = new ArrayList<Place>();
			try {
				JSONArray results = (JSONArray) object.get("results");
				for(int i=0; i<results.length(); i++){
					JSONObject placeData = (JSONObject)results.get(i);
					places.add(new Place(placeData));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return places;
		}

		@Override
		JSONObject processResponse(HttpResponse r) {
			return createJSONFromResponse(r);
		}

		@Override
		AndroidHttpClient getClient() {
			return _googleClient;
		}

		@Override
		URI getQuery() {
			return createQueryString(_googleRoot, new HashMap<String, String>(){{
				put("key", _googleToken);
				put("location", _location.getLatitude() + "," + _location.getLongitude());
				put("radius", "300");
				put("sensor", "true");
			}});
		}

		@Override
		Object processJSON(JSONObject j) {
			return JSONToPlaces(j);
		}
	}
	
	public PlaceWorker placeWorkerForLocation(Observer observer, Location location){
		Log.v("API", "Starting new placeWorker.");
		return new PlaceWorker(location);
	}
}