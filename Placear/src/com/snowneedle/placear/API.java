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
import android.os.AsyncTask;
import android.util.Log;

public class API {
	
	private String _googleToken;	
	private AndroidHttpClient _googleClient;
	private final String _mapRoot = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private final String _mapDetailRoot = "https://maps.googleapis.com/maps/api/place/details/json";
	
	public API(String googleToken, String facebookToken){
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
	
	private ArrayList<Place> JSONToPlaces(JSONObject object) {
		ArrayList<Place> places = new ArrayList<Place>();
		try {
			JSONArray results = (JSONArray) object.get("results");
			for(int i=0; i<results.length(); i++){
				JSONObject placeData = (JSONObject)results.get(i);
				Place p = new Place(placeData);
				places.add(p);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return places;
	}
//	
//	public class PlaceWorker extends Observable implements Runnable {
//		
//		private Location _location;
//		
//		public PlaceWorker(Location loc) {
//			_location = loc;
//		}
//		
//		@Override
//		public void run() {
//			URI query = createQueryString(_mapRoot, new HashMap<String, String>(){{
//				put("key", _googleToken);
//				put("location", _location.getLatitude() + "," + _location.getLongitude());
//				put("radius", "300");
//				put("sensor", "true");
//			}});
//			HttpGet request = new HttpGet(query);
//			HttpResponse response;
//			JSONObject json = null;
//			ArrayList<Place> places = new ArrayList<Place>();
//			while(true) {
//				response = _googleClient.execute(request);
//				json = createJSONFromResponse(response);
//				places = JSONToPlaces(json);
//				new AsyncTask<Place, Void, PlaceDetail>() {
//					@Override
//					protected PlaceDetail doInBackground(Place... arg0) {
//						URI detailQuery;
//						HttpGet req;
//						HttpResponse resp;
//						JSONObject j = null;
//						for(Place p: arg0) {
//							detailQuery = createQueryString(_mapDetailRoot, new HashMap<String, String>() {{
//								put("key", _googleToken);
//								put("reference", p.getReference());
//								put("sensor", "true");
//							}});
//							req = new HttpGet(detailQuery);
//							resp = _googleClient.execute(req);
//							j = createJSONFromResponse(resp);
//							p.setDetail(new PlaceDetail(j.getJSONObject("result")));
//						}
//					}
//				}.execute((Place[]) places.toArray());
//			}
//		}
//	}
	
	public class PlaceWorker extends Observable implements Runnable {
		private Location _location;
		
		public PlaceWorker(Location location) {
			_location = location;
		}
		
		private PlaceDetail getDetail(Place place) throws IOException, JSONException{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("key", _googleToken);
			params.put("reference", place.getReference());
			params.put("sensor", "true");
			URI query = createQueryString(_mapDetailRoot, params);
			HttpGet detailRequest = new HttpGet(query);
			HttpResponse response = _googleClient.execute(detailRequest);
			JSONObject json = createJSONFromResponse(response);
			return new PlaceDetail(json.getJSONObject("result"));
		}
		
		public void run() {
			URI query = createQueryString(_mapRoot, new HashMap<String, String>(){{
				put("key", _googleToken);
				put("location", _location.getLatitude() + "," + _location.getLongitude());
				put("radius", "300");
				put("sensor", "true");
			}});
			HttpGet placeRequest = new HttpGet(query);
			while(true){
				HttpResponse response;
				try {
					response = _googleClient.execute(placeRequest);
					JSONObject json = createJSONFromResponse(response);
					ArrayList<Place> places = JSONToPlaces(json);
					for(Place place : places) {
						PlaceDetail detail = getDetail(place);
						place.setDetail(detail);
					}
					setChanged();
					notifyObservers(places);
					Thread.currentThread().sleep(5000);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
//	private abstract class APIWorker extends Observable implements Runnable {
//		
//		protected boolean infinite = true;
//		
//		abstract AndroidHttpClient getClient();
//		abstract URI getQuery();
//		abstract JSONObject processResponse(HttpResponse r);
//		abstract Object processJSON(JSONObject j);
//
//		@Override
//		public void run() {
//			URI query = getQuery();
//			HttpGet request = new HttpGet(query);
//			HttpResponse response;
//			JSONObject json = null;
//			Log.v("API", "Starting thread.");
//			do {
//				try {
//					Log.v("API", "Executing the req.");
//					response = getClient().execute(request);
//					Log.v("API", "Processing the resp.");
//					json = processResponse(response);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				setChanged();
//				Log.v("API", "Notifying.");
//				notifyObservers(processJSON(json));
//				try {
//					Thread.currentThread().sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while(infinite);
//			
//		}
//		
//	}
	
//	public class PlaceWorker extends APIWorker {
//		private Location _location;
//		
//		public PlaceWorker(Location location) {
//			_location = location;
//		}
//		
//		@Override
//		JSONObject processResponse(HttpResponse r) {
//			return createJSONFromResponse(r);
//		}
//
//		@Override
//		AndroidHttpClient getClient() {
//			return _googleClient;
//		}
//
//		@Override
//		URI getQuery() {
//			return null;
//		}
//
//		@Override
//		Object processJSON(JSONObject j) {
//			return JSONToPlaces(j);
//		}
//	}
	
//	public class PlaceDetailWorker extends APIWorker {
//		
//		private String _reference;
//		
//		public PlaceDetailWorker(String reference) {
//			this.infinite = false;
//			_reference = reference;
//		}
//
//		@Override
//		AndroidHttpClient getClient() {
//			return _googleClient;
//		}
//
//		@Override
//		URI getQuery() {
////			return 
//		}
//			
//
//		@Override
//		JSONObject processResponse(HttpResponse r) {
//			AsyncTask<Place>
//			return createJSONFromResponse(r);
//		}
//		
//		private PlaceDetail JSONToPlaceDetail(JSONObject j) {
//			try {
//				return new PlaceDetail(j.getJSONObject("result"));
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//
//		@Override
//		Object processJSON(JSONObject j) {
//			return JSONToPlaceDetail(j);
//		}
//		
//	}
	
	public PlaceWorker placeWorkerForLocation(Location location){
		return new PlaceWorker(location);
	}
	
}