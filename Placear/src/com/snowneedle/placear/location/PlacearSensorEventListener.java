package com.snowneedle.placear.location;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class PlacearSensorEventListener implements SensorEventListener {
		
		private float azimuth;
		
		public PlacearSensorEventListener() {
			azimuth = (float) 0.0;
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// stub
		}

		@Override
		public void onSensorChanged(SensorEvent arg0) {
			float[] rotationMatrix = new float[16];
			float[] gravity = new float[3];
			float[] geomagnetic = new float[3];
			SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
			float[] vals = new float[3];
			SensorManager.getOrientation(rotationMatrix, vals);
			azimuth = vals[0];
			//Log.v("Placear", "" + arg0.values[0]); //apparently the first elem is direction
		}
		
		public float getAzimuth() {
			return azimuth * 180 / (float) Math.PI;
		}

	}