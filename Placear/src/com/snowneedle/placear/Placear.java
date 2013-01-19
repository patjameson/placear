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
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.util.Log;

import android.widget.FrameLayout;

public class Placear extends Activity {
	SensorManager sensorManager;
	PlacearCamera preview;
	PlacearLocationManager PLM;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_placear);
		
		
		sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
		
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		PLM = new PlacearLocationManager(lm, sensorManager, getGoogleAccessToken(), this);
		
		preview = new PlacearCamera(this, PLM.locationListener);
		FrameLayout f = ((FrameLayout) findViewById(R.id.preview));
		f.addView(preview);
	}
	
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
	
	
}
