package net.albertogarrido.classifieds.interactors;

import net.albertogarrido.classifieds.data.entities.GoogleImage;

import java.util.List;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public interface ICategoriesInteractor {

    void fetchCategoriesCovers(final OnCategoriesResultsListener listener);

    void trackClickOnCategory(String categoryName, final OnCategoriesResultsListener listener);


    interface OnCategoriesResultsListener extends MainListener{

        void onCategoriesCoversReceived(List<GoogleImage> images);

    }
}