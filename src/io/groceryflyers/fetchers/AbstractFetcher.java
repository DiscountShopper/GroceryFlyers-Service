package io.groceryflyers.fetchers;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import io.groceryflyers.fetchers.impl.EyFlyerFetcher;
import io.groceryflyers.models.Publication;
import io.groceryflyers.models.PublicationItem;
import io.groceryflyers.models.Store;

import java.io.IOException;
import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public abstract class AbstractFetcher {
    public abstract List<Store> getStoreNearby(EyFlyerFetcher.EyFlyersProviders provider, String postalCode);
    public abstract List<Store> getAllStoreNearby(String postalCode);

    public abstract List<PublicationItem> getAllPublicationItems(EyFlyerFetcher.EyFlyersProviders provider, String pguid);
    public abstract List<Publication> getAllPublicationByStore(EyFlyerFetcher.EyFlyersProviders provider, String sguid);

    protected HttpRequestFactory getDefaultHttpFactory() {
        return new ApacheHttpTransport().createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setParser(new JsonObjectParser((AbstractFetcher.this).getDefaultJSONFactory()));
            }
        });
    }

    private GsonFactory getDefaultJSONFactory() {
        return new GsonFactory();
    }
}
