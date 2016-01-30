package io.groceryflyers.fetchers.impl;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import io.groceryflyers.fetchers.AbstractFetcher;
import io.groceryflyers.fetchers.impl.models.EyFlyersStores;
import io.groceryflyers.fetchers.impl.providers.MetroProvider;
import io.groceryflyers.models.Store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyerFetcher extends AbstractFetcher {
    public enum EyFlyersProviders {
        SUPER_C("http://eflyer.metro.ca/SUPRC/SUPRC", null),
        MAXI("http://eflyer.metro.ca/MAXI/MAXI", null),
        IGA("http://eflyer.metro.ca/IGA/IGA", null),
        METRO("http://eflyer.metro.ca/MTR/MTR", new MetroProvider()),
        LOBLAWS("http://eflyer.metro.ca/LOB/LOB", null);

        private String base_url;
        private EyFlyerProvider provider;
        EyFlyersProviders(String base_url, EyFlyerProvider provider) {
            this.base_url = base_url;
            this.provider = provider;
        }

        public String getBaseUrl() {
            return this.base_url;
        }

        public String getStoresNearbyPostalCodeUrl(String postalCode) {
            return this.getBaseUrl().concat("/fr/Landing/GetClosestStoresByPostalCode?" +
                    "orgCode=9999&" +
                    "bannerCode=9999&" +
                    "countryCode=CA&" +
                    "postalCode=" + postalCode + "&" +
                    "culture=fr");
        }

        public EyFlyerProvider getProvider() { return this.provider; }

        public static EyFlyersProviders getProviderFromString(String provider) {
            switch(provider){
                case "SUPERC":
                    return SUPER_C;
                case "MAXI":
                    return MAXI;
                case "IGA":
                    return IGA;
                case "METRO":
                    return METRO;
                case "LOBLAWS":
                    return LOBLAWS;
                default:
                    return null;
            }
        }
    };

    @Override
    public List<Store> getStoreNearby(EyFlyersProviders provider, String postalCode) {
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(provider.getStoresNearbyPostalCodeUrl(postalCode))
            );

            List<EyFlyersStores> stores = req.execute().parseAs(EyFlyersStores.eyFlyersStoresList.class).storeList;
            return stores.stream().map( item -> (
                    (Function<EyFlyersStores, Store>) map -> {
                        return map.mapToBusinessModel(provider.getProvider());
                    }).apply(item)
            ).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Store> getAllStoreNearby(String postalCode) {
        List<Store> allStores = new ArrayList<>();

        for(EyFlyersProviders provider: EyFlyersProviders.values()){
            allStores.addAll(getStoreNearby(provider, postalCode));
        }

        return allStores;
    }
}
