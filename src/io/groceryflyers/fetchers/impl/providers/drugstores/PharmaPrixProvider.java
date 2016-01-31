package io.groceryflyers.fetchers.impl.providers.drugstores;

import io.groceryflyers.fetchers.impl.EyFlyerProvider;
import org.jsoup.Jsoup;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class PharmaPrixProvider extends EyFlyerProvider {
    @Override
    public String getProductTitleFrench(String title) {
        return title;
    }

    @Override
    public String getProductTitleEnglish(String title) {
        return "";
    }

}
