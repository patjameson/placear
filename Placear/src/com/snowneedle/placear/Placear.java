package com.snowneedle.placear;

import com.snowneedle.placear.R;
import com.snowneedle.placear.location.PlacearLocationManager;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.util.Log;

import android.widget.FrameLayout;

public class Placear extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_placear);
		PlacearCamera preview = new PlacearCamera(this);
		FrameLayout f = ((FrameLayout) findViewById(R.id.preview));
		f.addView(preview);
		
		Log.v("Placear", "Creating PLM.");
		SensorManager sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		PlacearLocationManager PLM = new PlacearLocationManager(lm, sensorManager);
		Log.v("Placear", "Created PLM.");
		
//		mGLView = new Test3d(this);
//        setContentView(mGLView);
	}
}
