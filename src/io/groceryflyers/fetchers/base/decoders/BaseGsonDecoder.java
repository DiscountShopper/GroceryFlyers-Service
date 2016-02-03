package io.groceryflyers.fetchers.base.decoders;

import com.google.api.client.json.gson.GsonFactory;
import io.groceryflyers.fetchers.base.AbstractDecoder;

/**
 * Created by jeremiep on 2016-02-02.
 */
public class BaseGsonDecoder extends AbstractDecoder {
    // TODO : add some bad ass custom Gson decoder that uses custom serializers and deserializers
    // for now let's just use anotations
    public GsonFactory getDefaultJSONFactory() {
        return new GsonFactory();
    }
}
