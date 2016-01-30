package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class Category {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("total_products")
    public int totalProducts;
}
