package io.groceryflyers.fetchers.impl.transcontinental.json.models;

import com.google.api.client.util.Key;
import io.groceryflyers.fetchers.impl.transcontinental.json.TCProvider;
import io.groceryflyers.models.Category;
import io.groceryflyers.models.utils.MappableTo;

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
    public Category mapToBusinessModel(TCProvider p) {
        Category c = new Category();

        c.id = this.id;
        c.name = this.name;
        c.totalProducts = this.totalProducts;

        return c;
    }

}
