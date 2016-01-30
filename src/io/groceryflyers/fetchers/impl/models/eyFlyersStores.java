package io.groceryflyers.fetchers.impl.models;

import com.google.api.client.util.Key;
import oracle.jrockit.jfr.StringConstantPool;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class eyFlyersStores {
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

    @Key("RegionCode")
    private String regionCode;

    @Key("PCZip")
    private String postalCode;
}
