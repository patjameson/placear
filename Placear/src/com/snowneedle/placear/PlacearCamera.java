package com.snowneedle.placear;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.snowneedle.placear.location.PlacearLocationManager;
	
public class PlacearCamera extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera camera;
	private Paint paint;
	private Paint small;
	private Paint tiny;
	private Paint box;
	private Placear context;
	private Paint circle;
	private PlacearLocationManager plm;
	private Caller caller;
	
	private ActionBar bar;
	
	private String phoneNumber;
	

	PlacearCamera(Placear context, PlacearLocationManager _plm, ActionBar bar) {
		
		super(context);
		this.context = context;
		this.setWillNotDraw(false);		
		paint = new Paint();
		small = new Paint();
		tiny = new Paint();
		box = new Paint();
		circle = new Paint();
		
		plm = _plm;
		this.bar = bar;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		caller = new Caller(context);
		phoneNumber = "";
	}
	
	@Override 
    public void onDraw(Canvas canvas) {		
		
		paint.setColor(Color.WHITE);

		paint.setStrokeWidth(3);

		paint.setTextSize(50);
		canvas.rotate(0);
		
		Location cur_loc = plm.getCurrentLocation();
		small.setTextSize(30);
        small.setColor(Color.WHITE);	
        tiny.setTextSize(25);
        tiny.setColor(Color.WHITE);
        
		double temp_lat = cur_loc.getLatitude();
		double temp_long = cur_loc.getLongitude();			
		
		DecimalFormat twoD = new DecimalFormat("#.##");
      	double lat = Double.valueOf(twoD.format(temp_lat));
      	double longi = Double.valueOf(twoD.format(temp_long));      
        String lats = Double.toString(lat);
        String longs = Double.toString(longi);
        
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        
        Place p = plm.getClosestLocation(); 
       
        if(p != null) {
        
	        phoneNumber = p.getDetail().getPhoneNumber();
	        String name = p.getName();
	        Double rating = p.getRating();
	        String ratingStars = "";
	        if(rating != 0) {
	        	for(int i = 0; i < rating.intValue(); i++) {
	        		ratingStars += " * ";
	        	}
	        }
	        int range = p.getPriceLevel();
	        String priceLevel = "";
	        if(range != 0) {
	        	for(int i = 0; i < range; i++) {
	        		priceLevel += " $ ";
	        	}
	        }
	        String address = p.getDetail().getAddress();
	        int isOpen = p.isOpenNow();
	        String days = p.getDetail().convertIntToDay(day);
	        HashMap<String, String> hours = p.getDetail().getHoursForDay(days);
	        if (hours != null) {
		        String openTime = hours.get("open");
		        String closeTime = hours.get("close");
		        
		        if (openTime != null && closeTime != null){       	       	
		       
		        	canvas.drawText(openTime + " - " + closeTime, 100, 250, small);
		        }
	        }
	        small.setShadowLayer(15, 5, 5, Color.BLACK);
	        paint.setShadowLayer(15, 5, 5, Color.BLACK);
	        
	        if (isOpen == 1) {
	        	circle.setColor(Color.GREEN);
	        	canvas.drawCircle(45, 200, 20, circle);
	        } else if (isOpen == 0) {
	        	circle.setColor(Color.RED);
	        	canvas.drawCircle(45, 200, 20, circle);
	        } else {
	        	circle.setColor(Color.DKGRAY);
	        	canvas.drawCircle(45, 200, 20, circle);
	        }
	        
	        Bitmap bmp = p.getDetail().getIcon();
	        canvas.drawBitmap(bmp, 15, 75, paint);	        
	        
	        canvas.drawText(name, 100, 125, paint);
	        canvas.drawText(address, 100, 180, small);
	        canvas.drawText("Ph: " + phoneNumber, 100, 220, small);
	        
	        canvas.drawText(ratingStars, 100, 270, small);
	        canvas.drawText(priceLevel, 100, 320, small);
        }               
        
			canvas.drawText("Lat: "+ lats, 15, 650, tiny);
			canvas.drawText("Long: " + longs, 150, 650, tiny);
      	        	     
        
        float direction = plm.getAzimuth();
        
        if (direction > 180)
			direction = -(360 - direction);
			
		
		
		
        
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getX() > 100 && event.getX() < 350 && event.getY() > 200 && event.getY() < 230) {
			caller.placeCall(phoneNumber);
			return true;
		} else if(event.getAction() == 0) { 
			if(bar.isShowing()) {
				bar.hide();
			} else {
				bar.show();
			}
			return true;
		}
		return false;
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
		camera.release(); 
		camera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(w, h);
		camera.startPreview();
	}


}

