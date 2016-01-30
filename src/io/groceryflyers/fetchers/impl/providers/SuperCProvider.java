package io.groceryflyers.fetchers.impl.providers;

import io.groceryflyers.fetchers.impl.EyFlyerProvider;
import org.jsoup.Jsoup;

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

    @Override
    public String[] getKeywords(String keywords) {
        Matcher match = PRODUCT_KEYWORDS_PATT.matcher(keywords);
        String kwds = "";
        if(match.matches()) {
            kwds = match.group(2);
        }
        kwds = Jsoup.parse(kwds).text();
        return kwds.split(" ");
    }
}
