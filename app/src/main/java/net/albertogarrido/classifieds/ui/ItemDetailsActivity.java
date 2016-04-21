package net.albertogarrido.classifieds.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.albertogarrido.classifieds.R;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.presenters.IItemDetailsPresenter;
import net.albertogarrido.classifieds.presenters.ItemDetailsPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemDetailsActivity extends ClassifiedsActivity implements ItemDetailsPresenter.IItemDetailsView {

    private static final String TAG = ItemDetailsActivity.class.getSimpleName();
    public static final String SELECTED_ITEM = "selected_item_extra";

    private IItemDetailsPresenter presenter;

    @Bind(R.id.item_details_main_view) RelativeLayout itemDetailsMainView;
    @Bind(R.id.detail_image) ImageView detailImage;
    @Bind(R.id.detail_quote) TextView detailQuote;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);
        presenter = new ItemDetailsPresenter(this, getIntent());
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
        return itemDetailsMainView;
    }

    @Override
    public void setUpData(GoogleImage googleImageInformation) {
        setToolbarTitle(googleImageInformation.getTitle());
        Picasso.with(getContext()).load(googleImageInformation.getFullSizeLink()).into(detailImage);
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