package io.groceryflyers.fetchers.impl.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jeremiep on 2016-01-31.
 */
public class EyFlyersProductItemRequest {
    @SerializedName("pubguid")
    public String pubguid;

    @SerializedName("sguid")
    public String sguid;

    @SerializedName("pguid")
    public String pguid;

    public EyFlyersProductItemRequest(String pubguid, String sguid, String pguid) {
        this.pubguid = pubguid;
        this.sguid = sguid;
        this.pguid = pguid;
    }
}
