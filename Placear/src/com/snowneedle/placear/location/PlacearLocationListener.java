package com.snowneedle.placear.location;

import java.util.ArrayList;
import java.util.Random;

import android.hardware.GeomagneticField;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class PlacearLocationListener implements LocationListener {
		
		private final static String TAG = "Placear";
		private Location lastKnownLocation;
		private ArrayList<Location> locations;
		private PlacearSensorEventListener sensorListener;
		
		public PlacearLocationListener(Location lastKnown, SensorEventListener sensorListener) {
			this.lastKnownLocation = lastKnown; 
			locations = new ArrayList<Location>();
			
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
			for(Location l1: locations) {
				Log.v(TAG, l1.toString());
			}
			this.sensorListener = (PlacearSensorEventListener) sensorListener;
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
		
		public double getDeclination() {
			GeomagneticField gf = new GeomagneticField(Double.valueOf(lastKnownLocation.getLatitude()).floatValue(),
					Double.valueOf(lastKnownLocation.getLongitude()).floatValue(),
					Double.valueOf(lastKnownLocation.getAltitude()).floatValue(),
					System.currentTimeMillis());
			return gf.getDeclination();
		}
		
		public Location getClosestLocation() {
			if(locations.size() == 0) return null;
			Location closestLocation = null;
			float bearing = lastKnownLocation.bearingTo(locations.get(0));
			float minDirection = (float) ((sensorListener.getAzimuth() + getDeclination()) - bearing);
			float d = 0;
			int i = 0;
			for(Location l: locations) {
				bearing = lastKnownLocation.bearingTo(l);
				d = (float) ((sensorListener.getAzimuth() + getDeclination()) - bearing);
				Log.v("I", "" + i + " " + d);
				if(d < minDirection) {
					closestLocation = l;
					minDirection = d;
				}
				i++;
			}
			return closestLocation;
		}
		
	}