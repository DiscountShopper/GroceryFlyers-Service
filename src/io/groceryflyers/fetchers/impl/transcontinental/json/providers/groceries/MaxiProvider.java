package io.groceryflyers.fetchers.impl.transcontinental.json.providers.groceries;

import io.groceryflyers.fetchers.impl.transcontinental.json.EyFlyerProvider;

import java.util.regex.Pattern;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class MaxiProvider extends EyFlyerProvider {
    private static Pattern PRODUCT_TITLE_PATT = Pattern.compile("^\\*?(.*)$");
    private static Pattern PRODUCT_KEYWORDS_PATT = Pattern.compile("^(.*) \\| (.*)$", Pattern.MULTILINE);

    @Override
    public String getProductTitleFrench(String title) {
        return title;
    }

    @Override
    public String getProductTitleEnglish(String title) {
        return "";
    }

}
