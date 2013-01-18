package com.example.placear;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.location.*;

public class API {
	
	AndroidHttpClient _facebookClient;
	URI _facebookRoot;
	
	public API(){
		_facebookClient = AndroidHttpClient.newInstance("PlaceAR");
		try {
			_facebookRoot = new URI("http://graph.facebook.com/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public AsyncTask<Location, Void, JSONArray> eventFinderForLocation(){
		return new AsyncTask<Location, Void, JSONArray>(){
			@Override
			protected JSONArray doInBackground(Location... params) {
				Location location = params[0];
				String queryString = "search?type=event&distance=300&center=" +
						location.getLatitude() + "," + location.getLongitude();
				HttpGet request = new HttpGet(_facebookRoot.resolve(queryString));
				HttpResponse response;
				String responseBody = "";
				JSONArray json = new JSONArray();
				try {
					response = _facebookClient.execute(request);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					responseBody = out.toString();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Log.w("data", responseBody);
				
				try {
					json = new JSONArray(responseBody);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				Log.w("tag", json.toString());
				
				return json;
			}
		};
//		
//		return null;
	}
}
