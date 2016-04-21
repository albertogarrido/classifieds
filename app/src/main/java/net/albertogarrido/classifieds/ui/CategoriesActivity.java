package net.albertogarrido.classifieds.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import net.albertogarrido.classifieds.R;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.presenters.CategoriesPresenter;
import net.albertogarrido.classifieds.presenters.ICategoriesPresenter;
import net.albertogarrido.classifieds.ui.adapters.CategoryListAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoriesActivity extends ClassifiedsActivity implements CategoriesPresenter.ICategoriesView {

    private static final String TAG = CategoriesActivity.class.getSimpleName();

    @Bind(R.id.categories_main_view) RelativeLayout categoriesMainView;
    @Bind(R.id.categories_grid) RecyclerView categoriesGrid;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private ICategoriesPresenter presenter;
    private CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);
        presenter = new CategoriesPresenter(this);
        configureToolbar();
        setToolbarTitle(getResources().getString(R.string.categories));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbarTitle(getResources().getString(R.string.categories));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public View getMainView() {
        return categoriesMainView;
    }

    @Override
    public void configureToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }


    @Override
    public void setupCategoriesGrid() {
        categoriesGrid.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                List<GoogleImage> list = categoryListAdapter.getList();
                return list.get(position).getCategoryRelevance();
            }
        });
        categoriesGrid.setLayoutManager(layoutManager);
    }

    @Override
    public void startCategoryItemsActivityForCategory(String categoryName) {
        Intent intent = new Intent(this, CategoryItemsActivity.class);
        intent.putExtra(CategoryItemsActivity.CATEGORY_NAME, categoryName);
        startActivity(intent);
    }

    @Override
    public void populateGridItems(List<GoogleImage> images) {
        categoryListAdapter = new CategoryListAdapter(getContext(), presenter, images);
        categoriesGrid.setAdapter(categoryListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}