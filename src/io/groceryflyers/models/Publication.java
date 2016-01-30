package io.groceryflyers.models;

import com.google.gson.annotations.SerializedName;
import com.mongodb.BasicDBObject;
import io.groceryflyers.fetchers.AbstractProvider;
import io.groceryflyers.models.utils.MappableTo;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class Publication implements MappableTo<BasicDBObject> {
    @SerializedName("identifier")
    public String id;

    @SerializedName("effective_date")
    public String date;

    @SerializedName("title")
    public String title;

    @SerializedName("thumbnail")
    public String thumbnail;

    @Override
    public BasicDBObject mapToBusinessModel(AbstractProvider p) {

        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("identifier", this.id);
        dbObject.put("date", this.date);
        dbObject.put("title", this.title);
        dbObject.put("thumbnail", this.thumbnail);

        return dbObject;
    }
}
