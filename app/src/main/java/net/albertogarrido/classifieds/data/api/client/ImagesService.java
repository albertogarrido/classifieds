package net.albertogarrido.classifieds.data.api.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.albertogarrido.classifieds.data.api.deserializer.SearchResultDeserializer;
import net.albertogarrido.classifieds.data.entities.SearchResult;
import net.albertogarrido.classifieds.util.Endpoints;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public interface ImagesService {

    @GET(Endpoints.GOOGLE_CUSTOM_SEARCH + Endpoints.VERSION)
    Observable<SearchResult> getImages(
            @Query("key") String key,
            @Query("cx") String cx,
            @Query("searchType") String searchType,
            @Query("q") String q,
            @Query("start") Integer start
    );

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(SearchResult.class, new SearchResultDeserializer())
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.GOOGLE_APIS)
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
