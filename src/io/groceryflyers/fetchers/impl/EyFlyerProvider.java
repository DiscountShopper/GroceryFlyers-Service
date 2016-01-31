package io.groceryflyers.fetchers.impl;

import io.groceryflyers.fetchers.AbstractProvider;
import org.jsoup.Jsoup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public abstract class EyFlyerProvider extends AbstractProvider {
    @Override
    public String[] getKeywords(String title, String keywords) {
        if(keywords == null) {
            return new String[] {};
        }

        List<String> results = new LinkedList<>();
        for(String k :  Jsoup.parse(keywords).text().split(" "))
        {
            if(k.length() > 2 && title.toLowerCase().contains(k.toLowerCase())) {
                results.add(k);
            }
        }

        return results.toArray(new String[results.size()]);
    }
}
