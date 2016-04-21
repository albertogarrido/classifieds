package net.albertogarrido.classifieds.presenters;

import android.content.Intent;

import net.albertogarrido.classifieds.data.entities.GoogleImage;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public interface ICategoryItemsPresenter extends IClassiefiedsPresenter {
    void setSelectedCategory(Intent intent);

    void onItemSelected(GoogleImage googleImageItem);
}

