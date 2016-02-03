package io.groceryflyers.fetchers.impl.transcontinental.json.models;

import com.google.api.client.util.Key;
import io.groceryflyers.fetchers.impl.transcontinental.json.TCProvider;
import io.groceryflyers.models.Publication;
import io.groceryflyers.models.utils.MappableTo;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyersPublications implements MappableTo<Publication> {
    @Key("PubId")
    private String guid;

    @Key("EffectiveDate")
    private String effectiveDates;

    @Key("Title")
    private String title;

    @Key("Thumbnail")
    private String thumbnail;

    private List<EyFlyersPublicationsItems> items;

    @Override
    public Publication mapToBusinessModel(TCProvider p) {
        Publication r = new Publication();
        r.id = this.guid;
        r.date = this.effectiveDates;
        r.title = this.title;
        r.thumbnail = this.thumbnail;

        return r;
    }

    public static class EyFlyersPublicationsList {
        @Key("Publications")
        public List<EyFlyersPublications> publications;
    }
}
