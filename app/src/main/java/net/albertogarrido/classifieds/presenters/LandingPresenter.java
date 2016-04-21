package net.albertogarrido.classifieds.presenters;

import net.albertogarrido.classifieds.R;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.interactors.ILandingInteractor;
import net.albertogarrido.classifieds.interactors.LandingInteractor;
import net.albertogarrido.classifieds.ui.LandingActivity;
import net.albertogarrido.classifieds.ui.view.IClassifiedsView;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public class LandingPresenter implements ILandingPresenter, ILandingInteractor.OnResultsListener {

    private ILandingView view;
    private ILandingInteractor interactor;

    public interface ILandingView extends IClassifiedsView {

        void startCategoriesActivity(boolean shouldFinishCurrent);

        void setUpLandingImage(GoogleImage image);

    }

    public LandingPresenter(ILandingView landingView) {
        this.view = landingView;
        interactor = new LandingInteractor(view.getContext());
        interactor.fetchHistoricalData(this);
    }

    @Override
    public void onCategoryHistoryFound(String mostVisitedCategoryName) {
        interactor.fetchRandomGoogleImageWithCategory(mostVisitedCategoryName, this);
    }

    @Override
    public void onCategoryHistoryNotFound() {
        view.startCategoriesActivity(LandingActivity.SHOULD_FINISH_CURRENT);
    }

    @Override
    public void onGoogleImagesReceived(GoogleImage image) {
        view.setUpLandingImage(image);
    }

    @Override
    public void onGoogleImagesFailed() {
        ((LandingActivity) view).showError(
                view.getMainView(), ((LandingActivity) view).getResources().getString(R.string.error_image_not_found)
        );
        view.startCategoriesActivity(LandingActivity.SHOULD_FINISH_CURRENT);
    }

    @Override
    public void onServerError(String errorText) {
        ((LandingActivity) view).showError(view.getMainView(), errorText);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
