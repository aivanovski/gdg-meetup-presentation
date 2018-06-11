package com.example.tastymeals.ui.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.tastymeals.R;

public class CategoriesActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.core_base_activity);

		initActionBar();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, CategoriesFragment.newInstance())
				.commit();
	}

	private void initActionBar() {
		setSupportActionBar(findViewById(R.id.tool_bar));

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(R.string.app_name);
		}
	}
}
