package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;
import io.groceryflyers.fetchers.impl.transcontinental.json.TCProvider;
import io.groceryflyers.models.utils.MappableTo;
import org.bson.Document;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class Publication implements MappableTo<Document> {
    @SerializedName("identifier")
    public String id;

    @SerializedName("effective_date")
    public String date;

    @SerializedName("title")
    public String title;

    @SerializedName("thumbnail")
    public String thumbnail;

    @Override
    public Document mapToBusinessModel(TCProvider p) {

        Document dbObject = new Document();
        dbObject.put("identifier", this.id);
        dbObject.put("date", this.date);
        dbObject.put("title", this.title);
        dbObject.put("thumbnail", this.thumbnail);

        return dbObject;
    }
}
