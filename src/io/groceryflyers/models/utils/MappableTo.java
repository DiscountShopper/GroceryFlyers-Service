package io.groceryflyers.models.utils;

import io.groceryflyers.fetchers.AbstractProvider;

/**
 * Created by jeremiep on 2016-01-30.
 */
public interface MappableTo<E> {
    public E mapToBusinessModel(AbstractProvider p);
}
