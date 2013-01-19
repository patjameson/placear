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
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.util.Log;

import android.widget.FrameLayout;

public class Placear extends Activity {
	SensorManager sensorManager;
	PlacearLocationManager PLM;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
		
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		PLM = new PlacearLocationManager(lm, sensorManager, getGoogleAccessToken(), "");
		
		setContentView(R.layout.activity_placear);
		PlacearCamera preview = new PlacearCamera(this);
		FrameLayout f = ((FrameLayout) findViewById(R.id.preview));
		f.addView(preview);
		
//		// adding in opengl overlay
//		Renderer renderer = new AROverlay(this);
//		GLSurfaceView surfaceView = new GLSurfaceView(this);
//		surfaceView.setZOrderMediaOverlay(true);
//		surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
//		surfaceView.setRenderer(renderer);
//		surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
//		f.addView(surfaceView);
		
		// set up a call
//		Caller caller = new Caller(this);
//		caller.placeCall("973-985-6199");
		Log.v("P", "Starting app.");
		System.out.println("Test");
//		
//		sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
//		
//		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
//		PlacearLocationManager PLM = new PlacearLocationManager(lm, sensorManager,
//				getGoogleAccessToken(), getFacebookAccessToken());
	}
	
	public PlacearLocationManager getLocationManager(){ return PLM; }
	public SensorManager getSensorManager(){ return sensorManager; }
	
	private String getGoogleAccessToken() {
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
	
	private String getFacebookAccessToken() {
		String token = "";
		InputStream in;
		try {
			in = getAssets().open("facebook_token");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			token = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.w("access token", token);
		
		return token;
	}
	
	
}
