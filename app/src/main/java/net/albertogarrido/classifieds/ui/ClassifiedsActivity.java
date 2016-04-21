package net.albertogarrido.classifieds.ui;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public abstract class ClassifiedsActivity  extends AppCompatActivity {
    public void showError(View view, String errorText) {
        Snackbar.make(view, errorText, Snackbar.LENGTH_LONG).show();
    }

}
