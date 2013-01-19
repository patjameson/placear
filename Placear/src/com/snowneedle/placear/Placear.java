package com.snowneedle.placear;

import com.snowneedle.placear.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

public class Placear extends Activity {
	
	protected static final Context Context = null;
	private GLSurfaceView mGLView;
	
	public static final boolean HOLD_TOGGLE = true;
	
	public static final int AUTO_HIDE_TIME = 5000; //in ms	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			//Build Top Navigation Bar
		  	final ActionBar bar = getActionBar();
		    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		    ActionBar.Tab tabA = bar.newTab().setText("Search");
		    ActionBar.Tab tabB = bar.newTab().setText("Camera");
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
		PlacearCamera preview = new PlacearCamera(this);
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


