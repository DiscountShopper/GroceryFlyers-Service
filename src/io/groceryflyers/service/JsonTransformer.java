package io.groceryflyers.service;

/**
 * Created by olivier on 2016-01-30.
 */
import com.google.gson.Gson;
import org.bson.Document;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        if(model instanceof Document) {
            return ((Document)model).toJson();
        }
        return gson.toJson(model);
    }

}
