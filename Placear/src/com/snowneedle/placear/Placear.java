package com.snowneedle.placear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.snowneedle.placear.location.PlacearLocationManager;

public class Placear extends Activity {
	SensorManager sensorManager;
	PlacearLocationManager PLM;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
		
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		PLM = new PlacearLocationManager(lm, sensorManager, getGoogleAccessToken());
		
		setContentView(R.layout.activity_placear);
		PlacearCamera preview = new PlacearCamera(this);
		FrameLayout f = ((FrameLayout) findViewById(R.id.preview));
		f.addView(preview);
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
}
