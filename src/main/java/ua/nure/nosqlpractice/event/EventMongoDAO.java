package ua.nure.nosqlpractice.event;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.nure.nosqlpractice.mongoDb.MongoConnection;

import java.util.*;


@Repository
public class EventMongoDAO implements IEventDAO{

    private final MongoCollection<Document> collection;

    @Autowired
    public EventMongoDAO(MongoConnection mongoConnection){
        collection = mongoConnection.getDatabase().getCollection("Event");
    }

    @Override
    public void create(Event event) {

        Document document = eventToDocument(event);
        collection.insertOne(document);

    }

    @Override
    public Optional<Event> getByName(String name){
        Document query = new Document("eventName", name);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToEvent(document));
    }
    @Override
    public Optional<Event> getById(ObjectId id) {

        Document query = new Document("_id", id);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToEvent(document));    }

    @Override
    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();

        //Cast docs to Events list and close cursor
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                events.add(documentToEvent(document));
            }
        }

        return events;
    }

    @Override
    public void update(Event event) {
        Document query = new Document("_id", event.getEventId());
        Document updates = new Document("$set", eventToDocument(event));

        try {
            collection.updateOne(query, updates);


        } catch (MongoException me) {
            System.err.println(
                    me.getMessage());
        }

    }

    @Override
    public void delete(Event event) {
        Bson query = Filters.eq("_id", event.getEventId());
        collection.deleteOne(query);
    }

    public Event documentToEvent(Document document) {

        if (document != null) {
            ObjectId eventId = document.getObjectId("_id");
            String name = document.getString("eventName");
            String description = document.getString("description");
            Date eventDate = document.getDate("eventDate");

            //Get list from document and cast it to array of size 3{Venue name, City, Country)
            List<String> addressList = document.get("address", ArrayList.class);
            String[] address = new String[addressList.size()];
            address = addressList.toArray(address);

            List<String> eventCategories = document.get("eventCategories", ArrayList.class);

            // Cast docs Tickets to Map<Ticket, Integer>
            List<Document> ticketsDoc = document.get("tickets", ArrayList.class);
            Map<Ticket, Integer> tickets = new HashMap<>();


            for (Document ticketDoc : ticketsDoc) {

                Ticket ticket = new Ticket(
                        ticketDoc.getString("name"),
                        ticketDoc.getDouble("price"));
                int availableTickets = ticketDoc.getInteger("availableTickets");
                tickets.put(ticket, availableTickets);
            }

            return new Event(eventId, name, description, eventDate, address, eventCategories, tickets);
        }
        return null;
    }


    public Document eventToDocument(Event event){

        if (event != null){
        List<Document> ticketDocuments = new ArrayList<>();
        for (Map.Entry<Ticket, Integer> entry : event.getTickets().entrySet()) {
            Ticket ticket = entry.getKey();
            Integer availableTickets = entry.getValue();

            Document ticketDocument = new Document("price", ticket.getPrice())
                    .append("name", ticket.getName())
                    .append("availableTickets", availableTickets);

            ticketDocuments.add(ticketDocument);
        }

        Document document = new Document("_id", event.getEventId())
                .append("eventName", event.getName())
                .append("eventDate", event.getEventDate())
                .append("eventCategories", event.getEventCategories())
                .append("address", Arrays.stream(event.getAddress()).toList())
                .append("description", event.getDescription())
                .append("tickets", ticketDocuments);
        return document;
        }

        return null;
    }
}