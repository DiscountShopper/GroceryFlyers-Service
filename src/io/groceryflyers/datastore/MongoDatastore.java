package io.groceryflyers.datastore;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.groceryflyers.models.utils.MappableTo;
import org.bson.Document;

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

    private MongoClient mongo;
    private MongoDatabase database;

    private MongoDatastore(){
        this.mongo = new MongoClient(new MongoClientURI(""));
        this.database = mongo.getDatabase("epicerie");
    }

    public void storeModel(String collectionName, MappableTo<Document> model){
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertOne(model.mapToBusinessModel(null));
    }
}
