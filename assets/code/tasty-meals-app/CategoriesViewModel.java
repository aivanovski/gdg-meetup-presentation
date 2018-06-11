package com.example.tastymeals.ui.categories;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tastymeals.App;
import com.example.tastymeals.R;
import com.example.tastymeals.domain.model.Category;
import com.example.tastymeals.domain.model.Resource;
import com.example.tastymeals.domain.repository.CategoryRepository;
import com.example.tastymeals.ui.core.ViewState;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoriesViewModel extends AndroidViewModel {

	@Inject
	CategoryRepository repository;

	private MutableLiveData<String> emptyTextData;
	private MutableLiveData<String> errorTextData;
	private MutableLiveData<ViewState> stateData;
	private MutableLiveData<Boolean> refreshingData;
	private MutableLiveData<List<Category>> categoriesData;
	private CompositeDisposable disposables;

	public CategoriesViewModel(@NonNull Application application) {
		super(application);
		App.getDaggerComponent().inject(this);

		emptyTextData = new MutableLiveData<>();
		errorTextData = new MutableLiveData<>();
		stateData = new MutableLiveData<>();
		refreshingData = new MutableLiveData<>();
		categoriesData = new MutableLiveData<>();
		disposables = new CompositeDisposable();
	}

	LiveData<ViewState> getState() {
		return stateData;
	}

	LiveData<String> getEmptyText() {
		return emptyTextData;
	}

	LiveData<String> getErrorText() {
		return errorTextData;
	}

	LiveData<Boolean> isRefreshing() {
		return refreshingData;
	}

	LiveData<List<Category>> getCategories() {
		return categoriesData;
	}

	void start() {
		loadData(true);
	}

	private void loadData(boolean displayProgress) {
		Disposable disposable = repository.getCategories()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(d -> {
					if (displayProgress) {
						stateData.postValue(ViewState.PROGRESS);
					}
				})
				.subscribe(this::onCategoriesLoaded);

		disposables.add(disposable);
	}

	private void onCategoriesLoaded(Resource<List<Category>> result) {
		List<Category> categories = result.getResult();

		if (result.getError() == null) {
			if (categories.size() != 0) {
				categoriesData.setValue(categories);
				stateData.setValue(ViewState.DATA);
			} else {
				emptyTextData.setValue(getApplication().getString(R.string.no_items));
				stateData.setValue(ViewState.EMPTY);
			}
		} else {
			if (categories.size() != 0) {
				categoriesData.setValue(categories);
				errorTextData.setValue(createErrorMessage(result.getError()));
				stateData.setValue(ViewState.DATA_WITH_ERROR_PANEL);
			} else {
				errorTextData.setValue(createErrorMessage(result.getError()));
				stateData.setValue(ViewState.ERROR);
			}
		}

		refreshingData.setValue(false);
	}

	private String createErrorMessage(Throwable error) {
		String message;

		if (error instanceof IOException) {
			message = getApplication().getString(R.string.check_your_internet_connection);
		} else {
			message = getApplication().getString(R.string.error_was_occurred);
		}

		return message;
	}

	void refreshData() {
		refreshingData.setValue(true);
		loadData(false);
	}

	@Override
	protected void onCleared() {
		disposables.clear();
	}
}
