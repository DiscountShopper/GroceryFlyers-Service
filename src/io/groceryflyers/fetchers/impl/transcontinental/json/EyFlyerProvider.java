package io.groceryflyers.fetchers.impl.transcontinental.json;

import org.jsoup.Jsoup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public abstract class EyFlyerProvider extends TCProvider {
    @Override
    public String[] getKeywords(String title, String keywords) {
        if(keywords == null) {
            return new String[] {};
        }

        List<String> results = new LinkedList<>();
        for(String k :  Jsoup.parse(keywords).text().toLowerCase().split(" "))
        {
            if(k.length() > 3 && title.toLowerCase().contains(k) && !results.contains(k)) {
                results.add(k);
            }
        }


        return results.toArray(new String[results.size()]);
    }

    @Override
    public String[] getSearchKeywords(String title, String keywords) {
        if(keywords == null) {
            return new String[] {};
        }

        return keywords.toLowerCase().split(" ");
    }
}
