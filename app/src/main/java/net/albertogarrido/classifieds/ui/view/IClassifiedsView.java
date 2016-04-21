package net.albertogarrido.classifieds.ui.view;

import android.content.Context;
import android.view.View;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public interface IClassifiedsView {

    Context getContext();

    View getMainView();

    void configureToolbar();

    void setToolbarTitle(String title);
}
