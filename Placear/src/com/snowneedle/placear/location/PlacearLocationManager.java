package com.snowneedle.placear.location;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import com.snowneedle.placear.API;
import com.snowneedle.placear.Place;
import com.snowneedle.placear.API.PlaceWorker;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.snowneedle.placear.Place;


public class PlacearLocationManager implements SensorEventListener, LocationListener, Observer {
	private float azimuth = -1;
	
	private Location lastKnownLocation;
	private LocationManager locationManager;
	private ArrayList<Place> places;
	private API api;
	ArrayList<Location> locations = new ArrayList<Location>();
	String provider = LocationManager.NETWORK_PROVIDER;
	
	public PlacearLocationManager(LocationManager _locationManager, SensorManager sensorManager, String googleAccessToken) {
		locationManager = _locationManager;
		locationManager.requestLocationUpdates(provider, 0, 0, this);
		
		Sensor compass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
		
		places = new ArrayList<Place>();
		api = new API(googleAccessToken);
		
		PlaceWorker worker = api.placeWorkerForLocation(locationManager.getLastKnownLocation(provider));
		new Thread(worker).start();
		worker.addObserver(this);
	}
	
	/******BEARING******/
	@Override
	public void onSensorChanged(SensorEvent event) {
		azimuth = event.values[0];
	}
	public ArrayList<Place> getPlaces(){ return places; }
	
	public float getAzimuth() {
		return azimuth;
	}
	
	/******GET CLOSEST LOCATION******/
	public Place getClosestLocation() {
		System.out.println(places.size());
		if(places.size() == 0) return null;
		
		Place closestLocation = null;
		
		Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
		System.out.println(lastKnownLocation.getAccuracy() + " " + azimuth + " " + lastKnownLocation.getLatitude());
		
		if (azimuth != -1 && lastKnownLocation != null) {
			float direction = azimuth;
			
			if (direction > 180)
				direction = -(360 - direction);
			
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
		}
		
		if (closestLocation != null)
			return closestLocation;
		else
			return null;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		places = (ArrayList<Place>)data;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
