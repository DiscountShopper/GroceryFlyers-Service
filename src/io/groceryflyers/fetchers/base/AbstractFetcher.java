package io.groceryflyers.fetchers.base;

/**
 * Created by jeremiep on 2016-02-02.
 */
public abstract class AbstractFetcher <D extends AbstractDecoder> {
    private D defaultDecoder;

    protected AbstractFetcher(D decoder) {
        this.defaultDecoder = decoder;
    }

    protected D getDefaultDecoder() { return defaultDecoder; }
}
