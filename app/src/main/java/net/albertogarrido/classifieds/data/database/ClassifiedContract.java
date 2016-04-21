package net.albertogarrido.classifieds.data.database;

import android.provider.BaseColumns;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public class ClassifiedContract {

    public ClassifiedContract() {
    }

    public static abstract class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_CATEGORY_NAME = "category_name";
        public static final String COLUMN_NAME_VISITED_TIMES = "visited_times";
    }

    public static abstract class ImagesEntry implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_IMAGE_LINK = "image_link";
        public static final String COLUMN_NAME_VISITED_TIMES = "visited_times";
    }

    public static abstract class QuotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "quotes";
        public static final String COLUMN_NAME_QUOTE = "quote";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
