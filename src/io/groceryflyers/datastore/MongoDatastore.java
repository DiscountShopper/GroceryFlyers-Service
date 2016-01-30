package io.groceryflyers.datastore;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import io.groceryflyers.models.utils.MappableTo;

/**
 * Created by olivier on 2016-01-30.
 */
public class MongoDatastore {
    private static MongoDatastore instance;

    public static MongoDatastore getInstance() {
        if(instance == null){
            instance = new MongoDatastore();
        }
        return instance;
    }

    private MongoDatastore(){

    }

    private void storeModel(String collection, MappableTo<BasicDBObject> model){

    }
}
