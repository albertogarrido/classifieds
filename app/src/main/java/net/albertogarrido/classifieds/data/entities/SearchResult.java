package net.albertogarrido.classifieds.data.entities;

import java.util.List;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class SearchResult {

    private String term;
    private long totalResults;
    private Integer nextPage;
    private Integer prevPage;
    private List<GoogleImage> imageList;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Integer prevPage) {
        this.prevPage = prevPage;
    }

    public List<GoogleImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<GoogleImage> imageList) {
        this.imageList = imageList;
    }

    public static final String PROPERTY_ITEMS = "items";
    public static final String PROPERTY_QUERIES = "queries";
    public static final String PROPERTY_NEXT_PAGE = "nextPage";
    public static final String PROPERTY_PREV_PAGE = "previousPage";
    public static final String PROPERTY_START_INDEX = "startIndex";
    public static final String PROPERTY_TOTAL_RESULTS = "totalResults";
    public static final String PROPERTY_REQUEST = "request";
}