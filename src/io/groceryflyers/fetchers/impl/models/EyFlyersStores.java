package io.groceryflyers.fetchers.impl.models;

import com.google.api.client.util.Key;
import io.groceryflyers.fetchers.AbstractProvider;
import io.groceryflyers.models.Store;
import io.groceryflyers.models.utils.MappableTo;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyersStores implements MappableTo<Store> {
    @Key("BannerCode")
    private String bannerCode;

    @Key("StoreId")
    private String storeGuid;

    @Key("StoreName")
    private String name;

    @Key("Address1")
    private String address1;

    @Key("Address2")
    private String address2;

    @Key("Phone")
    private String phone;

    @Key("City")
    private String city;

    @Key("Region")
    private String region;

    @Key("Country")
    private String country;

    @Key("RegionCode")
    private String regionCode;

    @Key("PCZip")
    private String postalCode;

    @Key("Distance")
    private float distance;

    @Override
    public Store mapToBusinessModel(AbstractProvider p) {
        Store s = new Store();
        s.name = this.name;
        s.banner = this.bannerCode;
        s.guid = this.storeGuid;
        s.address = this.address1 + this.address2;
        s.city = this.city;
        s.region = this.region;
        s.region_code = this.regionCode;
        s.country = this.country;
        s.postal_code = this.postalCode;
        s.distance = this.distance;

        return s;
    }

    public static class eyFlyersStoresList {
        @Key("Stores")
        public List<EyFlyersStores> storeList;
    }
}
