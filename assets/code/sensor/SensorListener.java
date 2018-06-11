package com.github.aivanovsky.lifecycledemo.sensor;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorListener implements
		LifecycleObserver,
		SensorEventListener {

	private final SensorManager sensorManager;
	private final OnAccelerometerChangeLister callback;

	public interface OnAccelerometerChangeLister {
		void onAccelerometerChanged(SensorEvent event);
	}

	public SensorListener(Context context,
						  OnAccelerometerChangeLister callback) {
		this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		this.callback = callback;
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	void registerListener() {
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	void unregisterListener() {
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (callback != null) {
			callback.onAccelerometerChanged(event);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
