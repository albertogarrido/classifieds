package net.albertogarrido.classifieds.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import net.albertogarrido.classifieds.R;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.presenters.ILandingPresenter;
import net.albertogarrido.classifieds.presenters.LandingPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingActivity extends ClassifiedsActivity implements LandingPresenter.ILandingView {

    private static final String TAG = LandingActivity.class.getSimpleName();

    public static final boolean SHOULDNT_FINISH_CURRENT = false;
    public static final boolean SHOULD_FINISH_CURRENT = true;

    private ILandingPresenter presenter;

    @Bind(R.id.landing_image) ImageView landingImage;
    @Bind(R.id.landingMainView) RelativeLayout landingMainView;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        presenter = new LandingPresenter(this);
        configureToolbar();
        setToolbarTitle(getResources().getString(R.string.app_name));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @OnClick(R.id.landing_button)
    void onLandingButtonClick(Button landingButton) {
        //TODO open Details, for now opening categories list
        startCategoriesActivity(SHOULDNT_FINISH_CURRENT);
    }

    @OnClick(R.id.landing_image)
    void onLandingImageClick(ImageView landingImage) {
        startCategoriesActivity(SHOULDNT_FINISH_CURRENT);
    }

    @Override
    public void startCategoriesActivity(boolean shouldFinishCurrent) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
        if(shouldFinishCurrent){
            finish();
        }
    }

    @Override
    public void setUpLandingImage(GoogleImage image) {
        Picasso.with(getContext()).load(image.getFullSizeLink()).into(landingImage);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public View getMainView() {
        return landingMainView;
    }

    @Override
    public void configureToolbar() {
        //do nothing because we don't need the up button here
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }
}
