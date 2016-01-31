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

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(other == this) return true;
        if(!(other instanceof Category)) return false;

        return ((Category) other).name.equalsIgnoreCase(this.name);
    }
}
