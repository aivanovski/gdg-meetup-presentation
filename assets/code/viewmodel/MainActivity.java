package com.github.aivanovsky.lifecycledemo.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ItemsViewModel viewModel = ViewModelProviders.of(this)
				.get(ItemsViewModel.class);

		viewModel.getItems().observe(this, items -> {
			//handle items in the UI
		});
	}
}
