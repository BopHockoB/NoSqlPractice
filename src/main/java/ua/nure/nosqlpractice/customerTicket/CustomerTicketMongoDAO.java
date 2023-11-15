package ua.nure.nosqlpractice.customerTicket;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.EventMongoDAO;
import ua.nure.nosqlpractice.event.IEventDAO;
import ua.nure.nosqlpractice.mongoDb.MongoConnection;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerTicketMongoDAO implements ICustomerTicketDAO {

    private final MongoCollection<Document> collection;
    private final IEventDAO eventDAO;



    @Autowired
    public CustomerTicketMongoDAO(MongoConnection connection, EventMongoDAO eventMongoDAO) {
        this.collection = connection.getDatabase().getCollection("CustomerTicket");
        this.eventDAO = eventMongoDAO;
    }

    @Override
    public void create(CustomerTicket ticket) {
        Document document = customerTicketToDocument(ticket);
        collection.insertOne(document);
    }

    @Override
    public Optional<CustomerTicket> getById(ObjectId id) {

        Document query = new Document("_id", id);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToCustomerTicket(document));
    }

    @Override
    public List<CustomerTicket> getAll() {
        List<CustomerTicket> customerTickets = new ArrayList<>();

        //Cast docs to CustomerTicket list and close cursor
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                customerTickets.add(documentToCustomerTicket(document));
            }
        }

        return customerTickets;
    }

    @Override
    public void update(CustomerTicket customerTicket) {
        Document query = new Document("_id", customerTicket.getTicketId());
        Document updates = new Document("$set", customerTicketToDocument(customerTicket));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
            System.err.println(me.getMessage());
        }
    }

    @Override
    public void delete(ObjectId id) {
        Bson query = Filters.eq("_id", id);
        collection.deleteOne(query);
    }

    @Override
    public Document customerTicketToDocument(CustomerTicket customerTicket) {
        if (customerTicket != null) {
            if(customerTicket.getTicketId() == null)
                customerTicket.setTicketId(new ObjectId());
            return new Document("_id", customerTicket.getTicketId())
                    .append("eventId", customerTicket.getEvent().getEventId())
                    .append("userId", customerTicket.getUserId())
                    .append("purchasedDate", customerTicket.getPurchasedDate())
                    .append("ticketType", customerTicket.getTicketType())
                    .append("price", customerTicket.getPrice());
        }
        return null;
    }

    @Override
    public CustomerTicket documentToCustomerTicket(Document document) {
        if (document != null) {
            ObjectId ticketId = document.getObjectId("_id");
            ObjectId eventId = document.getObjectId("eventId");
            ObjectId userId = document.getObjectId("userId");
            Date purchasedDate = document.getDate("purchasedDate");
            String ticketType = document.getString("ticketType");
            Double price = document.getDouble("price");

            Event event = eventDAO.getById(eventId).orElse(null);

            new CustomerTicket.CustomerTicketBuilder()
                    .setTicketId(ticketId)
                    .setEvent(event)
                    .setUserId(userId)
                    .setPurchasedDate(purchasedDate)
                    .setTicketType(ticketType)
                    .setPrice(price)
                    .build();
        }
        return null;
    }
}


