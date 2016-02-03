package io.groceryflyers.fetchers;

import io.groceryflyers.fetchers.impl.transcontinental.json.EyFlyerFetcher;
import io.groceryflyers.models.Store;

import java.util.Collection;
import java.util.List;

/**
 * Created by jeremiep on 2016-02-02.
 */
public interface IStoreFetcher {
    List<Store> getStoreNearby(EyFlyerFetcher.EyFlyersProviders provider, String postalCode);
    List<Store> getAllStoreNearby(String postalCode);

}
