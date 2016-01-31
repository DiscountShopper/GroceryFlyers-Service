package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;
import io.groceryflyers.fetchers.AbstractProvider;
import io.groceryflyers.fetchers.impl.EyFlyerFetcher;
import io.groceryflyers.models.utils.MappableTo;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class PublicationSet implements MappableTo<Document> {

    public static String MONGO_DOCUMENT_NAME = "publication_set";

    @SerializedName("publication")
    public Publication publication;

    @SerializedName("items")
    public List<PublicationItem> items;

    @SerializedName("banner")
    public String banner;

    @SerializedName("type")
    public String type;

    @Override
    public Document mapToBusinessModel(AbstractProvider p) {
        Document document = new Document();

        document.put("publication", this.publication.mapToBusinessModel(null));
        document.put("banner", this.banner);
        document.put("type", this.type);
        document.put("items", this.items
                .stream()
                .map(x -> x.mapToBusinessModel(null))
                .collect(Collectors.toList())
        );

        return document;
    }
}
