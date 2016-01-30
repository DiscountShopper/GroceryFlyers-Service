package io.groceryflyers.datastore;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.groceryflyers.models.PublicationSet;
import io.groceryflyers.models.utils.MappableTo;
import org.bson.Document;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

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
        this.mongo = new MongoClient(new MongoClientURI("mongodb://javaservice:q7w8r9t0@162.243.54.4:27017/grocery"));
        this.database = mongo.getDatabase("grocery");
    }

    public void storeModel(String collectionName, MappableTo<Document> model){
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertOne(model.mapToBusinessModel(null));
    }

    public void storeModels(String collectionName, List<MappableTo<Document>> models){
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertMany(models.stream().map(x -> x.mapToBusinessModel(null)).collect(Collectors.toList()));
    }

    public Optional<Document> findPublicationIfAvailable(String guid){
        MongoCollection<Document> collection = database.getCollection(PublicationSet.MONGO_DOCUMENT_NAME);
        FindIterable<Document> result = collection.find(eq("publication.identifier", guid));

        if(result.iterator().hasNext()){
            return Optional.of(result.first());
        }
        return Optional.empty();
    }
}
