package net.albertogarrido.classifieds.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.albertogarrido.classifieds.R;

public class ItemDetailsActivity extends AppCompatActivity {

    public static final String SELECTED_ITEM = "selected_item_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
    }
}
