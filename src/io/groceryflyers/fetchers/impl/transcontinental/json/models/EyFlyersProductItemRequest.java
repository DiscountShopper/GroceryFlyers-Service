package io.groceryflyers.fetchers.impl.transcontinental.json.models;

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

    @SerializedName("banner_code")
    public String banner_code;

    public EyFlyersProductItemRequest(String banner_code, String pubguid, String sguid, String pguid) {
        this.banner_code = banner_code;
        this.pubguid = pubguid;
        this.sguid = sguid;
        this.pguid = pguid;
    }
}
