package io.groceryflyers.fetchers.impl;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import io.groceryflyers.fetchers.AbstractFetcher;
import io.groceryflyers.models.Store;

import java.io.IOException;
import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public abstract class eyFlyerFetcher extends AbstractFetcher {
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
    };

    protected eyFlyersProviders provider;

    @Override
    public List<Store> getStoreNearby(String postalCode) {
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(this.provider.getStoresNearbyPostalCodeUrl(postalCode))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
