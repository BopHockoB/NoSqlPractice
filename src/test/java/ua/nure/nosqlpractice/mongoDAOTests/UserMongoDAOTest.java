package ua.nure.nosqlpractice.mongoDAOTests;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.event.*;
import ua.nure.nosqlpractice.dbConnections.MongoConnection;
import ua.nure.nosqlpractice.event.eventDao.EventMongoDAO;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.userDao.UserMongoDAO;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMongoDAOTest {
    @Autowired
    private MongoConnection connection;
    @Autowired
    private UserMongoDAO userMongoDAO;
    @Autowired
    private EventMongoDAO eventMongoDAO;

    @Before
    public void setUp() {
        connection.getDatabase().getCollection("User").drop();

        User user1 = createUserObject();
        User user2 = createUserObject();
        User user3 = createUserObject();

        userMongoDAO.create(user1);
        userMongoDAO.create(user2);
        userMongoDAO.create(user3);
    }

    @Test
    public void testCreateAndRetrieveUser() {
        User user = createUserObject();
        userMongoDAO.create(user);

        ObjectId userId = user.getUserId();
        Optional<User> retrievedUser = userMongoDAO.getById(userId);

        assertTrue(retrievedUser.isPresent());
        assertEquals(userId, retrievedUser.get().getUserId());
        assertEquals(user.getEmail(), retrievedUser.get().getEmail());
    }

    @Test
    public void testGetByLastName() {
        String lastNameToSearch = "Doe";

        User user = createUserObject();
        user.setLastName(lastNameToSearch);
        userMongoDAO.create(user);


        Optional<User> userToCheck = userMongoDAO.getByLastName(lastNameToSearch);

        assertTrue(userToCheck.isPresent());
        assertEquals(lastNameToSearch, userToCheck.get().getLastName());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userMongoDAO.getAll();

        assertFalse(users.isEmpty());
    }

    @Test
    public void testUpdateUser() {
        User user = createUserObject();
        userMongoDAO.create(user);

        for (int i = 0; i < user.getTickets().size(); i++) {
            eventMongoDAO.create(user.getTickets().get(i).getEvent());
        }

        ObjectId userId = user.getUserId();
        Optional<User> retrievedUser = userMongoDAO.getById(userId);
        assertTrue(retrievedUser.isPresent());


        User updatedUser = retrievedUser.get();
        updatedUser.setFirstName("UpdatedFirstName");
        updatedUser.setLastName("UpdatedLastName");
        updatedUser.setAge((short) 30);

        userMongoDAO.update(updatedUser);

        // Retrieve the updated user
        Optional<User> updatedUserResult = userMongoDAO.getById(userId);
        assertTrue(updatedUserResult.isPresent());

        assertEquals("UpdatedFirstName", updatedUserResult.get().getFirstName());
        assertEquals("UpdatedLastName", updatedUserResult.get().getLastName());
        assertEquals(30, updatedUserResult.orElse(null).getAge());
    }

    @Test
    public void testDeleteUser() {
        User user = createUserObject();
        userMongoDAO.create(user);

        ObjectId userId = user.getUserId();
        Optional<User> retrievedUser = userMongoDAO.getById(userId);
        assertTrue(retrievedUser.isPresent());

        // Delete the user
        userMongoDAO.delete(retrievedUser.get().getUserId());

        // Attempt to retrieve the deleted user
        Optional<User> deletedUser = userMongoDAO.getById(userId);

        // Ensure that the user has been deleted
        assertFalse(deletedUser.isPresent());
    }


    private User createUserObject() {

        User user = new User();
        user.setUserId(new ObjectId()); // Установите новый ObjectId
        user.setEmail("newuser@example.com");
        user.setPassword("newuserpassword");
        user.setFirstName("New");
        user.setLastName("User");
        user.setAge((short) 25);

        List<CustomerTicket> tickets = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            CustomerTicket customerTicket = createCustomerTicket();
            customerTicket.setUserId(user.getUserId());

            tickets.add(customerTicket);

        }


        user.setTickets(tickets);

        return user;
    }

    private CustomerTicket createCustomerTicket() {
        Event event = createEventObject();

        CustomerTicket customerTicket = new CustomerTicket();
        customerTicket.setTicketId(new ObjectId());
        customerTicket.setEvent(event);
        customerTicket.setUserId(null);
        customerTicket.setPurchasedDate(new Date());
        customerTicket.setTicketType(new TicketType(null, "Sample Ticket Type"));
        customerTicket.setPrice(50.0);

        return customerTicket;
    }

    private Event createEventObject() {
        Event event = new Event();
        event.setEventId(new ObjectId());
        event.setName("Sample Event");
        event.setDescription("A description of the sample event");
        event.setEventDate(new Date());

        Venue venue = new Venue(null ,"Sample Venue", "Sample City", "Sample Country");
        event.setVenue(venue);

        List<EventCategory> eventCategories = new ArrayList<>();
        eventCategories.add(new EventCategory(null, "EventCategory 1"));
        eventCategories.add(new EventCategory(null, "EventCategory 2"));
        event.setEventCategories(eventCategories);

        List<Ticket> tickets2 = new ArrayList<>();
        tickets2.add(new Ticket(null,new TicketType(null, "Standard"), 60.0, 80));
        tickets2.add(new Ticket(null,new TicketType(null, "Premium"), 85.0, 40));
        event.setTickets(tickets2);

        return event;
    }
}
