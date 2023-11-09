package ua.nure.nosqlpractice.MongoDAOTests;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.EventMongoDAO;
import ua.nure.nosqlpractice.event.Ticket;
import ua.nure.nosqlpractice.mongoDb.MongoConnection;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest

public class EventMongoDAOTest {
    @Autowired
    private MongoConnection connection;
    @Autowired
    private EventMongoDAO eventMongoDAO;

    @Before
    public void setUp() {
        //Drop whole data from collection before testing
         connection.getDatabase().getCollection("Event").drop();

        Event event2 = new Event();
        event2.setEventId(new ObjectId());
        event2.setName("Sample Event 2");
        event2.setDescription("A description of the second sample event");
        event2.setEventDate(new Date());

        String[] address2 = new String[]{"Sample Venue 2", "Sample City 2", "Sample Country 2"};
        event2.setAddress(address2);

        List<String> eventCategories2 = new ArrayList<>();
        eventCategories2.add("Category 3");
        eventCategories2.add("Category 4");
        event2.setEventCategories(eventCategories2);

        List<Ticket> tickets2 = new ArrayList();
        tickets2.add(new Ticket("Standard", 60.0, 80));
        tickets2.add(new Ticket("Premium", 85.0, 40));
        event2.setTickets(tickets2);

        Event event3 = new Event();
        event3.setEventId(new ObjectId());
        event3.setName("Sample Event 3");
        event3.setDescription("A description of the third sample event");
        event3.setEventDate(new Date());

        String[] address3 = new String[]{"Sample Venue 3", "Sample City 3", "Sample Country 3"};
        event3.setAddress(address3);

        List<String> eventCategories3 = new ArrayList<>();
        eventCategories3.add("Category 5");
        eventCategories3.add("Category 6");
        event3.setEventCategories(eventCategories3);

        List<Ticket> tickets3 = new ArrayList();
        tickets3.add(new Ticket("Standard", 60.0, 80));
        tickets3.add(new Ticket("Premium", 85.0, 40));
        event3.setTickets(tickets3);

        eventMongoDAO.create(event2);
        eventMongoDAO.create(event3);

    }

    @Test
    public void testCreateAndGetByName() {
        Event event = createEventObject(); // Create an Event object for testing
        eventMongoDAO.create(event);

        String eventName = event.getName();
        Event retrievedEvent = eventMongoDAO.getByName(eventName).orElse(null);

        assertNotNull(retrievedEvent);
        assertEquals(eventName, retrievedEvent.getName());
    }

    @Test
    public void testGetAll() {
        // Create several events and add them to the database
        List<Event> eventsToInsert = new ArrayList<>();

        List<Event> events = eventMongoDAO.getAll();
        assertFalse(events.isEmpty());
    }

    @Test
    public void testUpdate() {
        Event event = createEventObject();
        eventMongoDAO.create(event);

        event = eventMongoDAO.getByName(event.getName()).get();
        String newEventName = "TestName";
        event.setName(newEventName);

        eventMongoDAO.update(event);

        Event updatedEvent = eventMongoDAO.getByName(newEventName).orElse(null);

        assertNotNull(updatedEvent);
        assertEquals(newEventName, updatedEvent.getName());
    }

    @Test
    public void testDelete() {
        Event event = createEventObject();
        eventMongoDAO.create(event);

        eventMongoDAO.delete(
                event
        );

        Event deletedEvent = eventMongoDAO.getById(event.getEventId()).orElse(null);
        assertNull(deletedEvent);
    }

    private Event createEventObject() {
        Event event = new Event();
        event.setEventId(new ObjectId());
        event.setName("Sample Event");
        event.setDescription("A description of the sample event");
        event.setEventDate(new Date());

        String[] address = new String[]{"Sample Venue", "Sample City", "Sample Country"};
        event.setAddress(address);

        List<String> eventCategories = new ArrayList<>();
        eventCategories.add("Category 1");
        eventCategories.add("Category 2");
        event.setEventCategories(eventCategories);

        List<Ticket> tickets = new ArrayList();
        tickets.add(new Ticket("Standard", 60.0, 80));
        tickets.add(new Ticket("Premium", 85.0, 40));
        event.setTickets(tickets);

        return event;
    }
}
