package com.snowneedle.placear;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.location.*;

public class API {
	
	private String _token;
//	private AndroidHttpClient _facebookClient;
	private AndroidHttpClient _googleClient;
//	private URI _facebookRoot;
	private URI _googleRoot; 
	private JSONObject _locationsCache;
	
	public API(String token){
		_token = token;
		_googleClient = AndroidHttpClient.newInstance("PlaceAR");
		try {
			_googleRoot = new URI("https://maps.googleapis.com/maps/api/place/nearbysearch/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
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
	
	public class PlaceWorker extends Observable implements Runnable {
		private Location _location;
		public PlaceWorker(Location location) {
			_location = location;
		}
		
		@Override
		public void run() {
			String queryString = "json?key=" + _token + "&location=" + _location.getLatitude() +
					"," + _location.getLongitude() + "&radius=300&sensor=true";
			
			
			HttpGet request = new HttpGet(_googleRoot.resolve(queryString));
			HttpResponse response;
			String responseBody = "";
			JSONObject json = null;
			while(true) {
				try {
					Thread.currentThread().sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (_locationsCache == null) {
					System.out.println("CALLING GOOGLE");
					try {
						response = _googleClient.execute(request);
						System.out.println(_googleRoot.resolve(queryString));
						InputStream stream = response.getEntity().getContent();
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
						_locationsCache = json;
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				setChanged();
				notifyObservers(JSONToPlaces(_locationsCache));
				try {
					Thread.currentThread().sleep(50000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public PlaceWorker placeWorkerForLocation(Observer observer, Location location){
		return new PlaceWorker(location);
	}
}