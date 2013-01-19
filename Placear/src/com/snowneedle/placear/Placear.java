package com.snowneedle.placear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.snowneedle.placear.Place;
import com.snowneedle.placear.R;
import com.snowneedle.placear.location.PlacearLocationManager;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.util.Log;

import android.widget.FrameLayout;

public class Placear extends Activity implements SensorEventListener {
	SensorManager sensorManager;
	Sensor compass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_placear);
		PlacearCamera preview = new PlacearCamera(this);
		FrameLayout f = ((FrameLayout) findViewById(R.id.preview));
		f.addView(preview);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		compass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		SensorManager sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		PlacearLocationManager PLM = new PlacearLocationManager(lm, sensorManager, getGoogleAccessToken());
		
		//Log.v("Placear", "Created PLM.");
		
//		mGLView = new Test3d(this);
//        setContentView(mGLView);
		
	}
	
	private String getGoogleAccessToken(){
		String token = "";
		InputStream in;
		try {
			in = getAssets().open("google_token");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			token = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.w("access token", token);
		
		return token;
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		double myAzimuth = Math.round(event.values[0]);
        double myPitch = Math.round(event.values[1]);
        double myRoll = Math.round(event.values[2]);
	}
}
