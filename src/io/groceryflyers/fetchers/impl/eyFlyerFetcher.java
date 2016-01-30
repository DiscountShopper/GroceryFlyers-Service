package io.groceryflyers.fetchers.impl;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import io.groceryflyers.fetchers.AbstractFetcher;
import io.groceryflyers.fetchers.impl.models.eyFlyersStores;
import io.groceryflyers.models.Store;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class eyFlyerFetcher extends AbstractFetcher {
    public enum eyFlyersProviders {
        SUPER_C("http://eflyer.metro.ca/SUPRC/SUPRC"),
        MAXI("http://eflyer.metro.ca/MAXI/MAXI"),
        IGA("http://eflyer.metro.ca/IGA/IGA"),
        METRO("http://eflyer.metro.ca/MTR/MTR"),
        LOBLAWS("http://eflyer.metro.ca/LOB/LOB");

        private String base_url;
        eyFlyersProviders(String base_url) {
            this.base_url = base_url;
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

        public static eyFlyersProviders getProviderFromString(String provider) {
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

    protected eyFlyersProviders provider;

    public eyFlyerFetcher(eyFlyersProviders provider) {
        this.provider = provider;
    }

    @Override
    public List<Store> getStoreNearby(String postalCode) {
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(this.provider.getStoresNearbyPostalCodeUrl(postalCode))
            );

            List<eyFlyersStores> stores = req.execute().parseAs(eyFlyersStores.eyFlyersStoresList.class).storeList;
            return stores.stream().map(
                    ((Function<eyFlyersStores, Store>) eyFlyersStores::mapToBusinessModel)::apply
            ).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
