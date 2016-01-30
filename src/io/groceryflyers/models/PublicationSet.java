package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class PublicationSet {
    @SerializedName("publication")
    public Publication publication;

    @SerializedName("items")
    public List<PublicationItem> items;
}
