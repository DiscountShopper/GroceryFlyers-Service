package io.groceryflyers.service;

import io.groceryflyers.datastore.MongoDatastore;
import io.groceryflyers.fetchers.impl.EyFlyerFetcher;

import java.util.regex.Pattern;

import static spark.Spark.*;

/**
 * Created by olivier on 2016-01-30.
 */
public class MainProgram {
    private final static int PORT = 1337;
    private final static Pattern POSTAL_CODE_PATTERN = Pattern.compile("[a-zA-Z][0-9][a-zA-Z][0-9][a-zA-Z][0-9]");
    private final static Pattern GUID_CODE_PATTERN = Pattern.compile("^[0-9a-z]{8}\\-[0-9a-z]{4}\\-[0-9a-z]{4}\\-[0-9a-z]{4}\\-[0-9a-z]{12}$");

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

        before("/api/publications/:bannerCode/:guid", (request, response) -> {
            boolean validParameters = true;

            validParameters = EyFlyerFetcher.EyFlyersProviders.getProviderFromString(request.params(":bannerCode")) != null;
            validParameters = GUID_CODE_PATTERN.matcher(request.params(":guid")).matches();

            if (!validParameters) {
                halt(400, "Invalid parameters");
            }
        });
        get("/api/publications/:bannerCode/:guid", (request, response) ->  {
            return new EyFlyerFetcher()
                    .getAllPublicationSetsByStore(
                            EyFlyerFetcher.EyFlyersProviders.getProviderFromString(request.params(":bannerCode")),
                            request.params("guid"));
        }, new JsonTransformer());

        /*
        *
        *  GET CATEGORIES OF A CERTAIN PUBLICATION
        *
        */
        before("/api/categories/:bannerCode/:pguid", (request, response) -> {
            boolean validParameters = true;

            validParameters = EyFlyerFetcher.EyFlyersProviders.getProviderFromString(request.params(":bannerCode")) != null;
            validParameters = GUID_CODE_PATTERN.matcher(request.params(":pguid")).matches();

            if (!validParameters) {
                halt(400, "Invalid parameters");
            }
        });
        get("/api/categories/:pguid", (request, response) ->  {
            return new EyFlyerFetcher()
                    .getAllPublicationSetsByStore(
                            EyFlyerFetcher.EyFlyersProviders.getProviderFromString(request.params(":bannerCode")),
                            request.params("pguid"));
        }, new JsonTransformer());
    }
}
