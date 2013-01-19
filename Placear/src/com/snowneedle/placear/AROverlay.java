package com.snowneedle.placear;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.snowneedle.placear.location.PlacearLocationManager;
import com.snowneedle.placear.location.PlacearSensorEventListener;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class AROverlay implements GLSurfaceView.Renderer {
	
	PlacearSensorEventListener _sensor;
	PlacearLocationManager _locationManager;
	Location _currentLocation;
	ArrayList<Place> _places;
	
	public AROverlay(Placear context) {
		_locationManager = context.getLocationManager();
		_sensor = _locationManager.getSensorListener();
	}
	
	public void setupLocations(){
		_currentLocation = _locationManager.getLastKnownLocation();
//		_places = _locationManager.getPlaces();
		_places = new ArrayList<Place>();
		_places.add(new Place());
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		setupLocations();
		
//		gl.glMatrixMode(GL10.GL_MODELVIEW);
//		gl.glLoadIdentity();
		
		gl.glPointSize(10.0f);
		
		Float height = 0.0001f;
		Location placeLocation = _places.get(0).getLocation();
		
//		float points[] = {(float)placeLocation.getLatitude(), (float)placeLocation.getLongitude()};
		float points[] = {0.5f, 0.5f, 0.5f};
		
		FloatBuffer vertexBuffer;
		ByteBuffer bb = ByteBuffer.allocateDirect(points.length*4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(points);
		vertexBuffer.position(0);
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		GLU.gluLookAt(gl, (float)placeLocation.getLatitude(), height,
//				(float)placeLocation.getLongitude(),
//				(float)_currentLocation.getLatitude(), height,
//				(float)_currentLocation.getLongitude(), 0f, 1f, 0f);
		//gl.glDrawElements(GL10.GL_POINTS, points.length, GL10.GL_FLOAT, points);
//		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//		gl.glDrawElements(GL10.GL_POINTS, points.length, GL10.GL_UNSIGNED_BYTE, vertexBuffer);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
		gl.glLoadIdentity();                        // reset the matrix to its default state
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		gl.glClearColor(1f, 0f, 0f, 0.5f);
	}

}
