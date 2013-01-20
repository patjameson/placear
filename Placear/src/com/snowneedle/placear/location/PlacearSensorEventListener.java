package com.snowneedle.placear.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class PlacearSensorEventListener implements SensorEventListener {
		SensorManager sensorManager;
		Sensor compass;
		private float azimuth;
		
		public PlacearSensorEventListener(SensorManager _sensorManager) {
			sensorManager = _sensorManager;
			compass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
			sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			azimuth = event.values[0];
		}
		
		public float getAzimuth() {
			return azimuth;
		}

	}