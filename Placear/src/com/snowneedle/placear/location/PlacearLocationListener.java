package com.snowneedle.placear.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class PlacearLocationListener implements LocationListener {
	
	private final static String TAG = "Placear";

	@Override
	public void onLocationChanged(Location arg0) {
		Log.v(TAG, arg0.toString());
		
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
	
}
