package com.snowneedle.placear;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.snowneedle.placear.location.PlacearLocationManager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.location.Location;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;


	
public class PlacearCamera extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera camera;
	private Paint paint;
	private Paint small;
	private PlacearLocationManager plm;


	PlacearCamera(Placear context, PlacearLocationManager _plm) {
		
		super(context);
		this.setWillNotDraw(false);		
		paint = new Paint();
		small = new Paint();
		
		plm = _plm;

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override 
    public void onDraw(Canvas canvas) {		
		
		paint.setColor(Color.WHITE);

		paint.setStrokeWidth(3);

		paint.setTextSize(60);
		canvas.rotate(0);
		canvas.skew(.25f, 0);


		if (plm != null) {
			Place loc = plm.getClosestLocation();
			if (loc != null) {
				canvas.drawText(loc.getName(), 300, 200, paint);
				Location cur_loc = plm.getCurrentLocation();
				
				double temp_lat = cur_loc.getLatitude();
				double temp_long = cur_loc.getLongitude();
				float accu = cur_loc.getAccuracy();
				
//				DecimalFormat twoD = new DecimalFormat("#.##");
//		      	double lat = Double.valueOf(twoD.format(temp_lat));
//		      	double longi = Double.valueOf(twoD.format(temp_long));      
		        String lats = Double.toString(temp_lat);
		        String longs = Double.toString(temp_long);
		        
		        small.setTextSize(40);
		        small.setColor(Color.WHITE);
		        canvas.drawText("Bearing: " + plm.getAzimuth(), 300, 500, small);
		        canvas.drawText("Lat: "+ lats, 300, 350, small);
		        canvas.drawText("Long: " + longs, 300, 400, small);
		        canvas.drawText("Accu: "+ accu, 300, 450, small);
		        canvas.drawText("bearto: " + plm.getCurrentLocation().bearingTo(loc.getLocation()), 300, 550, small);		        
		        
//		        TextView textView = (TextView)(findViewById(R.id.bot_bar));		        
//		        textView.setText("New text");		     
		        
		        float direction = plm.getAzimuth();
				
				if (direction > 180)
					direction = -(360 - direction);
					canvas.drawText("bearlive: " + Math.abs(plm.getCurrentLocation().bearingTo(loc.getLocation())-direction), 300, 600, small);
				
			}
		}	
		
		
        
//        String lat_long = getResources().getString(R.string.lat_long);
//        String lat_long_cur = String.format(lat_long, lats, longs);
        
        
        
//        EditText leftText = (EditText) findViewById(R.id.bot_bar);
//        leftText.setText(lat_long_cur);	

		
		try {
			Thread.currentThread().sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.invalidate();

    }

	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open(0);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(w, h);
		camera.startPreview();
	}


}

