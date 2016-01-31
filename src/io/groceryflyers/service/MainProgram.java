package io.groceryflyers.service;

import io.groceryflyers.datastore.MongoDatastore;
import io.groceryflyers.fetchers.impl.EyFlyerFetcher;
import org.apache.log4j.Logger;
import org.bson.Document;
import spark.Request;

import java.util.Optional;
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

        get("/api/stores/:bannerCode/:postalCode", (req, res) ->  {

            enforceBannerCode(req);
            enforcePostalCode(req);

            return new EyFlyerFetcher()
                    .getStoreNearby(EyFlyerFetcher.EyFlyersProviders.getProviderFromString(req.params(":bannerCode")), req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET STORES CONCATENATION (ALL PROVIDERS) BASED ON LOCATION
        *
        */

        get("/api/stores/:postalCode", (req, res) ->  {

            enforcePostalCode(req);

            return new EyFlyerFetcher().getAllStoreNearby(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET PUBlICATIONS OF A CERTAIN STORE
        *
        */

        get("/api/publications/:bannerCode/:publicationId", (req, res) ->  {

            enforceBannerCode(req);
            enforcePublicationId(req);

            return new EyFlyerFetcher()
                    .getAllPublicationSetsByStore(
                            EyFlyerFetcher.EyFlyersProviders.getProviderFromString(req.params(":bannerCode")),
                            req.params(":publicationId"));
        }, new JsonTransformer());

        /*
        *
        *  GET CATEGORIES OF A CERTAIN PUBLICATION
        *
        */

        get("/api/categories/:productId", (req, res) ->  {

            enforceProductId(req);

            return new EyFlyerFetcher()
                    .getAllPublicationSetsByStore(
                            EyFlyerFetcher.EyFlyersProviders.getProviderFromString(req.params(":bannerCode")),
                            req.params(":productId"));
        }, new JsonTransformer());

        /*
        *
        *  GET A SINGLE PRODUCT
        *
        */

        get("/api/products/:publicationId/:productId", (req, res) -> {

            enforcePublicationId(req);
            enforceProductId(req);

            Optional<Document> product = MongoDatastore.getInstance().findProduct(req.params(":publicationId"), req.params(":productId"));
            return product.orElseGet(Document::new);
        }, new JsonTransformer());

        /*
        *
        *  GET ALL PRODUCTS FROM EACH ONE OF THE CLOSEST STORES
        *
        */

        get("/api/closest/publications/:postalCode", (req, res) -> {

            enforcePostalCode(req);

            return new EyFlyerFetcher().getAllPublicationSetsForAllStores(req.params(":postalCode"));
        }, new JsonTransformer());
    }

    private static void enforcePostalCode(Request request){
        if(!POSTAL_CODE_PATTERN.matcher(request.params(":postalCode")).matches()){
            halt(400, "Invalid parameters");
        }
    }

    private static void enforcePublicationId(Request request){
        if(!GUID_CODE_PATTERN.matcher(request.params(":publicationId")).matches()){
            halt(400, "Invalid parameters");
        }
    }

    private static void enforceProductId(Request request){
        if(!GUID_CODE_PATTERN.matcher(request.params(":productId")).matches()){
            halt(400, "Invalid parameters");
        }
    }

    private static void enforceBannerCode(Request request){
        if(EyFlyerFetcher.EyFlyersProviders.getProviderFromString(request.params(":bannerCode")) == null){
            halt(400, "Invalid parameters");
        }
    }
}
