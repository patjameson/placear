package com.snowneedle.placear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.snowneedle.placear.R;
import com.snowneedle.placear.location.PlacearLocationManager;
import java.text.DecimalFormat;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.snowneedle.placear.location.PlacearLocationManager;

public class Placear extends Activity {

	SensorManager sensorManager;
	PlacearCamera preview;
	PlacearLocationManager PLM;
	
	public static final boolean HOLD_TOGGLE = true;
	
	public static final int AUTO_HIDE_TIME = 5000; //in ms	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			//Build Top Navigation Bar
		  	final ActionBar bar = getActionBar();
		    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		    ActionBar.Tab tabA = bar.newTab().setText("Camera");
		    ActionBar.Tab tabB = bar.newTab().setText("About");
		   // ActionBar.Tab tabC = bar.newTab().findItem(R.id.menu_search);

		    Fragment fragmentA = new AFragment();
		    Fragment fragmentB = new BFragment();
		   // Fragment fragmentC = new CFragment();	

		    tabA.setTabListener(new MyTabsListener(fragmentA));
		    tabB.setTabListener(new MyTabsListener(fragmentB));
		  //  tabC.setTabListener(new MyTabsListener(fragmentC));

		    bar.addTab(tabA);
		    bar.addTab(tabB);
		    //bar.addTab(tabC);			
		
		setContentView(R.layout.activity_placear);
		sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
		
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		PLM = new PlacearLocationManager(lm, sensorManager, getGoogleAccessToken());
		
		preview = new PlacearCamera(this, PLM);
		FrameLayout f = ((FrameLayout) findViewById(R.id.preview));

		f.addView(preview);			
		 
	 preview.setOnLongClickListener(new View.OnLongClickListener() {
			 
		public boolean onLongClick(View v){
				if (HOLD_TOGGLE) {    
			
					bar.show();
		}	
			return true;			
		}			
		 });
	 preview.setOnClickListener(new View.OnClickListener() {		 
		
		@Override
		public void onClick(View v) {
			if (HOLD_TOGGLE) {
				bar.hide();
			}
			// TODO Auto-generated method stub
			
		}
	});
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
		
		return token;
	}	
}

class MyTabsListener implements ActionBar.TabListener {
	public Fragment fragment;

	public MyTabsListener(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		//Toast.makeText(StartActivity.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.layout, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}
		
}


