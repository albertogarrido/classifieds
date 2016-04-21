package net.albertogarrido.classifieds.interactors;

import android.content.Context;
import android.util.Log;

import net.albertogarrido.classifieds.data.api.client.ImagesService;
import net.albertogarrido.classifieds.data.database.ClassifiedDbHelper;
import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.data.entities.SearchResult;
import net.albertogarrido.classifieds.util.Parameters;
import net.albertogarrido.classifieds.util.Util;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public class LandingInteractor implements ILandingInteractor {

    private static final String TAG = LandingInteractor.class.getSimpleName();

    private ClassifiedDbHelper classifiedDbHelper;

    public LandingInteractor(Context context) {
        classifiedDbHelper = new ClassifiedDbHelper(context);
    }

    @Override
    public void fetchHistoricalData(final OnResultsListener listener) {

        classifiedDbHelper.getMostViewedCategory()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String mostViewedCategory) {
                        if (!mostViewedCategory.equals("")) {
                            listener.onCategoryHistoryFound(mostViewedCategory);
                        } else {
                            listener.onCategoryHistoryNotFound();
                        }
                    }
                });
    }

    @Override
    public void fetchRandomGoogleImageWithCategory(String category, final OnResultsListener listener) {

        ImagesService imagesService = ImagesService.retrofit.create(ImagesService.class);
        Observable<SearchResult> googleImagesCall = imagesService.getImages(
                Parameters.API_KEY, //browser
                Parameters.CX_CODE,
                Parameters.SEARCH_TYPE,
                category,
                1
        );

        // FIXME: 4/21/16 this subscription it's not (shouldn't be) necessary now
        Subscription googleImagesSubscription = googleImagesCall
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
                                   if (googleImage.getImageList() != null && googleImage.getImageList().size() > 0) {
                                       randomGoogleImage = googleImage.getImageList().get(randomIndex);
                                       listener.onGoogleImagesReceived(randomGoogleImage);
                                   } else {
                                       listener.onGoogleImagesFailed();
                                   }
                               }
                           }

                );
    }
}
