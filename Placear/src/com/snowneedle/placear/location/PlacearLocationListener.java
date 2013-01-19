package com.snowneedle.placear.location;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import com.snowneedle.placear.API;
import com.snowneedle.placear.API.PlaceWorker;
import com.snowneedle.placear.Place;

import android.hardware.GeomagneticField;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class PlacearLocationListener implements LocationListener, Observer {
		
		private final static String TAG = "Placear";
		private Location lastKnownLocation;
		private ArrayList<Place> places;
		private PlacearSensorEventListener sensorListener;
		private API api;
		ArrayList<Location> locations = new ArrayList<Location>();
		
		public PlacearLocationListener(Location lastKnown, SensorEventListener sensorListener,
				String googleAccessToken, String facebookAccessToken) {
			this.lastKnownLocation = lastKnown; 
			places = new ArrayList<Place>();
			
			api = new API(googleAccessToken, facebookAccessToken);
			PlaceWorker worker = api.placeWorkerForLocation(this, lastKnownLocation);
			new Thread(worker).start();
			worker.addObserver(this);
			
			new Thread(new Runnable() {
				public void run() {
					System.out.println("in the thread");
					while (true) {
						getClosestLocation();
						
						try {
							Thread.currentThread().sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
			Location l = new Location("");
			l.setLatitude(39.956465);
			l.setLongitude(-75.20442);
			locations.add(l);
			l = new Location("");
			l.setLatitude(39.954622);
			l.setLongitude(-75.19721);
			locations.add(l);
			l = new Location("");
			l.setLatitude(39.961596);
			l.setLongitude(-75.192833);
			locations.add(l);
			l = new Location("");
			l.setLatitude(39.949622);
			l.setLongitude(-75.17601);
			locations.add(l);
			l = new Location("");
			l.setLatitude(39.941791);
			l.setLongitude(-75.184851);
			locations.add(l);
			l = new Location("");
			l.setLatitude(39.939817);
			l.setLongitude(-75.200901);
			locations.add(l);
			l = new Location("");
			l.setLatitude(39.952525);
			l.setLongitude(-75.190065);
			locations.add(l);
			
			this.sensorListener = (PlacearSensorEventListener) sensorListener;
		}
		
		@Override
		public void update(Observable observable, Object data) {
			places = (ArrayList<Place>)data;
		}
		

		@Override
		public void onLocationChanged(Location arg0) {
			lastKnownLocation = arg0;
			//Log.v(TAG, lastKnownLocation.toString());
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
		public Location getLastKnownLocation() {
			return lastKnownLocation;
		}
		
		public double getLastLong() {
			return lastKnownLocation.getLongitude();
		}
		
		public double getLastLat() {
			return lastKnownLocation.getLatitude();
		}
		
		public Location getClosestLocation() {
			//System.out.println(locations.size());
			
			if(locations.size() == 0) return null;
			Location closestLocation = null;
			
			
			ArrayList<Place> chosenPlaces = new ArrayList<Place>();
			
			for(Location p: locations) {
				float bearing = lastKnownLocation.bearingTo(p);
				//System.out.println("Bearing: " + bearing);
//				p.getLocation().getLatitude();
//				Location l = p.getLocation();
			}
			return closestLocation;
		}
		
	}