package net.albertogarrido.classifieds.presenters;

import android.content.Intent;

import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.ui.ItemDetailsActivity;
import net.albertogarrido.classifieds.ui.view.IClassifiedsView;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public class ItemDetailsPresenter implements IItemDetailsPresenter {

    private IItemDetailsView view;

    public interface IItemDetailsView extends IClassifiedsView {

        void setUpData(GoogleImage googleImageInformation);
    }

    public ItemDetailsPresenter(IItemDetailsView view, Intent intent) {
        this.view = view;
        this.view.setUpData(getGoogleImageInformation(intent));
    }

    private GoogleImage getGoogleImageInformation(Intent intent) {
        return intent.getParcelableExtra(ItemDetailsActivity.SELECTED_ITEM);

    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
