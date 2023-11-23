package ua.nure.nosqlpractice.dbConnections;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MongoConnection {

//    @Value("${nosqlPractice.mongodb.host}")
//    private String host;
//
//    @Value("${nosqlPractice.mongodb.port}")
//    private String port;
//
//    @Value("${nosqlPractice.mongodb.databaseName}")
//    private String databaseName ;

//    private static final String HOST = "localhost"; // MongoDB server host
//    private static final int PORT = 27017;          // MongoDB server port
//    private static final String DATABASE_NAME = "TicketMaster"; // Name of your database

    private final MongoClient mongoClient;
    private final MongoDatabase database;


    @Autowired
    private MongoConnection(
            @Value("${nosqlPractice.mongodb.host}") String host,
            @Value("${nosqlPractice.mongodb.port}") String port,
            @Value("${nosqlPractice.mongodb.databaseName}") String databaseName) {


        ConnectionString connectionString = new ConnectionString("mongodb://" + host + ":" + port + "/");
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(databaseName);
        System.out.println("Successful connection to " + databaseName);
    }


    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
