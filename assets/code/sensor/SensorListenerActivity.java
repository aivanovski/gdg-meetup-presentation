package com.github.aivanovsky.lifecycledemo.sensor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SensorListenerActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getLifecycle().addObserver(new SensorListener(this, event -> {
			//handle sensor event in UI
		}));
	}
}
