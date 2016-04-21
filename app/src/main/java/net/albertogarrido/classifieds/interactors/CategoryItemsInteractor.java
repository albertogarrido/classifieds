package net.albertogarrido.classifieds.interactors;

import android.content.Context;
import android.util.Log;

import net.albertogarrido.classifieds.data.api.client.ImagesService;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.data.entities.SearchResult;
import net.albertogarrido.classifieds.util.Parameters;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class CategoryItemsInteractor implements ICategoryItemsInteractor{


    private static final String TAG = CategoryItemsInteractor.class.getSimpleName();
    private List<GoogleImage> results = new ArrayList<>(30);

    public CategoryItemsInteractor(Context context) {

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
                start
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
                                   combine(googleImage, selectedCategory, listener);
                               }
                           }

                );
    }

    private void combine(SearchResult searchResult, String selectedCategory, OnCategoryItemsResultsListener listener) {
        results.addAll(searchResult.getImageList());
        if(results.size() < 30){
            getCategoryItems(selectedCategory, listener, searchResult.getNextPage());
        } else {
            listener.onCategoryItemsReceived(results);
        }
    }
}
