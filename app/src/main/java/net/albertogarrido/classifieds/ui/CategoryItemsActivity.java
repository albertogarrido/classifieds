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
import android.widget.TextView;

import net.albertogarrido.classifieds.R;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.presenters.CategoryItemsPresenter;
import net.albertogarrido.classifieds.ui.adapters.CategoryItemsAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CategoryItemsActivity extends ClassifiedsActivity implements CategoryItemsPresenter.ICategoryItemsView {

    public static final String CATEGORY_NAME = "category_name";

    private static final String TAG = CategoryItemsActivity.class.getSimpleName();

    @Bind(R.id.category_items_main_view) RelativeLayout categoryItemsMainView;
    @Bind(R.id.category_items_grid) RecyclerView categoriesGrid;
    @Bind(R.id.header) TextView header;

    private CategoryItemsPresenter presenter;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
        ButterKnife.bind(this);
        setupCategoryItemsGrid();
        presenter = new CategoryItemsPresenter(this, getIntent());
        configureToolbar();
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
        return categoryItemsMainView;
    }

    @Override
    public void setupCategoryItemsGrid() {
        categoriesGrid.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        categoriesGrid.setLayoutManager(layoutManager);
    }

    @Override
    public void startItemDetail(GoogleImage googleImage) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra(ItemDetailsActivity.SELECTED_ITEM, googleImage);
        startActivity(intent);
    }

    @Override
    public void populateGridItems(List<GoogleImage> images) {

        header.setText(getResources().getString(R.string.results_of) + " " + images.get(0).getCategoryName());
        setToolbarTitle(images.get(0).getCategoryName());

        CategoryItemsAdapter categoryListAdapter = new CategoryItemsAdapter(getContext(), presenter, images);
        categoriesGrid.setAdapter(categoryListAdapter);
    }

    @Override
    public void configureToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }
}

