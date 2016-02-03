package io.groceryflyers.fetchers.impl.transcontinental.json;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.GsonBuilder;
import io.groceryflyers.datastore.MongoDatastore;
import io.groceryflyers.fetchers.IProductFetcher;
import io.groceryflyers.fetchers.IStoreFetcher;
import io.groceryflyers.fetchers.base.fetchers.BaseHTTPJsonFetcher;
import io.groceryflyers.fetchers.impl.transcontinental.json.models.*;
import io.groceryflyers.fetchers.impl.transcontinental.json.providers.drugstores.*;
import io.groceryflyers.fetchers.impl.transcontinental.json.providers.groceries.*;
import io.groceryflyers.models.*;
import io.groceryflyers.storage.AwsS3Manager;
import org.apache.log4j.Logger;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.bson.Document;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyerFetcher extends BaseHTTPJsonFetcher implements IProductFetcher, IStoreFetcher {
    private static Logger LOG = Logger.getLogger(EyFlyerFetcher.class);
    public enum EyFlyersFetcherTypes {
        GROCERIES("GROCERIES"),
        DRUGSTORES("DRUGSTORES");

        private String code;
        private EyFlyersFetcherTypes(String code) {
            this.code = code;
        }

        public String getCode() { return this.code; }

        public static EyFlyersFetcherTypes fromString(String str) {
            if(str.equalsIgnoreCase("GROCERIES")) { return GROCERIES; }
            else if(str.equalsIgnoreCase("DRUGSTORES")) { return DRUGSTORES; }

            return null;
        }
    }
    public enum EyFlyersProviders {
        SUPER_C("http://eflyer.metro.ca/SUPRC/SUPRC", "SUPERC", EyFlyersFetcherTypes.GROCERIES, new SuperCProvider()),
        MAXI("http://eflyer.metro.ca/MAXI/MAXI", "MAXI", EyFlyersFetcherTypes.GROCERIES, new MaxiProvider()),
        IGA("http://eflyer.metro.ca/IGA/IGA", "IGA", EyFlyersFetcherTypes.GROCERIES, new IGAProvider()),
        METRO("http://eflyer.metro.ca/MTR/MTR", "METRO", EyFlyersFetcherTypes.GROCERIES, new MetroProvider()),
        LOBLAWS("http://eflyer.metro.ca/LOBPQ/LOBPQ", "LOBLAWS", EyFlyersFetcherTypes.GROCERIES, new LoblawsProvider()),
        PROVIGO("http://eflyer.metro.ca/PROV/PROV", "PROVIGO", EyFlyersFetcherTypes.GROCERIES, new ProvigoProvider()),
        M_ET_M("http://eflyer.metro.ca/LAM/LAM", "M_ET_M", EyFlyersFetcherTypes.GROCERIES, new MetMProvider()),
        BRUNET("http://eflyer.metro.ca/BRNT/BRNT", "BRUNET", EyFlyersFetcherTypes.DRUGSTORES, new BrunetProvider()),
        JEAN_COUTU("http://eflyer.metro.ca/JCP/JCP", "JEAN_COUTU", EyFlyersFetcherTypes.DRUGSTORES, new JeanCoutuProvider()),
        UNIPRIX("http://eflyer.metro.ca/UNIP/UNIP", "UNIPRIX", EyFlyersFetcherTypes.DRUGSTORES, new UniPrixProvider()),
        PROXIM("http://eflyer.metro.ca/PXM/PXM", "PROXIM", EyFlyersFetcherTypes.DRUGSTORES, new ProximProvider()),
        PHARMAPRIX("http://eflyer.metro.ca/PHX/PHX", "PHARMAPRIX",EyFlyersFetcherTypes.DRUGSTORES, new PharmaPrixProvider()),
        SHOPPERS_DRUG_MART("http://eflyer.metro.ca/SDM/SDM", "SHOPPERS_DRUG_MART",EyFlyersFetcherTypes.DRUGSTORES, new ShoppersDrugMartProvider());


        private String base_url;
        private String code;
        private EyFlyersFetcherTypes type;
        private EyFlyerProvider provider;
        EyFlyersProviders(String base_url, String code, EyFlyersFetcherTypes type, EyFlyerProvider provider) {
            this.base_url = base_url;
            this.code = code;
            this.type = type;
            this.provider = provider;
        }

        public String getBannerCode() { return this.code; }

        public EyFlyersFetcherTypes getStoreType() { return this.type; }

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

        public String getCategoriesByPublication(String pguid) {
            return this.getBaseUrl().concat("/fr/" + pguid + "/Publication/Categories");
        }

        public String getPdfPagesUrl(String pguid, String sguid, Integer singlePage) {
            if(singlePage == null) {
                return this.getBaseUrl().concat("/fr/" + pguid + "/Page/PDF?" +
                        "print=false&" +
                        "storeId=" + sguid);
            } else {
                return this.getBaseUrl().concat("/fr/" + pguid + "/Page/PDF?" +
                        "pageNums=" + singlePage.toString() + "&" +
                        "print=false&" +
                        "storeId=" + sguid);
            }

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
                case "PROVIGO":
                    return PROVIGO;
                case "M_ET_M":
                    return M_ET_M;
                case "BRUNET":
                    return BRUNET;
                case "JEAN_COUTU":
                    return JEAN_COUTU;
                case "UNIPRIX":
                    return UNIPRIX;
                case "PROXIM":
                    return PROXIM;
                case "PHARMAPRIX":
                    return PHARMAPRIX;
                case "SHOPPERS_DRUG_MART":
                    return SHOPPERS_DRUG_MART;
                default:
                    throw new RuntimeException("Unknown bannerCode from string " + provider);
            }
        }

        public static EyFlyersProviders[] getProvidersByType(EyFlyersFetcherTypes type) {
            switch(type) {
                case GROCERIES:
                    return new EyFlyersProviders[] { SUPER_C, MAXI, IGA, METRO, LOBLAWS, PROVIGO, M_ET_M };
                case DRUGSTORES:
                    return new EyFlyersProviders[] { BRUNET, JEAN_COUTU, UNIPRIX, PROXIM, PHARMAPRIX, SHOPPERS_DRUG_MART };
            }

            return null;
        }

        public static boolean isProviderTypeSupported(String type) {
            return type.equalsIgnoreCase("GROCERIES") || type.equalsIgnoreCase("DRUGSTORES");
        }
    };

    private EyFlyersFetcherTypes currentType;

    public EyFlyerFetcher(EyFlyersFetcherTypes type) {
        this.currentType = type;
    }

    @Override
    public List<Store> getStoreNearby(EyFlyersProviders provider, String postalCode) {
        try {
            LOG.info(provider.getStoresNearbyPostalCodeUrl(postalCode));
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

        for(EyFlyersProviders provider: EyFlyersProviders.getProvidersByType(this.currentType)){
            allStores.addAll(getStoreNearby(provider, postalCode));
        }

        return allStores;
    }

    @Override
    public List<PublicationItem> getAllPublicationItems(EyFlyersProviders provider, String pguid) {
        try {
            LOG.info(provider.getPublicationItemsByPubGuid(pguid));
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
            LOG.info(provider.getPublicationByStoreId(sguid));
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
                PublicationSet existingSet = new GsonBuilder().create().fromJson(existingPub.get().toJson(), PublicationSet.class);
                pset.add(existingSet);
                LOG.debug("Cached request " + existingSet.publication.id);
                continue;
            }

            PublicationSet set = new PublicationSet();
            set.publication = pub;
            set.banner = provider.getBannerCode();
            set.type = provider.getStoreType().getCode();
            set.items = this.getAllPublicationItems(provider, pub.id);

            MongoDatastore.getInstance().storeModel(PublicationSet.MONGO_DOCUMENT_NAME, set);
            pset.add(set);
        }

        return pset;
    }

    @Override
    public List<Category> getAllCategoriesByPublication(EyFlyersProviders provider, String pguid) {
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(provider.getCategoriesByPublication(pguid))
            );

            EyFlyersCategories[] categories = req.execute().parseAs(EyFlyersCategories[].class);

            return Arrays.asList(categories).stream().map(item -> (
                    (Function<EyFlyersCategories, Category>) map -> {
                        return map.mapToBusinessModel(provider.getProvider());
                    }).apply(item)
            ).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PublicationSet> getAllPublicationSetsForAllStores(String postalCode) {
        List<PublicationSet> sets = new LinkedList<PublicationSet>();
        for(EyFlyersProviders p : EyFlyersProviders.getProvidersByType(this.currentType)) {
            List<Store> nearbyStores = this.getStoreNearby(p, postalCode);
            if(nearbyStores.size() > 0) {
                for(PublicationSet set : this.getAllPublicationSetsByStore(p, nearbyStores.get(0).guid)) {
                    sets.add(set);
                }
            }
        }

        return sets;
    }

    public List<PublicationItem> getRelatedProducts(String[] keywords, String postalCode) {
        List<PublicationSet> sets = this.getAllPublicationSetsForAllStores(postalCode);
        HashMap<PublicationItem, Integer> result = new HashMap<PublicationItem, Integer>();

        for(PublicationSet set : sets) {
            for(PublicationItem item : set.items) {
                Set<String> s1 = new HashSet<String>(Arrays.asList(keywords));
                Set<String> s2 = new HashSet<String>(Arrays.asList(item.key_words));

                s1.retainAll(s2);

                if(s1.size() >= 1) {
                    result.put(item, s1.size());
                }
            }
        }

        return result.entrySet()
                .stream()
                .sorted((x1, x2) -> { return x2.getValue().compareTo(x1.getValue()); })
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public List<Category> getAllCategories(String postalCode) {
        List<Category> result = new LinkedList<Category>();
        for(PublicationSet pubSet : this.getAllPublicationSetsForAllStores(postalCode)) {
            List<Category> categories = this.getAllCategoriesByPublication(
                    EyFlyersProviders.getProviderFromString(pubSet.banner),
                    pubSet.publication.id);
            for(Category cat : categories) {
                if(!result.contains(cat)) {
                    result.add(cat);
                }
            }
        }

        return result;
    }

    public File downloadPDFForPublicationItem(EyFlyersProductItemRequest re) {
        PublicationItem pubItem = new GsonBuilder().create().fromJson(
                MongoDatastore.getInstance().findProduct(re.pubguid, re.pguid).get().toJson(),
                PublicationItem.class);
        try {
            HttpRequest req = this.getDefaultHttpFactory().buildGetRequest(
                    new GenericUrl(EyFlyersProviders
                            .getProviderFromString(re.banner_code)
                            .getPdfPagesUrl(re.pubguid, re.sguid, pubItem.page_number))
            );

            File downloadPdf = new File("./tmp/" + re.pguid.concat(".pdf"));
            OutputStream fout = new FileOutputStream(downloadPdf);
            req.execute().download(fout);
            fout.close();

            return downloadPdf;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String downloadMergeAndUploadAllPDFForPublications(List<EyFlyersProductItemRequest> requests) {
        List<File> downloadedPdfs = new LinkedList<File>();
        PDFMergerUtility merge = new PDFMergerUtility();

        for(EyFlyersProductItemRequest req : requests) {
            File f = this.downloadPDFForPublicationItem(req);
            downloadedPdfs.add(f);

            try {
                merge.addSource(f);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        File mergedPdf = new File("./tmp/".concat(UUID.randomUUID().toString()).concat(".pdf"));

        try {
            FileOutputStream out = new FileOutputStream(mergedPdf);
            merge.setDestinationStream(out);
            merge.mergeDocuments(null);
            out.close();

            downloadedPdfs.stream().forEach(x -> { x.delete(); });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return AwsS3Manager.getInstance().persist(mergedPdf.getName(), mergedPdf);
    }

    public static void main(String[] args) {
        EyFlyerFetcher fetcher = new EyFlyerFetcher(EyFlyersFetcherTypes.GROCERIES);

        fetcher.getAllStoreNearby("H1X 2T9");

        /*List<PublicationSet> items = fetcher.getAllPublicationSetsByStore(EyFlyersProviders.MAXI, "3c4099e9-983e-4eb6-beee-3b5b90432e90");
        System.out.println(items.size());
        LinkedList<EyFlyersProductItemRequest> reqs = new LinkedList<>();
        reqs.add(new EyFlyersProductItemRequest("MAXI", "1357247e-91e4-4995-8f26-c18d027fbcfd", "3c4099e9-983e-4eb6-beee-3b5b90432e90", "3a1b3d6d-cc0e-4c33-b6f9-651e1e65e86b"));
        reqs.add(new EyFlyersProductItemRequest("MAXI", "1357247e-91e4-4995-8f26-c18d027fbcfd", "3c4099e9-983e-4eb6-beee-3b5b90432e90", "c47b5f79-b872-4bbc-93e8-fc3a31e3d2cb"));
        reqs.add(new EyFlyersProductItemRequest("MAXI", "1357247e-91e4-4995-8f26-c18d027fbcfd", "3c4099e9-983e-4eb6-beee-3b5b90432e90", "2d2c0f4e-3656-4e36-baf4-c89c5e8d2faa"));

        String mergedPdf = fetcher.downloadMergeAndUploadAllPDFForPublications(reqs);
        System.out.print(mergedPdf);*/
    }
}
