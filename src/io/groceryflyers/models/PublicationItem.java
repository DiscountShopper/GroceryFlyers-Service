package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class PublicationItem {
    @SerializedName("title_fr")
    public String title_fr;

    @SerializedName("title_en")
    public String title_en;

    @SerializedName("description")
    public String description;

    @SerializedName("brand_fr")
    public String brand_fr;

    @SerializedName("brand_en")
    public String brand_en;

    @SerializedName("category_fr")
    public String category_fr;

    @SerializedName("category_en")
    public String category_en;

    @SerializedName("link")
    public String link;

    @SerializedName("image")
    public String image;

    @SerializedName("imageThumbnail")
    public String imageThumb;

    @SerializedName("price")
    public String price;

    @SerializedName("price_unit")
    public String price_unit;

    @SerializedName("keywords")
    public String[] keywords;
}
