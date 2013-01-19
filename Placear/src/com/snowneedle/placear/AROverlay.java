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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class AROverlay implements GLSurfaceView.Renderer, SensorEventListener {
	
//	PlacearSensorEventListener _sensor;
	SensorManager _sensorManager;
	Sensor _compass;
	float _azimuth;
	float[] _orientation;
	PlacearLocationManager _locationManager;
	Location _currentLocation;
	ArrayList<Place> _places;
	
	public AROverlay(Placear context) {
		_locationManager = context.getLocationManager();
//		_sensor = _locationManager.getSensorListener();
		_sensorManager = context.sensorManager;
		_compass = _sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		_sensorManager.registerListener(this, _compass, SensorManager.SENSOR_DELAY_NORMAL);
//		_azimuth = 0;
		_orientation = new float[3];
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
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		gl.glMatrixMode(GL10.GL_MODELVIEW);
//		gl.glLoadIdentity();
		
//		gl.glColor4f(0f, 1f, 0f, 1f);
//		gl.glPointSize(10.0f);
//		
//		gl.glMatrixMode(GL10.GL_PROJECTION);
//		gl.glLoadIdentity();
		
		Log.w("orientation", "x: " + _orientation[0] + ",\ty: " + _orientation[1] + ",\tz: " + _orientation[2]);
		Location placeLocation = _places.get(0).getLocation();
		_currentLocation.bearingTo(placeLocation);
		
//		Float height = 0.0001f;;
//		
////		float points[] = {(float)placeLocation.getLatitude(), (float)placeLocation.getLongitude()};
////		float points[] = {-1f, -h, 0.5f,
////				1f, -h, 0.5f,
////				1f, h, 0.5f,
////				-1f, h, 0.5f
////				};
//		double rad = -Math.PI * _azimuth / 180d;
//		
//		float pointX = (float)(_currentLocation.getLongitude() + Math.cos(rad)*4f);
//		float pointZ = (float)(_currentLocation.getLatitude() + Math.sin(rad)*4f);
//		
//		double inverse = - 1 / rad;
//		Float lookatX = (float)(_currentLocation.getLongitude() + placeLocation.getLongitude()) / 2;
//		Float lookatZ = (float)(_currentLocation.getLatitude() + placeLocation.getLatitude()) / 2;
//		
//		float size = 20;
//		float minX = (float)(pointX - Math.cos(inverse)*size);
//		float maxX = (float)(pointX + Math.cos(inverse)*size);
//		float minZ = (float)(pointZ - Math.sin(inverse)*size);
//		float maxZ = (float)(pointZ + Math.sin(inverse)*size);
//		
//		float h = 0.5f;
//		
//		float points[] = {
//			minX, -h, minZ,
//			minX,  h, minZ,
//			maxX,  h, maxZ
//		};
//		
//		FloatBuffer vertexBuffer;
//		ByteBuffer bb = ByteBuffer.allocateDirect(points.length*4);
//		bb.order(ByteOrder.nativeOrder());
//		vertexBuffer = bb.asFloatBuffer();
//		vertexBuffer.put(points);
//		vertexBuffer.position(0);
//		
//		gl.glPushMatrix();
////		gl.glRotatef(_azimuth, 0, 1f, 0);
//		
////		float rad = (float)Math.atan2(_currentLocation.getLongitude() - placeLocation.getLongitude(),
////				_currentLocation.getLatitude() - placeLocation.getLatitude());
////		float perpendicular = -1f / rad;
////		float distance = 0.5f;
//		
//		Log.w("lookat", lookatX.toString());
//		
////		GLU.gluLookAt(gl, 0f, 0f, 0f, lookatX, 0f, lookatZ, 0f, 1f, 0f);
//		GLU.gluLookAt(gl, (float)_currentLocation.getLongitude(), 0f, (float)_currentLocation.getLatitude(),
//				pointX, 0f, pointZ, 0f, 1f, 0f);
//		
////		gl.glPopMatrix();
//		
//		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, points.length/3);
//		
//		gl.glPopMatrix();
//		
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
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		_orientation = event.values;
//		_inclination = event.values[1];
//		_azimuth = event.values[0];
	}

}
