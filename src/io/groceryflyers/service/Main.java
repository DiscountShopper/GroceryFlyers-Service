package io.groceryflyers.service;

import io.groceryflyers.datastore.MongoDatastore;
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

        MongoDatastore.getInstance(); //NOTE(Olivier): Little hack to initialize mongo connnection on server start cuz singleton... crappy I know...

        port(PORT);

        /*
        *
        *  GET PARTICULAR PROVIDERS STORES BASED ON LOCATION
        *
        */

        before("/api/stores/:bannerCode/:postalCode", (request, response) -> {
            boolean validParameters = true;

            validParameters = POSTAL_CODE_PATTERN.matcher(request.params(":postalCode")).matches();
            validParameters = EyFlyerFetcher.EyFlyersProviders.getProviderFromString(request.params(":bannerCode")) != null;

            if (!validParameters) {
                halt(400, "Invalid parameters");
            }
        });
        get("/api/stores/:bannerCode/:postalCode", (req, res) ->  {
            return new EyFlyerFetcher()
                    .getStoreNearby(EyFlyerFetcher.EyFlyersProviders.getProviderFromString(req.params(":bannerCode")), req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET STORES CONCATENATION (ALL PROVIDERS) BASED ON LOCATION
        *
        */

        before("/api/stores/:postalCode", (request, response) -> {
            boolean validParameters = true;

            validParameters = POSTAL_CODE_PATTERN.matcher(request.params(":postalCode")).matches();

            if (!validParameters) {
                halt(400, "Invalid parameters");
            }
        });
        get("/api/stores/:postalCode", (req, res) ->  {
            return new EyFlyerFetcher().getAllStoreNearby(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET PUBlICATIONS OF A CERTAIN STORE
        *
        */

        get("/api/publications/:store", (req, res) ->  {
            //TODO: Get the latest stuff from je
            return new Object();

        }, new JsonTransformer());
    }
}
