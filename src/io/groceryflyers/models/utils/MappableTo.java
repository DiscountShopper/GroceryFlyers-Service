package io.groceryflyers.models.utils;

import io.groceryflyers.fetchers.impl.transcontinental.json.TCProvider;

/**
 * Created by jeremiep on 2016-01-30.
 */
public interface MappableTo<E> {
    public E mapToBusinessModel(TCProvider p);
}
