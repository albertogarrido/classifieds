package net.albertogarrido.classifieds.interactors;

import net.albertogarrido.classifieds.data.entities.GoogleImage;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public interface ILandingInteractor {

    interface OnResultsListener extends MainListener{
        void onCategoryHistoryFound(String mostVisitedCategoryName);

        void onCategoryHistoryNotFound();

        void onGoogleImagesReceived(GoogleImage image);

        void onGoogleImagesFailed();
    }

    void fetchHistoricalData(OnResultsListener listener);

    void fetchRandomGoogleImageWithCategory(String category, OnResultsListener listener);


}
