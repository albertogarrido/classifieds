package net.albertogarrido.classifieds.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.albertogarrido.classifieds.R;

public class CategoryItemsActivity extends AppCompatActivity {

    public static final String CATEGORY_NAME = "category_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
    }
}
