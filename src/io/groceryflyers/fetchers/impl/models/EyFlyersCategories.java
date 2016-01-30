package io.groceryflyers.fetchers.impl.models;

import com.google.api.client.util.Key;
import io.groceryflyers.fetchers.AbstractProvider;
import io.groceryflyers.models.Category;
import io.groceryflyers.models.utils.MappableTo;

import java.util.List;

/**
 * Created by jeremiep on 2016-01-30.
 */
public class EyFlyersCategories implements MappableTo<Category> {
    @Key("CategoryId")
    private String id;

    @Key("Category")
    private String name;

    @Key("TotalProducts")
    private int totalProducts;

    @Override
    public Category mapToBusinessModel(AbstractProvider p) {
        Category c = new Category();

        c.id = this.id;
        c.name = this.name;
        c.totalProducts = this.totalProducts;

        return c;
    }

}
