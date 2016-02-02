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

    private final static String POSTAL_CODE = ":postalCode";
    private final static String BANNER_CODE = ":bannerCoder";
    private final static String PUBLICATION_ID = ":publicationId";
    private final static String PRODUCT_ID = ":productId";

    public static void main(String[] args) {
        MongoDatastore.getInstance(); //NOTE(Olivier): Little hack to initialize mongo connnection on server start cuz singleton... crappy I know...

        port(PORT);

        /*
        *
        *  GET PARTICULAR PROVIDERS STORES BASED ON LOCATION
        *
        */

        get(String.format("/api/stores/%s/%s", BANNER_CODE, POSTAL_CODE), (req, res) ->  {
            enforceProviderType(req);
            enforceBannerCode(req);
            enforceParamFormat(req, POSTAL_CODE_PATTERN, POSTAL_CODE);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type")))
                    .getStoreNearby(EyFlyerFetcher.EyFlyersProviders.getProviderFromString(req.params(":bannerCode")), req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET STORES CONCATENATION (ALL PROVIDERS) BASED ON LOCATION
        *
        */

        get(String.format("/api/stores/%s", POSTAL_CODE), (req, res) ->  {
            enforceProviderType(req);
            enforceParamFormat(req, POSTAL_CODE_PATTERN, POSTAL_CODE);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type"))).getAllStoreNearby(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET PUBlICATIONS OF A CERTAIN STORE
        *
        */

        get(String.format("/api/publications/%s/%s", BANNER_CODE, PUBLICATION_ID), (req, res) ->  {
            enforceProviderType(req);
            enforceBannerCode(req);
            enforceParamFormat(req, GUID_CODE_PATTERN, PUBLICATION_ID);

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

        get(String.format("/api/categories/%s", PRODUCT_ID), (req, res) ->  {
            enforceProviderType(req);
            enforceParamFormat(req, GUID_CODE_PATTERN, PRODUCT_ID);

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

        get(String.format("/api/products/%s/%s", PUBLICATION_ID, PRODUCT_ID), (req, res) -> {
            enforceProviderType(req);
            enforceParamFormat(req, GUID_CODE_PATTERN, PUBLICATION_ID);
            enforceParamFormat(req, GUID_CODE_PATTERN, PRODUCT_ID);

            Optional<Document> product = MongoDatastore.getInstance().findProduct(req.params(":publicationId"), req.params(":productId"));
            return product.orElseGet(Document::new);
        }, new JsonTransformer());

        /*
        *
        *  GET ALL PRODUCTS FROM EACH ONE OF THE CLOSEST STORES
        *
        */

        get(String.format("/api/closest/publications/%s", POSTAL_CODE), (req, res) -> {
            enforceProviderType(req);
            enforceParamFormat(req, POSTAL_CODE_PATTERN, POSTAL_CODE);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type"))).getAllPublicationSetsForAllStores(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET ALL CATEGORIES FROM EACH ONE OF THE CLOSEST STORES
        *
        */

        get(String.format("/api/closest/categories/%s", POSTAL_CODE), (req, res) -> {
            enforceProviderType(req);
            enforceParamFormat(req, POSTAL_CODE_PATTERN, POSTAL_CODE);

            return new EyFlyerFetcher(EyFlyerFetcher.EyFlyersFetcherTypes.fromString(req.queryParams("type"))).getAllCategories(req.params(":postalCode"));
        }, new JsonTransformer());

        /*
        *
        *  GET ALL RECOMMENDED PRODCUTS
        *
        */

        post(String.format("/api/recommended/products/%s", POSTAL_CODE), "application/json", (req, res) -> {
            enforceProviderType(req);
            enforceParamFormat(req, POSTAL_CODE_PATTERN, POSTAL_CODE);

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

    private static void enforceParamFormat(Request request, Pattern format, String param){
        if(!format.matcher(request.params(param)).matches()){
            halt(400, String.format("Invalid parameter value: %s", param));
        }
    }

    private static void enforceProviderType(Request request){
        if(!EyFlyerFetcher.EyFlyersProviders.isProviderTypeSupported(request.queryParams("type"))){
            halt(400, "You must provide the api type");
        }
    }

    private static void enforceBannerCode(Request request){
        if(EyFlyerFetcher.EyFlyersProviders.getProviderFromString(request.params(BANNER_CODE)) == null){
            halt(400, "Invalid parameters");
        }
    }

    private static class UselessStaticClassForJsonDeserialize {
        @SerializedName("key_words")
        public String[] key_words;
    }
}
