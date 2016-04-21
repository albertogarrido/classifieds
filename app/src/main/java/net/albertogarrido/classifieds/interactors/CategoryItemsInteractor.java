package net.albertogarrido.classifieds.interactors;

import android.content.Context;
import android.util.Log;

import net.albertogarrido.classifieds.data.api.client.ImagesService;
import net.albertogarrido.classifieds.data.database.ClassifiedDbHelper;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.data.entities.SearchResult;
import net.albertogarrido.classifieds.util.Parameters;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class CategoryItemsInteractor implements ICategoryItemsInteractor {


    private static final String TAG = CategoryItemsInteractor.class.getSimpleName();
    private List<GoogleImage> results = new ArrayList<>(30);
    private ClassifiedDbHelper classifiedDbHelper;

    public CategoryItemsInteractor(Context context) {
        classifiedDbHelper = new ClassifiedDbHelper(context);
    }

    @Override
    public void getCategoryItems
            (final String selectedCategory, final OnCategoryItemsResultsListener listener, Integer start) {

        ImagesService imagesService = ImagesService.retrofit.create(ImagesService.class);
        Observable<SearchResult> googleImagesObservable = imagesService.getImages(
                Parameters.API_KEY, //browser
                Parameters.CX_CODE,
                Parameters.SEARCH_TYPE,
                selectedCategory,
                start,
                Parameters.DEFAULT_IMAGE_SIZE
        );

        googleImagesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResult>() {

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   Log.e(TAG, "failure: " + e.getMessage());
                                   listener.onServerError(e.getMessage());
                               }

                               @Override
                               public void onNext(SearchResult googleImage) {
                                   Log.d(TAG, "success");
                                   combineResults(googleImage, selectedCategory, listener);
                               }
                           }

                );
    }


    private void combineResults(SearchResult searchResult, String selectedCategory, OnCategoryItemsResultsListener
            listener) {
        results.addAll(searchResult.getImageList());
        if (results.size() < 30) {
            getCategoryItems(selectedCategory, listener, searchResult.getNextPage());
        } else {
            listener.onCategoryItemsReceived(results);
        }
    }

    @Override
    public void trackClickOnItem(final String fullSizeLink, final OnCategoryItemsResultsListener listener) {
        classifiedDbHelper.trackClickOnItem(fullSizeLink)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer newVisitedValue) {
                        if (newVisitedValue < 1) {
                            listener.onServerError("Error tracking: " + fullSizeLink + " - " + newVisitedValue);
                        }
                    }
                });
    }
}
