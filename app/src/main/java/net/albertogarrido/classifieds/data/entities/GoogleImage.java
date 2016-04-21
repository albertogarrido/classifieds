package net.albertogarrido.classifieds.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */

public class GoogleImage implements Parcelable {

    private String title;
    private String fullSizeLink;
    private int fullSizeHeight;
    private int fullSizeWidth;
    private String thumbnailLink;
    private int thumbnailHeight;
    private int thumbnailWidth;
    private String categoryName;
    /**
     * Relevance is the indicator of the most viewed categories:
     * The most viewed categoryRelevance = 3
     * The second most viewed categoryRelevance = 2
     * The rest of them and by default categoryRelevance = 1
     */
    private int categoryRelevance = 1;

    public GoogleImage() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullSizeLink() {
        return fullSizeLink;
    }

    public void setFullSizeLink(String fullSizeLink) {
        this.fullSizeLink = fullSizeLink;
    }

    public int getFullSizeHeight() {
        return fullSizeHeight;
    }

    public void setFullSizeHeight(int fullSizeHeight) {
        this.fullSizeHeight = fullSizeHeight;
    }

    public int getFullSizeWidth() {
        return fullSizeWidth;
    }

    public void setFullSizeWidth(int fullSizeWidth) {
        this.fullSizeWidth = fullSizeWidth;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getCategoryRelevance() {
        return categoryRelevance;
    }

    public void setCategoryRelevance(int categoryRelevance) {
        this.categoryRelevance = categoryRelevance;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static final String PROPERTY_FULL_SIZE_LINK = "link";
    public static final String PROPERTY_SEARCH_TERM = "searchTerms";
    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_IMAGE = "image";
    public static final String PROPERTY_FULL_SIZE_HEIGHT = "height";
    public static final String PROPERTY_FULL_SIZE_WIDTH = "width";
    public static final String PROPERTY_THUMBNAIL_HEIGHT = "thumbnailHeight";
    public static final String PROPERTY_THUMBNAIL_WIDTH = "thumbnailWidth";
    public static final String PROPERTY_THUMBNAIL_LINK = "thumbnailLink";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(fullSizeLink);
        dest.writeInt(fullSizeHeight);
        dest.writeInt(fullSizeWidth);
        dest.writeString(thumbnailLink);
        dest.writeInt(thumbnailHeight);
        dest.writeInt(thumbnailWidth);
        dest.writeString(categoryName);
        dest.writeInt(categoryRelevance);
    }
    public static final Parcelable.Creator<GoogleImage> CREATOR = new Parcelable.Creator<GoogleImage>() {
        public GoogleImage createFromParcel(Parcel pc) {
            return new GoogleImage(pc);
        }

        public GoogleImage[] newArray(int size) {
            return new GoogleImage[size];
        }
    };

    public GoogleImage(Parcel in) {
        title = in.readString();
        fullSizeLink = in.readString();
        fullSizeHeight = in.readInt();
        fullSizeWidth = in.readInt();
        thumbnailLink = in.readString();
        thumbnailHeight = in.readInt();
        thumbnailWidth = in.readInt();
        categoryName = in.readString();
        categoryRelevance = in.readInt();
    }

}
