package com.snowneedle.placear;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PlacearCamera extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	public Camera camera;
	Paint paint = new Paint();

	PlacearCamera(Context context) {
		super(context);
		this.setWillNotDraw(false);

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override 
    public void onDraw(Canvas canvas) {
		System.out.println("fdkjlfsa");
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		paint.setTextSize(40);
//		canvas.translate(20, 20);
//		canvas.rotate(20);
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
//		canvas.drawText("Test Text Here", 100, 200, paint);
    }
	

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		camera = Camera.open();
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
