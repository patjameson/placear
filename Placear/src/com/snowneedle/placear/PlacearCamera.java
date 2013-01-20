package com.snowneedle.placear;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.snowneedle.placear.location.PlacearLocationManager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlacearCamera extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera camera;
	private Paint paint;;
	private PlacearLocationManager plm;

	PlacearCamera(Placear context, PlacearLocationManager _plm) {
		super(context);
		this.setWillNotDraw(false);
		
		paint = new Paint();
		
		plm = _plm;

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override 
    public void onDraw(Canvas canvas) {
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		paint.setTextSize(35);
		canvas.rotate(-90, 250, 250);
		
		if (plm != null) {
			Place loc = plm.getClosestLocation();
			if (loc != null) {
				canvas.drawText(loc.getName(), 100, 50, paint);
				canvas.drawText("Bearing: " + plm.getAzimuth(), 100, 100, paint);
				canvas.drawText("Acc: " + plm.getCurrentLocation().getAccuracy(), 100, 150, paint);
				canvas.drawText("lat: " + plm.getCurrentLocation().getLatitude(), 100, 200, paint);
				canvas.drawText("long: " + plm.getCurrentLocation().getLongitude(), 100, 250, paint);
				canvas.drawText("bearto: " + plm.getCurrentLocation().bearingTo(loc.getLocation()), 100, 300, paint);
				
				float direction = plm.getAzimuth();
				
				if (direction > 180)
					direction = -(360 - direction);
				canvas.drawText("bearlive: " + Math.abs(plm.getCurrentLocation().bearingTo(loc.getLocation())-direction), 100, 350, paint);
			}
		} else {
			canvas.drawText("waiting...", 300, 200, paint);
		}
		
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
