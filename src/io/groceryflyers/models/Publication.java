package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class Publication {
    @SerializedName("identifier")
    public String id;

    @SerializedName("effective_date")
    public String date;

    @SerializedName("title")
    public String title;

    @SerializedName("thumbnail")
    public String thumbnail;
}
