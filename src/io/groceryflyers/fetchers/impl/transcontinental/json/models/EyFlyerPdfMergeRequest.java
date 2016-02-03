package io.groceryflyers.fetchers.impl.transcontinental.json.models;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by olivier on 2016-01-31.
 */
public class EyFlyerPdfMergeRequest {
    @SerializedName("products")
    public List<EyFlyersProductItemRequest> products;
}
