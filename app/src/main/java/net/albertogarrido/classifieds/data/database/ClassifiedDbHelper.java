package net.albertogarrido.classifieds.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class ClassifiedDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Classifieds.db";
    private static final String TAG = ClassifiedDbHelper.class.getSimpleName();

    public ClassifiedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Statements.SQL_CREATE_CATEGORIES);
        db.execSQL(Statements.SQL_CREATE_IMAGES);
//        db.execSQL(Statements.SQL_CREATE_QUOTES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion) {
            case 1:
                return;
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            Log.e(TAG, "Error reading from the database", ex);
                        }
                    }
                });
    }

    public Observable<String> getMostViewedCategory() {
        return makeObservable(getMostViewedCategory(getReadableDatabase())).subscribeOn(Schedulers.io());
    }

    private Callable<String> getMostViewedCategory(final SQLiteDatabase db) {
        return new Callable<String>() {
            @Override
            public String call() {
                String[] projection = {
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME,
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES
                };

                Cursor cursor = db.query(
                        ClassifiedContract.CategoryEntry.TABLE_NAME,    // table
                        projection,                                     // columns to query
                        null,                                           // where cols
                        null,                                           // where values
                        null,                                           // group
                        null,                                           // filter
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES + " DESC"   // sort
                );
                int resultCount = cursor.getCount();

                String mostViewedCategory = "";
                if (resultCount > 0) {
                    cursor.moveToFirst();
                    mostViewedCategory = cursor.getString(
                            cursor.getColumnIndex(ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME)
                    );
                }
                closeDb(cursor,db);
                return mostViewedCategory;
            }
        };

    }


    public Observable<Integer> determineCategoryRelevance(String categoryName) {
        return makeObservable(
                determineCategoryRelevance(getReadableDatabase(), categoryName))
                .subscribeOn(Schedulers.io()
                );

    }

    private Callable<Integer> determineCategoryRelevance(final SQLiteDatabase db, final String categoryName) {
        return new Callable<Integer>() {
            @Override
            public Integer call() {
                String[] projection = {
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME,
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES
                };

                Cursor cursor = db.query(
                        ClassifiedContract.CategoryEntry.TABLE_NAME,    // table
                        projection,                                     // columns to query
                        null,                                           // where cols
                        null,                                           // where values
                        null,                                           // group
                        null,                                           // filter
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES + " DESC"   // sort
                );
                int resultCount = cursor.getCount();


                if (resultCount > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < resultCount; i++) {
                        String current = cursor.getString(
                                cursor.getColumnIndex(ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME)
                        );
                        if (current.equals(categoryName)) {
                            closeDb(cursor, db);
                            if (i == 0) return 3;
                            else if (i == 1) return 2;
                            else return 1;
                        }
                        cursor.moveToNext();
                    }
                }
                closeDb(cursor, db);
                return 1;
            }
        };
    }

    /**
     * Add +1 to a category count
     * @param categoryName
     * @return the new count of clicks or -1 if error
     */
    public Observable<Integer> trackClickOnCategory(String categoryName) {
        return makeObservable(
                trackClickOnCategory(getReadableDatabase(), categoryName))
                .subscribeOn(Schedulers.io()
                );
    }

    private Callable<Integer> trackClickOnCategory(final SQLiteDatabase db, final String categoryName) {
        return new Callable<Integer>() {
            @Override
            public Integer call() {
                String[] projection = {
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME,
                        ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES
                };

                String whereClause = ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME + " = ? ";
                String[] whereValues = new String[]{ categoryName };

                Cursor cursor = db.query(
                        ClassifiedContract.CategoryEntry.TABLE_NAME,                    // table
                        projection,                                                     // columns to query
                        whereClause,                                                      // where cols
                        whereValues,                                                    // where values
                        null,                                                           // group
                        null,                                                           // filter
                        null                                                            // sort
                );

                int resultCount = cursor.getCount();
                int newVisitedValue;
                ContentValues values = new ContentValues();
                values.put(ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME, categoryName);
                if(resultCount < 1){
                    values.put(ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES, 1);
                    newVisitedValue = insertTrack(db, values, ClassifiedContract.CategoryEntry.TABLE_NAME);
                } else {
                    cursor.moveToFirst();
                    int currentVisitedValue = cursor.getInt(
                            cursor.getColumnIndex(ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES)
                    );
                    values.put(ClassifiedContract.CategoryEntry.COLUMN_NAME_VISITED_TIMES, currentVisitedValue+1);
                    String whereClauseUpdate = ClassifiedContract.CategoryEntry.COLUMN_NAME_CATEGORY_NAME + "=?";
                    String[] whereValuesUpdate = { categoryName };
                    newVisitedValue = updateTrack(
                            db, values, whereClauseUpdate, whereValuesUpdate, ClassifiedContract.CategoryEntry.TABLE_NAME
                    );
                    if(newVisitedValue > 0) newVisitedValue = currentVisitedValue+1;

                }
                closeDb(cursor, db);
                return newVisitedValue;
            }
        };
    }

    private int updateTrack(SQLiteDatabase db,ContentValues contentValues, String whereClause,
                            String[] whereValues, String table) {
        int rowsAffected = db.update(
                table,
                contentValues,
                whereClause,
                whereValues
        );

        if(rowsAffected != 1) return -1;
        return rowsAffected;
    }

    private int insertTrack(SQLiteDatabase db, ContentValues contentValues, String table) {
        long newRowId;
        newRowId = db.insert(
                table,
                null,
                contentValues);
        if(newRowId > -1) return 1;
        return (int) newRowId;
    }

    private void closeDb(Cursor cursor, SQLiteDatabase db) {
        cursor.close();
    }

    public Observable<Integer> trackClickOnItem(String fullSizeLink) {
        return makeObservable(
                trackClickOnItem(getReadableDatabase(), fullSizeLink))
                .subscribeOn(Schedulers.io()
                );
    }

    private Callable<Integer> trackClickOnItem(final SQLiteDatabase db, final String fullSizeLink) {
        return new Callable<Integer>() {
            @Override
            public Integer call() {
                String[] projection = {
                        ClassifiedContract.ImagesEntry.COLUMN_NAME_IMAGE_LINK,
                        ClassifiedContract.ImagesEntry.COLUMN_NAME_VISITED_TIMES
                };

                String whereClause = ClassifiedContract.ImagesEntry.COLUMN_NAME_IMAGE_LINK + " = ? ";
                String[] whereValues = new String[]{ fullSizeLink };

                Cursor cursor = db.query(
                        ClassifiedContract.ImagesEntry.TABLE_NAME,                    // table
                        projection,                                                     // columns to query
                        whereClause,                                                      // where cols
                        whereValues,                                                    // where values
                        null,                                                           // group
                        null,                                                           // filter
                        null                                                            // sort
                );

                int resultCount = cursor.getCount();
                int newVisitedValue;
                ContentValues values = new ContentValues();
                values.put(ClassifiedContract.ImagesEntry.COLUMN_NAME_IMAGE_LINK, fullSizeLink);
                if(resultCount < 1){
                    values.put(ClassifiedContract.ImagesEntry.COLUMN_NAME_VISITED_TIMES, 1);
                    newVisitedValue = insertTrack(db, values, ClassifiedContract.ImagesEntry.TABLE_NAME);
                } else {
                    cursor.moveToFirst();
                    int currentVisitedValue = cursor.getInt(
                            cursor.getColumnIndex(ClassifiedContract.ImagesEntry.COLUMN_NAME_VISITED_TIMES)
                    );
                    values.put(ClassifiedContract.ImagesEntry.COLUMN_NAME_VISITED_TIMES, currentVisitedValue+1);
                    String whereClauseUpdate = ClassifiedContract.ImagesEntry.COLUMN_NAME_IMAGE_LINK + "=?";
                    String[] whereValuesUpdate = { fullSizeLink };
                    newVisitedValue = updateTrack(
                            db, values, whereClauseUpdate, whereValuesUpdate, ClassifiedContract.ImagesEntry.TABLE_NAME
                    );
                    if(newVisitedValue > 0) newVisitedValue = currentVisitedValue+1;

                }
                closeDb(cursor, db);
                return newVisitedValue;
            }
        };
    }
}