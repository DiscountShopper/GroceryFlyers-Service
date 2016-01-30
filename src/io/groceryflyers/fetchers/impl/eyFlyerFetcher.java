package io.groceryflyers.fetchers.impl;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class eyFlyerFetcher {
    public enum eyFlyersProviders {
        SUPER_C("http://eflyer.metro.ca/SUPRC/SUPRC/"),
        MAXI("http://eflyer.metro.ca/MAXI/MAXI/"),
        IGA("http://eflyer.metro.ca/IGA/IGA/"),
        METRO("http://eflyer.metro.ca/MTR/MTR/"),
        LOBLAWS("http://eflyer.metro.ca/LOB/LOB/");

        private String base_url;
        eyFlyersProviders(String base_url) {
            this.base_url = base_url;
        }

        public String getBaseUrl() {
            return this.base_url;
        }
    };

    
}
