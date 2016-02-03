package io.groceryflyers.fetchers;

import io.groceryflyers.fetchers.impl.transcontinental.json.EyFlyerFetcher;
import io.groceryflyers.models.Category;
import io.groceryflyers.models.Publication;
import io.groceryflyers.models.PublicationItem;
import io.groceryflyers.models.PublicationSet;

import java.util.List;

/**
 * Created by jeremiep on 2016-02-02.
 */
public interface IProductFetcher {

    List<PublicationItem> getAllPublicationItems(EyFlyerFetcher.EyFlyersProviders provider, String pguid);
    List<Publication> getAllPublicationByStore(EyFlyerFetcher.EyFlyersProviders provider, String sguid);

    List<PublicationSet> getAllPublicationSetsByStore(EyFlyerFetcher.EyFlyersProviders provider, String sguid);
    List<Category> getAllCategoriesByPublication(EyFlyerFetcher.EyFlyersProviders provider, String pguid);
}
