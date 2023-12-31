package ua.nure.nosqlpractice.event.eventDao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.nure.nosqlpractice.dbConnections.MongoConnection;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.EventCategory;
import ua.nure.nosqlpractice.event.Ticket;
import ua.nure.nosqlpractice.event.Venue;

import java.util.*;


@Repository
public class EventMongoDAO implements IEventDAO {

    private final MongoCollection<Document> collection;

    @Autowired
    public EventMongoDAO(MongoConnection mongoConnection) {
        collection = mongoConnection.getDatabase().getCollection("Event");
    }

    @Override
    public void create(Event event) {

        Document document = eventToDocument(event);
        collection.insertOne(document);

    }

    @Override
    public Optional<Event> getByName(String name) {
        Document query = new Document("eventName", name);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToEvent(document));
    }

    @Override
    public Optional<Event> getById(ObjectId id) {

        Document query = new Document("_id", id);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToEvent(document));
    }

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
    public void delete(ObjectId id) {
        Bson query = Filters.eq("_id", id);
        collection.deleteOne(query);
    }


    public Document eventToDocument(Event event) {
        if (event != null) {
                if(event.getEventId() == null)
                    event.setEventId(new ObjectId());
            List<Document> ticketDocuments = new ArrayList<>();
            for (Ticket ticket : event.getTickets()) {
                Document ticketDocument = new Document()
                        .append("name", ticket.getName())
                        .append("price", ticket.getPrice())
                        .append("availableTickets", ticket.getAvailableTickets());
                ticketDocuments.add(ticketDocument);
            }

            Venue venue = event.getVenue();
            Document venueDocument = new Document()
                    .append("venueName", venue.getName())
                    .append("venueCity", venue.getCity())
                    .append("venueCountry", venue.getCountry());

            List<Document> eventCategoriesDocuments = new ArrayList<>();
            for (EventCategory eventCategory : event.getEventCategories()) {
                Document ticketDocument = new Document()
                        .append("id", eventCategory.getId())
                        .append("categoryName", eventCategory.getCategoryName());
                ticketDocuments.add(ticketDocument);
            }

            return new Document("_id", event.getEventId())
                    .append("eventName", event.getName())
                    .append("description", event.getDescription())
                    .append("eventDate", event.getEventDate())
                    .append("venue", venueDocument)
                    .append("eventCategories", eventCategoriesDocuments)
                    .append("tickets", ticketDocuments);
        }
        return null;
    }

    public Event documentToEvent(Document document) {
        if (document != null) {
            ObjectId eventId = document.getObjectId("_id");
            String name = document.getString("eventName");
            String description = document.getString("description");
            Date eventDate = document.getDate("eventDate");

            Document venueDocument = document.get("venue", Document.class);
            Venue venue = new Venue.VenueBuilder()
                        .setName(venueDocument.getString("venueName"))
                        .setCity(venueDocument.getString("venueCity"))
                        .setCountry(venueDocument.getString("venueCountry"))
                        .build();

            List<EventCategory> eventCategories = new ArrayList<>();
            List<Document> categoryDocuments = document.get("eventCategories", ArrayList.class);
            for (Document categoryDocument : categoryDocuments) {
                Integer categoryId = categoryDocument.getInteger("id");
                String categoryName = categoryDocument.getString("categoryName");

                EventCategory eventCategory = new EventCategory(categoryId, categoryName);
                eventCategories.add(eventCategory);
            }

            List<Ticket> tickets = new ArrayList<>();
            List<Document> ticketDocuments = document.get("tickets", ArrayList.class);
            for (Document ticketDocument : ticketDocuments) {
                String ticketName = ticketDocument.getString("name");
                Double ticketPrice = ticketDocument.getDouble("price");
                Integer availableTickets = ticketDocument.getInteger("availableTickets");

                Ticket ticket = new Ticket(null ,ticketName, ticketPrice, availableTickets);
                tickets.add(ticket);
            }

            return new Event.EventBuilder()
                    .setEventId(eventId)
                    .setName(name)
                    .setDescription(description)
                    .setEventDate(eventDate)
                    .setAddress(venue)
                    .setEventCategories(eventCategories)
                    .setTickets(tickets)
                    .build();
        }
        return null;
    }
}


