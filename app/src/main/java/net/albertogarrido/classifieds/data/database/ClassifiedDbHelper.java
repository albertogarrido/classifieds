package net.albertogarrido.classifieds.data.database;

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

    private void closeDb(Cursor cursor, SQLiteDatabase db) {
        cursor.close();
//        db.close();
    }
}
