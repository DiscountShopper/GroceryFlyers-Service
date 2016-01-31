package io.groceryflyers.fetchers;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public abstract class AbstractProvider {
    public abstract String getProductTitleFrench(String title);
    public abstract String getProductTitleEnglish(String title);
    public abstract String[] getKeywords(String title, String keywords);
    public abstract String[] getSearchKeywords(String title, String keywords);
}
