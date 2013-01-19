package com.snowneedle.placear;

import com.snowneedle.placear.R;
import com.snowneedle.placear.util.SystemUiHider;

import android.annotation.TargetApi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class Placear extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_placear);
		PlacearCamera preview = new PlacearCamera(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
	}
}
