package com.snowneedle.placear.location;

import java.util.ArrayList;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class PlacearLocationManager {
	
	private LocationManager locationManager;
	private PlacearLocationListener locationListener;
	private Location lastKnownLocation;
	private final int updateMs = 0;
	private final int updateDist = 0;
	
	private SensorManager sensorManager;
	private Sensor sensor;
	private PlacearSensorEventListener sensorListener;
	
	public PlacearLocationManager(LocationManager locationManager, SensorManager sensorManager) {
		Log.v("Placear", "Creating PSEL.");
		this.sensorListener = new PlacearSensorEventListener();
		this.sensorManager = sensorManager;
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		this.sensorManager.registerListener(sensorListener, this.sensor, SensorManager.SENSOR_DELAY_FASTEST);	
		
		locationListener = new PlacearLocationListener(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER),
				sensorListener);		
		this.locationManager = locationManager;
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				updateMs, updateDist, locationListener);
		Log.v("MYLOC", getLastKnownLocation().toString());
		Log.v("TEST", locationListener.getClosestLocation().toString());
	}
	
	public Location getLastKnownLocation() {
		return locationListener.getLastKnownLocation();
	}
	
	public double getLastLong() {
		return locationListener.getLastLong();
	}
	
	public double getLastLat() {
		return locationListener.getLastLat();
	}
	
	
	
	
	


}
