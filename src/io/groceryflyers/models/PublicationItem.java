package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class PublicationItem {
    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("brand")
    public String brand;

    @SerializedName("category")
    public String category;

    @SerializedName("link")
    public String link;

    @SerializedName("image")
    public String image;

    @SerializedName("imageThumbnail")
    public String imageThumb;

    @SerializedName("price")
    public String price;

    @SerializedName("keywords")
    public List<String> keywords;
}
