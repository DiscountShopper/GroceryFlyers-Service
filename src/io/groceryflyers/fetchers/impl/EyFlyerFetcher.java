package io.groceryflyers.fetchers.impl;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.GsonBuilder;
import com.mongodb.Mongo;
import io.groceryflyers.datastore.MongoDatastore;
import io.groceryflyers.fetchers.AbstractFetcher;
import io.groceryflyers.fetchers.impl.models.EyFlyersPublications;
import io.groceryflyers.fetchers.impl.models.EyFlyersPublicationsItems;
import io.groceryflyers.fetchers.impl.models.EyFlyersStores;
import io.groceryflyers.fetchers.impl.providers.*;
import io.groceryflyers.models.Publication;
import io.groceryflyers.models.PublicationItem;
import io.groceryflyers.models.PublicationSet;
import io.groceryflyers.models.Store;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyerFetcher extends AbstractFetcher {
    private static Logger LOG = Logger.getLogger(EyFlyerFetcher.class);
    public enum EyFlyersProviders {
        SUPER_C("http://eflyer.metro.ca/SUPRC/SUPRC", new SuperCProvider()),
        MAXI("http://eflyer.metro.ca/MAXI/MAXI", new MaxiProvider()),
        IGA("http://eflyer.metro.ca/IGA/IGA", new IGAProvider()),
        METRO("http://eflyer.metro.ca/MTR/MTR", new MetroProvider()),
        LOBLAWS("http://eflyer.metro.ca/LOB/LOB", new LoblawsProvider());

        private String base_url;
        private EyFlyerProvider provider;
        EyFlyersProviders(String base_url, EyFlyerProvider provider) {
            this.base_url = base_url;
            this.provider = provider;
        }

        public String getBaseUrl() {
            return this.base_url;
        }

        public String getStoresNearbyPostalCodeUrl(String postalCode) {
            return this.getBaseUrl().concat("/fr/Landing/GetClosestStoresByPostalCode?" +
                    "orgCode=9999&" +
                    "bannerCode=9999&" +
                    "countryCode=CA&" +
                    "postalCode=" + postalCode + "&" +
                    "culture=fr");
        }

        public String getPublicationItemsByPubGuid(String guid) {
            return this.getBaseUrl().concat("/fr/" + guid + "/Product/ListAllProducts");
        }

        public String getPublicationByStoreId(String sguid) {
            return this.getBaseUrl().concat("/fr/Landing/GetPublicationsByStoreId?" +
                    "storeId=" + sguid);
        }

        public EyFlyerProvider getProvider() { return this.provider; }

        public static EyFlyersProviders getProviderFromString(String provider) {
            switch(provider){
                case "SUPERC":
                    return SUPER_C;
                case "MAXI":
                    return MAXI;
                case "IGA":
                    return IGA;
                case "METRO":
                    return METRO;
                case "LOBLAWS":
                    return LOBLAWS;
                default:
                    return null;
            }
        }
    };

    @Override
    public List<Store> getStoreNearby(EyFlyersProviders provider, String postalCode) {
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(provider.getStoresNearbyPostalCodeUrl(postalCode))
            );

            List<EyFlyersStores> stores = req.execute().parseAs(EyFlyersStores.eyFlyersStoresList.class).storeList;
            return stores.stream().map( item -> (
                    (Function<EyFlyersStores, Store>) map -> {
                        return map.mapToBusinessModel(provider.getProvider());
                    }).apply(item)
            ).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Store> getAllStoreNearby(String postalCode) {
        List<Store> allStores = new ArrayList<>();

        for(EyFlyersProviders provider: EyFlyersProviders.values()){
            allStores.addAll(getStoreNearby(provider, postalCode));
        }

        return allStores;
    }

    @Override
    public List<PublicationItem> getAllPublicationItems(EyFlyersProviders provider, String pguid) {
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(provider.getPublicationItemsByPubGuid(pguid))
            );

            List<EyFlyersPublicationsItems> items = req.execute().parseAs(EyFlyersPublicationsItems.EyFlyersPublicationItemsList.class).products;
            return items.stream().map( item -> (
                    (Function<EyFlyersPublicationsItems, PublicationItem>) map -> {
                        return map.mapToBusinessModel(provider.getProvider());
                    }).apply(item)
            ).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Publication> getAllPublicationByStore(EyFlyersProviders provider, String sguid) {
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(provider.getPublicationByStoreId(sguid))
            );

            List<EyFlyersPublications> publications =
                    req.execute().parseAs(EyFlyersPublications.EyFlyersPublicationsList.class).publications;
            return publications.stream().map( item -> (
                    (Function<EyFlyersPublications, Publication>) map -> {
                        return map.mapToBusinessModel(provider.getProvider());
                    }).apply(item)
            ).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PublicationSet> getAllPublicationSetsByStore(EyFlyersProviders provider, String sguid) {
        LinkedList<PublicationSet> pset = new LinkedList<PublicationSet>();
        for(Publication pub : this.getAllPublicationByStore(provider, sguid)) {
            Optional<Document> existingPub = MongoDatastore.getInstance().findPublicationIfAvailable(pub.id);
            if(existingPub.isPresent()) {
                LOG.info("Using caching for " + sguid);
                PublicationSet existingSet = new GsonBuilder().create().fromJson(existingPub.get().toJson(), PublicationSet.class);
                pset.add(existingSet);
                continue;
            }

            PublicationSet set = new PublicationSet();
            set.publication = pub;
            set.items = this.getAllPublicationItems(provider, pub.id);

            MongoDatastore.getInstance().storeModel(PublicationSet.MONGO_DOCUMENT_NAME, set);
            pset.add(set);
        }

        return pset;
    }

    public static void main(String[] args) {
        EyFlyerFetcher fetcher = new EyFlyerFetcher();

        List<PublicationSet> items = fetcher.getAllPublicationSetsByStore(EyFlyersProviders.METRO, "5bbefd7f-3ebf-463a-8849-bf8c43959d52");
        System.out.println(items.size());
    }
}
