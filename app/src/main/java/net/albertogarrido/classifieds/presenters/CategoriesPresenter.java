package net.albertogarrido.classifieds.presenters;

import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.interactors.CategoriesInteractor;
import net.albertogarrido.classifieds.interactors.ICategoriesInteractor;
import net.albertogarrido.classifieds.ui.CategoriesActivity;
import net.albertogarrido.classifieds.ui.view.IClassifiedsView;

import java.util.List;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public class CategoriesPresenter implements ICategoriesPresenter, ICategoriesInteractor.OnCategoriesResultsListener {

    private ICategoriesView view;
    private ICategoriesInteractor interactor;

    public interface ICategoriesView extends IClassifiedsView {
        void setupCategoriesGrid();

        void startCategoryItemsActivityForCategory(String categoryName);

        void populateGridItems(List<GoogleImage> images);
    }

    public CategoriesPresenter(ICategoriesView view) {
        this.view = view;
        interactor = new CategoriesInteractor(view.getContext());
        interactor.fetchCategoriesCovers(this);
    }

    @Override
    public void onCategorySelected(String categoryName) {
        interactor.trackClickOnCategory(categoryName, this);
        view.startCategoryItemsActivityForCategory(categoryName);
    }

    @Override
    public void onCategoriesCoversReceived(List<GoogleImage> images) {
        view.setupCategoriesGrid();
        view.populateGridItems(images);
    }

    @Override
    public void onServerError(String errorText) {
        ((CategoriesActivity) view).showError(view.getMainView(), errorText);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
