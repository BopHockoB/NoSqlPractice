package ua.nure.nosqlpractice.mysqlDAOTests;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.event.*;
import ua.nure.nosqlpractice.event.eventDao.EventDAOFactory;
import ua.nure.nosqlpractice.event.eventDao.IEventDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMySQLDAOTest {

    @Autowired
    private EventDAOFactory eventDAOFactory;
    private  IEventDAO eventMySQLDAO;

    @Before
    public void setUp() {
        eventMySQLDAO = eventDAOFactory.getEventDAO("MYSQL");
    }

    @Test
    public void testCreateAndGetById() {
        Event event = createEventObject();
        eventMySQLDAO.create(event);

        ObjectId eventId = event.getEventId();
        Event retrievedEvent = eventMySQLDAO.getById(eventId).orElse(null);

        assertNotNull(retrievedEvent);
        assertEquals(event.getName(), retrievedEvent.getName());
    }

    @Test
    public void testCreateAndGetByName() {
        Event event = createEventObject();
        eventMySQLDAO.create(event);

        String eventName = event.getName();
        Event retrievedEvent = eventMySQLDAO.getByName(eventName).orElse(null);

        assertNotNull(retrievedEvent);
        assertEquals(event.getName(), retrievedEvent.getName());
    }

    // Other test methods (getAll, update, delete) should be adapted similarly

    private Event createEventObject() {
        Event event = new Event();
        event.setEventId(new ObjectId());
        event.setName("Sample Event " + event.getEventId());
        event.setDescription("A description of the sample event");
        event.setEventDate(new Date());

        Venue venue = new Venue(1 ,"Sample Venue", "Sample City", "Sample Country");
        event.setVenue(venue);

        List<EventCategory> eventCategories = new ArrayList<>();
        eventCategories.add(new EventCategory(1, "EventCategory 1"));
        eventCategories.add(new EventCategory(2, "EventCategory 2"));
        event.setEventCategories(eventCategories);

        List<Ticket> tickets = new ArrayList();
        tickets.add(new Ticket(null,new TicketType(1, "Standard"), 60.0, 80));
        tickets.add(new Ticket(null,new TicketType(2, "Premium"), 85.0, 40));
        event.setTickets(tickets);

        return event;
    }

    @Test
    public void testUpdate() {
        // Create an event
        Event event = createEventObject();
        eventMySQLDAO.create(event);

        // Retrieve the event by its name
        String eventName = event.getName();
        Event retrievedEvent = eventMySQLDAO.getByName(eventName).orElse(null);
        assertNotNull(retrievedEvent);

        // Modify the event's name
        String newEventName = "Updated Event Name " + event.getEventId().toHexString();
        retrievedEvent.setName(newEventName);

        // Update the event
        eventMySQLDAO.update(retrievedEvent);

        // Retrieve the updated event
        Event updatedEvent = eventMySQLDAO.getByName(newEventName).orElse(null);
        assertNotNull(updatedEvent);
        assertEquals(newEventName, updatedEvent.getName());
    }

    @Test
    public void testDelete() {
        // Create an event
        Event event = createEventObject();
        eventMySQLDAO.create(event);

        // Retrieve the event by its name
        String eventName = event.getName();
        Event retrievedEvent = eventMySQLDAO.getByName(eventName).orElse(null);
        assertNotNull(retrievedEvent);

        // Delete the event
        eventMySQLDAO.delete(retrievedEvent.getEventId());

        // Try to retrieve the deleted event
        Event deletedEvent = eventMySQLDAO.getByName(eventName).orElse(null);
        assertNull(deletedEvent);
    }
}
