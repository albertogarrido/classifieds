package net.albertogarrido.classifieds.interactors;

import net.albertogarrido.classifieds.data.entities.GoogleImage;

import java.util.List;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public interface ICategoryItemsInteractor {
    void getCategoryItems(String selectedCategory, OnCategoryItemsResultsListener listener, Integer start);

    interface OnCategoryItemsResultsListener extends MainListener {

        void onCategoryItemsReceived(List<GoogleImage> images);

    }
}