package io.groceryflyers.fetchers.impl.transcontinental.json.providers.groceries;

import io.groceryflyers.fetchers.impl.transcontinental.json.EyFlyerProvider;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class ProvigoProvider extends EyFlyerProvider {

    @Override
    public String getProductTitleFrench(String title) {
        return title;
    }

    @Override
    public String getProductTitleEnglish(String title) {
        return "";
    }

}
