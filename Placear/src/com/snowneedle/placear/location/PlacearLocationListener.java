package com.snowneedle.placear.location;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import com.snowneedle.placear.API;
import com.snowneedle.placear.API.PlaceWorker;
import com.snowneedle.placear.Place;
import com.snowneedle.placear.Placear;

import com.snowneedle.placear.PlacearCamera;

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
		
		public PlacearLocationListener(Location lastKnown, PlacearSensorEventListener _sensorListener, 
				String googleAccessToken, Placear placear) {
			this.lastKnownLocation = lastKnown; 
			places = new ArrayList<Place>();
			sensorListener = _sensorListener;
			
			api = new API(googleAccessToken);
			PlaceWorker worker = api.placeWorkerForLocation(this, lastKnownLocation);
			new Thread(worker).start();
			worker.addObserver(this);
			
//			Location l = new Location("");
//			l.setLatitude(39.952682);
//			l.setLongitude(-75.190151);
//			locations.add(l);
//			l = new Location("");
//			l.setLatitude(39.95232);
//			l.setLongitude(-75.190864);
//			locations.add(l);
//			l = new Location("");
//			l.setLatitude(39.952127);
//			l.setLongitude(-75.190242);
//			locations.add(l);
//			l = new Location("");
//			l.setLatitude(39.953054);
//			l.setLongitude(-75.189614);
//			locations.add(l);
			
			this.sensorListener = (PlacearSensorEventListener) sensorListener;
		}
		
		public class Refresh extends Observable implements Runnable {
			
			@Override
			public void run() {
				while (true) {
					Place place = getClosestLocation();
					
					if (place != null)
						System.out.println(place.getName());
					
					setChanged();
					notifyObservers(place);
					try {
						Thread.currentThread().sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		public Refresh refreshForLocation(Observer observer){
			return new Refresh();
		}
		
		@Override
		public void update(Observable observable, Object data) {
			places = (ArrayList<Place>)data;
		}
		

		@Override
		public void onLocationChanged(Location arg0) {
			lastKnownLocation = arg0;
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
		
		public Place getClosestLocation() {
			System.out.println(places.size());
			
			if(places.size() == 0) return null;
			Place closestLocation = null;
			
			
			ArrayList<Place> chosenPlaces = new ArrayList<Place>();
			
			System.out.println("here!");
			
			if (sensorListener != null && lastKnownLocation != null) {
				float direction = sensorListener.getAzimuth();
				if (direction > 180)
					direction = -(360 - direction);
				
				System.out.println(direction);
				
				double closestDistance = -1;
				
				for(Place p: places) {
					float bearing = lastKnownLocation.bearingTo(p.getLocation());
					if (Math.abs(bearing-direction) < 30) {
						double curDistance = ((p.getLocation().getLatitude()-lastKnownLocation.getLatitude()) * 
								(p.getLocation().getLatitude()-lastKnownLocation.getLatitude()) +
								(p.getLocation().getLongitude()-lastKnownLocation.getLongitude()) * 
								(p.getLocation().getLongitude()-lastKnownLocation.getLongitude()));
						if (curDistance < closestDistance || closestDistance == -1) {
							closestDistance = curDistance;
							closestLocation = p;
						}
					}
				}
				if (closestLocation != null)
					System.out.println(closestLocation.getName() + " " + closestLocation.getLocation().getLatitude() + " " + closestLocation.getLocation().getLongitude());
				else
					System.out.println("none");
			}
			if (closestLocation != null)
				return closestLocation;
			else
				return null;
		}
		
	}