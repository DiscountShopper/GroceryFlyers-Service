package io.groceryflyers.fetchers.impl.models;

import com.google.api.client.util.Key;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyersPublications {
    @Key("PubId")
    private String guid;

    @Key("EffectiveDate")
    private String effectiveDates;

    @Key("Title")
    private String title;

    @Key("Thumbnail")
    private String thumbnail;
}
