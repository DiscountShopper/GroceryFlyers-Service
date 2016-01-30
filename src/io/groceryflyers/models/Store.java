package io.groceryflyers.models;

import com.google.api.client.util.Key;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class Store {
    @SerializedName("banner_code")
    protected BannerTypes banner;

    @SerializedName("guid")
    protected String guid;

    @SerializedName("address")
    protected String address;

    @SerializedName("city")
    protected String city;

    @SerializedName("region")
    protected String region;

    @SerializedName("region_code")
    protected String region_code;

    @SerializedName("country")
    protected String country;

    @SerializedName("postal_code")
    protected String postal_code;
}
