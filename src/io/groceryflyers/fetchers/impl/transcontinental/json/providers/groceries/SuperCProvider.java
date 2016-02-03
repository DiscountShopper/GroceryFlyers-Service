package io.groceryflyers.fetchers.impl.transcontinental.json.providers.groceries;

import io.groceryflyers.fetchers.impl.transcontinental.json.EyFlyerProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class SuperCProvider extends EyFlyerProvider {
    private static Pattern PRODUCT_TITLE_PATT = Pattern.compile("^\\*?(.*)$");
    private static Pattern PRODUCT_KEYWORDS_PATT = Pattern.compile("^(.*) \\| (.*)$", Pattern.MULTILINE);

    @Override
    public String getProductTitleFrench(String title) {
        Matcher match = PRODUCT_TITLE_PATT.matcher(title);
        if(match.matches()) {
            return match.group(1);
        } else {
            return title;
        }
    }

    @Override
    public String getProductTitleEnglish(String title) {
        Matcher match = PRODUCT_TITLE_PATT.matcher(title);
        if(match.matches()) {
            return match.group(1);
        } else {
            return title;
        }
    }

}
