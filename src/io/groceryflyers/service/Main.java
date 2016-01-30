package io.groceryflyers.service;

import io.groceryflyers.fetchers.impl.EyFlyerFetcher;

import java.util.regex.Pattern;

import static spark.Spark.*;

/**
 * Created by olivier on 2016-01-30.
 */
public class Main {
    private final static int PORT = 1337;
    private final static Pattern POSTAL_CODE_PATTERN = Pattern.compile("[a-zA-Z][0-9][a-zA-Z][0-9][a-zA-Z][0-9]");

    public static void main(String[] args) {

        port(PORT);

        before("/api/:bannerCode/:postalCode", (request, response) -> {
            boolean validParameters = true;

            validParameters = POSTAL_CODE_PATTERN.matcher(request.params(":postalCode")).matches();
            validParameters = EyFlyerFetcher.eyFlyersProviders.getProviderFromString(request.params(":bannerCode")) != null;

            if (!validParameters) {
                halt(400, "Invalid parameters");
            }
        });
        get("/api/:bannerCode/:postalCode", (req, res) ->  {
            EyFlyerFetcher fetcher = new EyFlyerFetcher(EyFlyerFetcher.eyFlyersProviders.getProviderFromString(req.params(":bannerCode")));
            return fetcher.getStoreNearby(req.params(":postalCode"));
        }, new JsonTransformer());

        get("/api/publications/:store", (req, res) ->  {
            //TODO: Get the latest stuff from je
            return new Object();

        }, new JsonTransformer());
    }
}
