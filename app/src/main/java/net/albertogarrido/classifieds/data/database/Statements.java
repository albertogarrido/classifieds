package net.albertogarrido.classifieds.data.database;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public class Statements {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String SEPARATOR = ",";
    public static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + ClassifiedContract.CategoryEntry.TABLE_NAME + " (" +
                    ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME + TEXT_TYPE + " PRIMARY KEY" + SEPARATOR +
                    ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES+ INTEGER_TYPE +
                    " )";
    public static final String SQL_CREATE_IMAGES =
            "CREATE TABLE " + ClassifiedContract.ImagesEntry.TABLE_NAME + " (" +
                    ClassifiedContract.ImagesEntry.COLUMN_NAME_IMAGE_LINK + TEXT_TYPE + " PRIMARY KEY" + SEPARATOR +
                    ClassifiedContract.ImagesEntry.COLUMN_NAME_VISITED_TIMES + INTEGER_TYPE +
                    " )";
//    public static final String SQL_CREATE_QUOTES =
//            "CREATE TABLE " + ClassifiedContract.QuotesEntry.TABLE_NAME + " (" +
//                    ClassifiedContract.QuotesEntry.COLUMN_NAME_QUOTE + TEXT_TYPE + " PRIMARY KEY" + SEPARATOR +
//                    ClassifiedContract.QuotesEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + SEPARATOR +
//                    ClassifiedContract.QuotesEntry.COLUMN_NAME_AUTHOR + INTEGER_TYPE +
//                    " )";

    public static final String SQL_DELETE_CATEGORIES
            = "DROP TABLE IF EXISTS " + ClassifiedContract.CategoryEntry.TABLE_NAME;
    public static final String SQL_DELETE_IMAGES
            = "DROP TABLE IF EXISTS " + ClassifiedContract.ImagesEntry.TABLE_NAME;
//    public static final String SQL_DELETE_QUOTES
//            = "DROP TABLE IF EXISTS " + ClassifiedContract.ImagesEntry.TABLE_NAME;


}