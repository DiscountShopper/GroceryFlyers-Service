package io.groceryflyers.fetchers.impl.models;

import com.google.api.client.util.Key;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyersPublicationsItems {
    @Key("ProductTitle")
    private String title;

    @Key("Description")
    private String description;

    @Key("ImageURL")
    private String imageUrl;

    @Key("ProductThumbnailUrl")
    private String imageThumb;

    @Key("Price")
    private String price;

    @Key("PriceUnit")
    private String priceUnit;
}
