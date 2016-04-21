package net.albertogarrido.classifieds.data.api.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.albertogarrido.classifieds.data.entities.GoogleImage;
import net.albertogarrido.classifieds.data.entities.SearchResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class SearchResultDeserializer implements JsonDeserializer<SearchResult> {

    private static final String TAG = SearchResultDeserializer.class.getSimpleName();
    private String categoryName;

    public SearchResult deserialize(JsonElement json, Type type, JsonDeserializationContext deserializeContext)
            throws JsonParseException {

        JsonObject jsonSearchResult = json.getAsJsonObject();
        SearchResult searchResult = parseSearchResult(jsonSearchResult);


        return searchResult;
    }

    private SearchResult parseSearchResult(JsonObject jsonSearchResult) {
        SearchResult searchResult = new SearchResult();
        parseMetaData(jsonSearchResult, searchResult);
        parseImagesResult(jsonSearchResult, searchResult);
        return searchResult;
    }

    private void parseImagesResult(JsonObject jsonSearchResult, SearchResult searchResult) {

        JsonArray itemsArray = jsonSearchResult.get(SearchResult.PROPERTY_ITEMS).getAsJsonArray();

        List<GoogleImage> googleImages = null;

        if (itemsArray != null && !itemsArray.isJsonNull() && itemsArray.size() > 0) {

            googleImages = new ArrayList<>(itemsArray.size());

            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                if (item != null && !item.isJsonNull()) {
                    GoogleImage googleImage = new GoogleImage();


                    googleImage.setFullSizeLink(item.get(GoogleImage.PROPERTY_FULL_SIZE_LINK).getAsString());
                    googleImage.setTitle(item.get(GoogleImage.PROPERTY_TITLE).getAsString());
                    googleImage.setCategoryName(categoryName);

                    JsonObject imageData = item.get(GoogleImage.PROPERTY_IMAGE).getAsJsonObject();
                    if (imageData != null && !imageData.isJsonNull()) {

                        googleImage.setFullSizeHeight(imageData.get(GoogleImage.PROPERTY_FULL_SIZE_HEIGHT).getAsInt());
                        googleImage.setFullSizeWidth(imageData.get(GoogleImage.PROPERTY_FULL_SIZE_WIDTH).getAsInt());

                        googleImage.setThumbnailHeight(imageData.get(GoogleImage.PROPERTY_THUMBNAIL_HEIGHT).getAsInt());
                        googleImage.setThumbnailWidth(imageData.get(GoogleImage.PROPERTY_THUMBNAIL_WIDTH).getAsInt());

                        googleImage.setThumbnailLink(imageData.get(GoogleImage.PROPERTY_THUMBNAIL_LINK).getAsString());
                    }

                    googleImages.add(googleImage);
                }
            }
        }

        searchResult.setImageList(googleImages);

    }

    private void parseMetaData(JsonObject jsonSearchResult, SearchResult searchResult) {
        JsonObject queriesJsonObject = jsonSearchResult.get(SearchResult.PROPERTY_QUERIES).getAsJsonObject();
        if (queriesJsonObject != null && !queriesJsonObject.isJsonNull()) {

            JsonElement nextPageElement = queriesJsonObject.get(SearchResult.PROPERTY_NEXT_PAGE);
            if (existsPage(nextPageElement)) {
                JsonArray nextPageArray = nextPageElement.getAsJsonArray();
                if (nextPageArray != null && !nextPageArray.isJsonNull() && nextPageArray.size() > 0) {
                    searchResult.setNextPage(
                            ((JsonObject) nextPageArray.get(0)).get(SearchResult.PROPERTY_START_INDEX).getAsInt()
                    );
                    searchResult.setTotalResults(
                            ((JsonObject) nextPageArray.get(0)).get(SearchResult.PROPERTY_TOTAL_RESULTS).getAsLong()
                    );
                }
            }

            JsonElement prevPageElement = queriesJsonObject.get(SearchResult.PROPERTY_PREV_PAGE);
            if (existsPage(prevPageElement)) {

                JsonArray prevPageArray = prevPageElement.getAsJsonArray();

                if (prevPageArray != null && !prevPageArray.isJsonNull() && prevPageArray.size() > 0) {
                    searchResult.setPrevPage(
                            ((JsonObject) prevPageArray.get(0)).get(SearchResult.PROPERTY_START_INDEX).getAsInt()
                    );
                }
            }

            JsonElement requestElement = queriesJsonObject.get(SearchResult.PROPERTY_REQUEST);
            if (existsPage(requestElement)) {

                JsonArray requestArray = requestElement.getAsJsonArray();

                if (requestArray != null && !requestArray.isJsonNull() && requestArray.size() > 0) {
                    searchResult.setPrevPage(
                            ((JsonObject) requestArray.get(0)).get(SearchResult.PROPERTY_START_INDEX).getAsInt()
                    );
                    categoryName = ((JsonObject) requestArray.get(0)).get(GoogleImage.PROPERTY_SEARCH_TERM).getAsString();
                }
            }

        }
    }

    private boolean existsPage(JsonElement pageElement) {
        return pageElement != null && !pageElement.isJsonNull();
    }
}

