package com.snowneedle.placear;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.snowneedle.placear.API.PlaceWorker;
import com.snowneedle.placear.location.PlacearLocationListener;
import com.snowneedle.placear.location.PlacearLocationListener.Refresh;
import com.snowneedle.placear.location.PlacearLocationManager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.location.Location;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlacearCamera extends SurfaceView implements SurfaceHolder.Callback, Observer {
	SurfaceHolder mHolder;
	public Camera camera;
	Paint paint = new Paint();
	PlacearLocationListener ll;
	String text = "Nothing directly ahead...fd";

	PlacearCamera(Placear context, PlacearLocationListener cam) {
		super(context);
		this.setWillNotDraw(false);
		
		ll = cam;
		
//		Refresh worker = cam.refreshForLocation(this);
//		new Thread(worker).start();
//		worker.addObserver(this);

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		//this.invalidate();
	}
	
	@Override 
    public void onDraw(Canvas canvas) {
<<<<<<< HEAD
		System.out.println("fdkjlfsa");
		paint.setColor(Color.BLACK);
=======
		paint.setColor(Color.RED);
>>>>>>> 16aeaa1c0b4b23146664f69aa1c0473d76e1fa29
		paint.setStrokeWidth(3);
		paint.setTextSize(40);
//		canvas.translate(20, 20);
		canvas.rotate(0);
		canvas.skew(1, 0);
//		canvas.translate(-10, -10);
		
//		android.graphics.Camera cam = new android.graphics.Camera();
//		cam.save();
//		ca
		
//		Matrix imageMatrix = new Matrix();
//	    float[] srcPoints = {
//	        0, 0,
//	        0, 200,
//	        200, 200,
//	        200, 0};
//	    int rotation = -150;
//	    float[] destPoints = {
//	        rotation, rotation/2f,
//	        rotation, 200 - rotation/2f,
//	        200 - rotation, 200,
//	        200 - rotation, 0};
//	    imageMatrix.setPolyToPoly(srcPoints, 0, destPoints, 0, 4);
//	    canvas.setMatrix(imageMatrix);
//	    canvas.rotate(10);
<<<<<<< HEAD
		canvas.drawText("Test Text Here", 100, 200, paint);
=======
		if (ll != null) {
			Place loc = ll.getClosestLocation();
			if (loc != null)
				canvas.drawText(loc.getName(), 300, 200, paint);
		}
		try {
			Thread.currentThread().sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.invalidate();
>>>>>>> 16aeaa1c0b4b23146664f69aa1c0473d76e1fa29
    }
	

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
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
