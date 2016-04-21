package net.albertogarrido.classifieds.presenters;

import android.content.Intent;

import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.interactors.CategoryItemsInteractor;
import net.albertogarrido.classifieds.interactors.ICategoryItemsInteractor;
import net.albertogarrido.classifieds.ui.CategoryItemsActivity;
import net.albertogarrido.classifieds.ui.view.IClassifiedsView;

import java.util.List;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class CategoryItemsPresenter implements ICategoryItemsPresenter, ICategoryItemsInteractor.OnCategoryItemsResultsListener {


    private ICategoryItemsView view;
    private ICategoryItemsInteractor interactor;
    private String selectedCategory;

    public CategoryItemsPresenter(ICategoryItemsView view, Intent intent) {
        this.view = view;
        setSelectedCategory(intent);
        interactor = new CategoryItemsInteractor(view.getContext());
        interactor.getCategoryItems(selectedCategory, this, 1);
    }

    @Override
    public void setSelectedCategory(Intent intent) {
        selectedCategory = intent.getStringExtra(CategoryItemsActivity.CATEGORY_NAME);
    }

    @Override
    public void onCategoryItemsReceived(List<GoogleImage> images) {
        view.populateGridItems(images);
    }

    @Override
    public void onServerError(String errorText) {
        ((CategoryItemsActivity) view).showError(view.getMainView(), errorText);
    }

    public interface ICategoryItemsView extends IClassifiedsView {

        void setupCategoryItemsGrid();

        void startItemDetail(GoogleImage googleImage);

        void populateGridItems(List<GoogleImage> images);
    }

    @Override
    public void onItemSelected(GoogleImage googleImageItem) {
        view.startItemDetail(googleImageItem);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
