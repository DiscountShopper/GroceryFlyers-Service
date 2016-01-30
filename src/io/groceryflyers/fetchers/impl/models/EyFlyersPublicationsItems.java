package io.groceryflyers.fetchers.impl.models;

import com.google.api.client.util.Key;
import io.groceryflyers.fetchers.AbstractProvider;
import io.groceryflyers.models.PublicationItem;
import io.groceryflyers.models.utils.MappableTo;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyersPublicationsItems implements MappableTo<PublicationItem> {
    @Key("ProductTitle")
    private String title;

    @Key("Description")
    private String description;

    @Key("BrandName")
    private String brandName;

    @Key("BrandName_fr")
    private String brandNameFr;

    @Key("CategoryName")
    private String category;

    @Key("CategoryName_fr")
    private String categoryFr;

    @Key("LinkURL")
    private String link;

    @Key("EffectiveStartDate")
    private String effectiveStartDate;

    @Key("EffectiveEndDate")
    private String effectiveEndDate;

    @Key("ImageURL")
    private String imageUrl;

    @Key("ProductThumbnailUrl")
    private String imageThumb;

    @Key("Promotion")
    private String promotion;

    @Key("Rebate")
    private String rebate;

    @Key("Price")
    private String price;

    @Key("PriceUnit")
    private String priceUnit;

    @Key("Keywords")
    private String keywords;

    @Override
    public PublicationItem mapToBusinessModel(AbstractProvider p) {
        PublicationItem r = new PublicationItem();
        r.title = this.title;
        return null;
    }
}
