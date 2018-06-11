package com.example.tastymeals.ui.categories;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tastymeals.R;
import com.example.tastymeals.domain.model.Category;
import com.example.tastymeals.ui.core.BaseFragment;
import com.example.tastymeals.ui.core.adapter.SingleLineWithIconAdapter;
import com.example.tastymeals.ui.recipes.RecipesActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends BaseFragment {

	private SingleLineWithIconAdapter adapter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private CategoriesViewModel viewModel;

	public static CategoriesFragment newInstance() {
		return new CategoriesFragment();
	}

	@Override
	public void onStart() {
		super.onStart();
		viewModel.start();
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container,
									   Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.categories_fragment, container, false);

		RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
		swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

		LinearLayoutManager layoutManager =
				new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		DividerItemDecoration dividerItemDecoration =
				new DividerItemDecoration(getContext(), layoutManager.getOrientation());
		adapter = new SingleLineWithIconAdapter(getContext());

		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(dividerItemDecoration);
		recyclerView.setAdapter(adapter);

		viewModel = ViewModelProviders.of(getActivity()).get(CategoriesViewModel.class);

		viewModel.getState().observe(this, this::setViewState);
		viewModel.getEmptyText().observe(this, this::setEmptyText);
		viewModel.getErrorText().observe(this, this::setErrorText);
		viewModel.isRefreshing().observe(this, swipeRefreshLayout::setRefreshing);
		viewModel.getCategories().observe(this, this::setCategories);

		swipeRefreshLayout.setOnRefreshListener(viewModel::refreshData);

		return view;
	}

	private void setCategories(List<Category> categories) {
		adapter.setItems(createAdapterItems(categories));
		adapter.setOnItemClickListener(position -> openRecipes(categories.get(position)));
		adapter.notifyDataSetChanged();
	}

	private List<SingleLineWithIconAdapter.Item> createAdapterItems(List<Category> categories) {
		List<SingleLineWithIconAdapter.Item> result = new ArrayList<>();

		for (Category category : categories) {
			result.add(new SingleLineWithIconAdapter.Item(category.getName(), category.getImageUrl()));
		}

		return result;
	}

	private void openRecipes(Category category) {
		startActivity(RecipesActivity.createStartIntent(getContext(), category));
	}
}
