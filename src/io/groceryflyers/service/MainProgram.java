package io.groceryflyers.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.groceryflyers.datastore.MongoDatastore;
import io.groceryflyers.fetchers.impl.EyFlyerFetcher;
import io.groceryflyers.fetchers.impl.models.EyFlyerPdfMergeRequest;
import io.groceryflyers.fetchers.impl.models.EyFlyersProductItemRequest;
import io.groceryflyers.fetchers.impl.models.EyFlyersPublicationsItems;
import org.apache.log4j.Logger;
import org.bson.Document;
import spark.Request;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final static Logger LOGGER = Logger.getLogger(MainProgram.class);

    public static void main(String[] args) {
        MongoDatastore.getInstance(); //NOTE(Olivier): Little hack to initialize mongo connnection on server start cuz singleton... crappy I know...

        port(PORT);

        /*
        *
        *  GET PARTICULAR PROVIDERS STORES BASED ON LOCATION
        *
        */

        get("/api/stores/:bannerCode/:postalCode", (req, res) ->  {
            enforceProviderType(req);
            enforceBannerCode(req);
            enforcePostalCode(req);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type")))
                    .getStoreNearby(EyFlyerFetcher.EyFlyersProviders.getProviderFromString(req.params(":bannerCode")), req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET STORES CONCATENATION (ALL PROVIDERS) BASED ON LOCATION
        *
        */

        get("/api/stores/:postalCode", (req, res) ->  {
            enforceProviderType(req);
            enforcePostalCode(req);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type"))).getAllStoreNearby(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET PUBlICATIONS OF A CERTAIN STORE
        *
        */

        get("/api/publications/:bannerCode/:publicationId", (req, res) ->  {
            enforceProviderType(req);
            enforceBannerCode(req);
            enforcePublicationId(req);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type")))
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
            enforceProviderType(req);
            enforceProductId(req);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type")))
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
            enforceProviderType(req);
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
            enforceProviderType(req);
            enforcePostalCode(req);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type"))).getAllPublicationSetsForAllStores(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET ALL CATEGORIES FROM EACH ONE OF THE CLOSEST STORES
        *
        */

        get("/api/closest/categories/:postalCode", (req, res) -> {
            enforceProviderType(req);
            enforcePostalCode(req);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type"))).getAllCategories(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET ALL RECOMMENDED PRODCUTS
        *
        */

        post("/api/recommended/products/:postalCode", "application/json", (req, res) -> {
            enforceProviderType(req);
            enforcePostalCode(req);

            LOGGER.warn("Got into recommended request");

            UselessStaticClassForJsonDeserialize jsonObject = new Gson().fromJson(req.body(), UselessStaticClassForJsonDeserialize.class);
            String[] fields = jsonObject.key_words;


            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type"))).getRelatedProducts(fields, req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET PDF MERGED FROM CART
        *
        */

        post("/api/pdf", "application/json", (req, res) -> {

            LOGGER.warn("Got into pdf request");

            EyFlyerPdfMergeRequest itemsRequested = new Gson().fromJson(req.body(), EyFlyerPdfMergeRequest.class);
            return new Document("url", new EyFlyerFetcher(null).downloadMergeAndUploadAllPDFForPublications(itemsRequested.products));
        }, new JsonTransformer());

    }

    private static void enforceProviderType(Request request){
        if(!EyFlyerFetcher.EyFlyersProviders.isProviderTypeSupported(request.queryParams("type"))){
            halt(400, "You must provide the api type");
        }
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

    private static class UselessStaticClassForJsonDeserialize {
        @SerializedName("key_words")
        public String[] key_words;
    }
}
