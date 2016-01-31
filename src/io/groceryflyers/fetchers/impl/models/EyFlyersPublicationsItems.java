package io.groceryflyers.fetchers.impl.models;

import com.google.api.client.util.Key;
import io.groceryflyers.fetchers.AbstractProvider;
import io.groceryflyers.models.PublicationItem;
import io.groceryflyers.models.utils.MappableTo;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyersPublicationsItems implements MappableTo<PublicationItem> {
    @Key("Id")
    private String identifier;

    @Key("ProductTitle")
    private String title;

    @Key("Description")
    private String description;

    @Key("BrandName")
    private String brandName;

    @Key("BrandName_fr")
    private String brandNameFr;

    @Key("CategoryId")
    private String categoryId;

    @Key("CategoryName")
    private String category;

    @Key("CategoryName_fr")
    private String categoryFr;

    @Key("LinkURL")
    private String link;

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

    @Key("PriceAsNumber")
    private Float price_as_number;

    @Key("PriceUnit")
    private String priceUnit;

    @Key("Keywords")
    private String key_words;

    @Key("BannerCode")
    private String banner_code;

    @Key("EffectiveStartDate")
    private String effective_start_date;

    @Key("EffectiveEndDate")
    private String effective_end_date;

    @Key("PublicationId")
    private String publication_id;

    @Key("PageNumber")
    private Integer page_number;

    @Override
    public PublicationItem mapToBusinessModel(AbstractProvider p) {
        PublicationItem r = new PublicationItem();
        r.identifier = this.identifier;
        r.title_fr = p.getProductTitleFrench(this.title);
        r.title_en = p.getProductTitleEnglish(this.title);

        r.description = this.description;

        r.brand_fr = this.brandNameFr;
        r.brand_en = this.brandName;
        r.category_id = this.categoryId;
        r.category_fr = this.categoryFr;
        r.category_en = this.category;

        r.link = this.link;

        r.image = this.imageUrl;
        r.imageThumb = this.imageThumb;

        r.price = this.price;
        r.price_unit = this.priceUnit;
        r.key_words = p.getKeywords(this.title, this.key_words);
        r.search_key_words = p.getSearchKeywords(this.title, this.key_words);
        r.banner_code = this.banner_code;

        r.effective_start_date = this.effective_start_date;
        r.effective_end_date = this.effective_end_date;

        r.publication_id = this.publication_id;

        r.price_number = this.price_as_number;

        r.page_number = this.page_number;

        return r;
    }

    public static class EyFlyersPublicationItemsList {
        @Key("Products")
        public List<EyFlyersPublicationsItems> products;
    }
}
