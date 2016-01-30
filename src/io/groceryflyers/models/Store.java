package io.groceryflyers.models;

import com.google.api.client.util.Key;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class Store {
    /* TODO : put these attr private ... lazy */
    @SerializedName("name")
    public String name;

    @SerializedName("banner_code")
    public String banner;

    @SerializedName("guid")
    public String guid;

    @SerializedName("address")
    public String address;

    @SerializedName("city")
    public String city;

    @SerializedName("region")
    public String region;

    @SerializedName("region_code")
    public String region_code;

    @SerializedName("country")
    public String country;

    @SerializedName("postal_code")
    public String postal_code;

    @SerializedName("distance")
    public float distance;
}
