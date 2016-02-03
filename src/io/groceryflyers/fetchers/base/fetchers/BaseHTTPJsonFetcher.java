package io.groceryflyers.fetchers.base.fetchers;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import io.groceryflyers.fetchers.base.AbstractFetcher;
import io.groceryflyers.fetchers.base.decoders.BaseGsonDecoder;

import java.io.IOException;

/**
 * Created by jeremiep on 2016-02-02.
 */
public abstract class BaseHTTPJsonFetcher extends AbstractFetcher<BaseGsonDecoder> {

    protected BaseHTTPJsonFetcher() {
        super(new BaseGsonDecoder());
    }

    protected HttpRequestFactory getDefaultHttpFactory() {
        return new ApacheHttpTransport().createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setParser(new JsonObjectParser((BaseHTTPJsonFetcher.this).getDefaultDecoder().getDefaultJSONFactory()));
            }
        });
    }
}
