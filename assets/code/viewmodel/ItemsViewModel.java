package com.github.aivanovsky.lifecycledemo.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ItemsViewModel extends ViewModel {

	private MutableLiveData<List<Item>> itemsData;

	public LiveData<List<Item>> getItems() {
		if (itemsData == null) {
			itemsData = new MutableLiveData<>();
			loadItems();
		}
		return itemsData;
	}

	private void loadItems() {
		//load items from background thread
	}
}
