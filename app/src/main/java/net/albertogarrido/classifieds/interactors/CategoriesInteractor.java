package net.albertogarrido.classifieds.interactors;

import android.content.Context;
import android.util.Log;

import net.albertogarrido.classifieds.data.api.client.ImagesService;
import net.albertogarrido.classifieds.data.database.ClassifiedDbHelper;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.data.entities.SearchResult;
import net.albertogarrido.classifieds.util.Config;
import net.albertogarrido.classifieds.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class CategoriesInteractor implements ICategoriesInteractor {

    private static final String TAG = CategoriesInteractor.class.getSimpleName();

    private ClassifiedDbHelper classifiedDbHelper;
    private List<GoogleImage> categoriesList = new ArrayList<>(Config.CATEGORIES.length);

    public CategoriesInteractor(Context context) {
        classifiedDbHelper = new ClassifiedDbHelper(context);
    }

    @Override
    public void fetchCategoriesCovers(final OnCategoriesResultsListener listener) {
        ImagesService imagesService = ImagesService.retrofit.create(ImagesService.class);

        for (int i = 0; i < Config.CATEGORIES.length; i++) {

            Observable<SearchResult> googleImagesCall = imagesService.getImages(
                    Config.API_KEY, //browser
                    Config.CX_CODE,
                    Config.SEARCH_TYPE,
                    Config.CATEGORIES[i],
                    1,
                    Config.DEFAULT_IMAGE_SIZE
            );
            googleImagesCall
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
                                       GoogleImage randomGoogleImage = null;
                                       int randomIndex = Util.getRandomIntWithMax(1);
                                       if (googleImage.getImageList() != null && googleImage.getImageList().size() >
                                               0) {

                                           randomGoogleImage = googleImage.getImageList().get(randomIndex);
                                           getCategoryRelevance(randomGoogleImage, listener);
                                       }
                                   }
                               }

                    );
        }
    }

    @Override
    public void trackClickOnCategory(final String categoryName, final OnCategoriesResultsListener listener) {
        classifiedDbHelper.trackClickOnCategory(categoryName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer newVisitedValue) {
                        if(newVisitedValue < 1){
                            listener.onServerError("Error tracking: " + categoryName + " - " + newVisitedValue);
                        }
                    }
                });
    }


    private void getCategoryRelevance(final GoogleImage googleImage, final OnCategoriesResultsListener listener) {
        classifiedDbHelper.determineCategoryRelevance(googleImage.getCategoryName())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer categoryRelevance) {
                        googleImage.setCategoryRelevance(categoryRelevance);
                        sendGoogleImagesList(googleImage, listener);
                    }
                });
    }

    private void sendGoogleImagesList(GoogleImage googleImage, OnCategoriesResultsListener listener) {
        categoriesList.add(googleImage);
        if (categoriesList.size() == Config.CATEGORIES.length) {
            Collections.sort(categoriesList, new Comparator<GoogleImage>() {
                @Override
                public int compare(GoogleImage googleImage1, GoogleImage googleImage2) {
                    return googleImage2.getCategoryRelevance() - googleImage1.getCategoryRelevance(); // Descending
                }
            });
            listener.onCategoriesCoversReceived(categoriesList);
        }
    }
}