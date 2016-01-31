package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;
import io.groceryflyers.fetchers.AbstractProvider;
import io.groceryflyers.models.utils.MappableTo;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class PublicationItem implements MappableTo<Document> {
    @SerializedName("id")
    public String id;

    @SerializedName("title_fr")
    public String title_fr;

    @SerializedName("title_en")
    public String title_en;

    @SerializedName("description")
    public String description;

    @SerializedName("brand_fr")
    public String brand_fr;

    @SerializedName("brand_en")
    public String brand_en;

    @SerializedName("category_fr")
    public String category_fr;

    @SerializedName("category_en")
    public String category_en;

    @SerializedName("link")
    public String link;

    @SerializedName("image")
    public String image;

    @SerializedName("imageThumbnail")
    public String imageThumb;

    @SerializedName("price")
    public String price;

    @SerializedName("price_unit")
    public String price_unit;

    @SerializedName("keywords")
    public String[] keywords;

    @SerializedName("banner_code")
    public String banner_code;

    @Override
    public Document mapToBusinessModel(AbstractProvider p) {
        Document document = new Document();

        document.put("identifier", this.id);
        document.put("title_fr", this.title_fr);
        document.put("title_en", this.title_en);
        document.put("description", this.description);
        document.put("brand_fr", this.brand_fr);
        document.put("brand_en", this.brand_en);
        document.put("category_fr", this.category_fr);
        document.put("category_en", this.category_en);
        document.put("link", this.link);
        document.put("image", this.image);
        document.put("imageThumb", this.imageThumb);
        document.put("price", this.price);
        document.put("price_unit", this.price_unit);
        document.put("key_words", Arrays.asList(this.keywords));
        document.put("banner_code", this.banner_code);

        return document;
    }
}
