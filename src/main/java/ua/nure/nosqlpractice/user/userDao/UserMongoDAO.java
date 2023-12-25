package ua.nure.nosqlpractice.user.userDao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.CustomerTicketMongoDAO;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.ICustomerTicketDAO;
import ua.nure.nosqlpractice.dbConnections.MongoConnection;
import ua.nure.nosqlpractice.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Lazy
@Component
public class UserMongoDAO implements IUserDAO {

    private final MongoCollection<Document> collection;
    @Qualifier("customerTicketMongoDAO")
    private final ICustomerTicketDAO customerTicketDAO;

    @Autowired
    public UserMongoDAO(MongoConnection connection, CustomerTicketMongoDAO customerTicketMongoDAO) {
        collection = connection.getDatabase().getCollection("User");
        this.customerTicketDAO = customerTicketMongoDAO;
    }


    @Override
    public void create(User user) {
        Document document = userToDocument(user);
        collection.insertOne(document);
    }

    @Override
    public Optional<User> getById(ObjectId id) {

        Document query = new Document("_id", id);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToUser(document));
    }

    @Override
    public Optional<User> getByLastName(String lastName) {

        Document query = new Document("lastName", lastName);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToUser(document));
    }

    public Optional<User> getByEmail(String email) {

        Document query = new Document("email", email);
        Document document = collection.find(query).first();

        return Optional.ofNullable(documentToUser(document));
    }

    @Override
    public List<User> getAll() {

        List<User> users = new ArrayList<>();

        //Cast docs to CustomerTicket list and close cursor
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                users.add(documentToUser(document));
            }
        }

        return users;
    }

    @Override
    public void update(User user) {
        Document query = new Document("_id", user.getUserId());
        Document updates = new Document("$set", userToDocument(user));

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

    public Document userToDocument(User user) {
        if (user != null) {
            if(user.getUserId() == null)
                user.setUserId(new ObjectId());

            List<ObjectId> ticketsId = new ArrayList<>();
            for (CustomerTicket ticket : user.getTickets()) {
               ticketsId.add(ticket.getTicketId());
            }

            return new Document("_id", user.getUserId())
                    .append("email", user.getEmail())
                    .append("password", user.getPassword())
                    .append("firstName", user.getFirstName())
                    .append("lastName", user.getLastName())
                    .append("age", user.getAge())
                    .append("tickets", ticketsId);
        }
        return null;
    }

    public User documentToUser(Document document) {
        if (document != null) {
            ObjectId userId = document.getObjectId("_id");
            String email = document.getString("email");
            String password = document.getString("password");
            String firstName = document.getString("firstName");
            String lastName = document.getString("lastName");
            short age = document.getInteger("age").shortValue();

            List<CustomerTicket> tickets = new ArrayList<>();
            List<ObjectId> ticketsId = document.get("tickets", ArrayList.class);

            for (ObjectId ticketId: ticketsId) {
                Optional<CustomerTicket> customerTicket = customerTicketDAO.getById(ticketId);
                customerTicket.ifPresent(tickets::add);
            }


            return new User.UserBuilder()
                    .setUserId(userId)
                    .setEmail(email)
                    .setPassword(password)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setAge(age)
                    .setTickets(tickets)
                    .build();
        }
        return null;
    }
}
