package io.groceryflyers.fetchers;

/**
 * Created by jeremiep on 2016-02-02.
 */
public interface IFetcherFactory {
    IStoreFetcher createStoreFetcher();
    IProductFetcher createProductFetcher();
}
