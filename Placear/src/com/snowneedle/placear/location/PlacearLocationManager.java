package com.snowneedle.placear.location;

import android.hardware.SensorManager;
import android.location.LocationManager;

public class PlacearLocationManager {
	
	private LocationManager locationManager;
	private PlacearLocationListener locationListener;
	private SensorManager compass;
	
	public PlacearLocationManager(LocationManager locationManager, SensorManager compass) {
		locationListener = new PlacearLocationListener();
		
		this.locationManager = locationManager;
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		
		this.compass = compass;
	}

}
