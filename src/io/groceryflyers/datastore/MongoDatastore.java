package io.groceryflyers.datastore;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.groceryflyers.models.PublicationSet;
import io.groceryflyers.models.utils.MappableTo;
import org.bson.Document;

import java.util.ArrayList;
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
        this.mongo = new MongoClient(new MongoClientURI(""));
        this.database = mongo.getDatabase("epicerie");
    }

    public void storeModel(String collectionName, MappableTo<Document> model){
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertOne(model.mapToBusinessModel(null));
    }

    public void storeModels(String collectionName, List<MappableTo<Document>> models){
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.insertMany(models.stream().map(x -> x.mapToBusinessModel(null)).collect(Collectors.toList()));
    }

    public Optional<List<Document>> findPublicationIfAvailable(String guid){
        MongoCollection<Document> collection = database.getCollection(PublicationSet.MONGO_DOCUMENT_NAME);
        FindIterable<Document> result = collection.find(eq("publication.identifier", guid));

        if(result.iterator().hasNext()){
            List<Document> publications = new ArrayList<>();
            while(result.iterator().hasNext()){
                publications.add(result.iterator().next());
            }
            return Optional.of(publications);
        }
        return Optional.empty();
    }
}
