package ua.nure.nosqlpractice.mongoDAOTests;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.CustomerTicketMongoDAO;
import ua.nure.nosqlpractice.event.*;
import ua.nure.nosqlpractice.event.eventDao.EventMongoDAO;


import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTicketMongoDAOTest {
    @Autowired
    private CustomerTicketMongoDAO customerTicketMongoDAO;
    @Autowired
    private EventMongoDAO eventMongoDAO;

    @Before
    public void setUp() {
        customerTicketMongoDAO.getAll().forEach(ticket -> customerTicketMongoDAO.delete(ticket.getTicketId()));
    }

    @Test
    public void testCreateAndGetById() {
        CustomerTicket customerTicket = createCustomerTicket(); // Создайте объект CustomerTicket для тестирования
        customerTicketMongoDAO.create(customerTicket);

        ObjectId ticketId = customerTicket.getTicketId();
        CustomerTicket retrievedTicket = customerTicketMongoDAO.getById(ticketId).orElse(null);

        assertNotNull(retrievedTicket);
        assertEquals(ticketId, retrievedTicket.getTicketId());
    }

    @Test
    public void testUpdate() {
        CustomerTicket customerTicket = createCustomerTicket();
        customerTicketMongoDAO.create(customerTicket);
        eventMongoDAO.create(customerTicket.getEvent());

        customerTicket = customerTicketMongoDAO.getById(customerTicket.getTicketId()).get();
        String newTicketType = "Updated Ticket Type";
        customerTicket.setTicketType(newTicketType);

        customerTicketMongoDAO.update(customerTicket);

        CustomerTicket updatedTicket = customerTicketMongoDAO.getById(customerTicket.getTicketId()).orElse(null);

        assertNotNull(updatedTicket);
        assertEquals(newTicketType, updatedTicket.getTicketType());
    }

    @Test
    public void testDelete() {
        CustomerTicket customerTicket = createCustomerTicket();
        customerTicketMongoDAO.create(customerTicket);

        customerTicketMongoDAO.delete(customerTicket.getTicketId());

        CustomerTicket deletedTicket = customerTicketMongoDAO.getById(customerTicket.getTicketId()).orElse(null);
        assertNull(deletedTicket);
    }

    private CustomerTicket createCustomerTicket() {
        Event event = createEventObject();

        CustomerTicket customerTicket = new CustomerTicket();
        customerTicket.setTicketId(new ObjectId());
        customerTicket.setEvent(event);
        customerTicket.setUserId(null);
        customerTicket.setPurchasedDate(new Date());
        customerTicket.setTicketType("Sample Ticket Type");
        customerTicket.setPrice(50.0);

        return customerTicket;
    }

    private Event createEventObject() {
        Event event = new Event();
        event.setEventId(new ObjectId());
        event.setName("Sample Event");
        event.setDescription("A description of the sample event");
        event.setEventDate(new Date());

        Venue venue = new Venue.VenueBuilder()
                .setName("Sample Venue")
                .setCity("Sample City")
                .setCountry("Sample Country")
                .build();
        event.setVenue(venue);

        List<EventCategory> eventCategories = new ArrayList<>();
        eventCategories.add(new EventCategory(null ,"Category 1"));
        eventCategories.add(new EventCategory(null ,"Category 2"));
        event.setEventCategories(eventCategories);

        List<Ticket> tickets = new ArrayList();
        tickets.add(new Ticket(null,"Standard", 60.0, 80));
        tickets.add(new Ticket(null,"Premium", 85.0, 40));
        event.setTickets(tickets);

        return event;
    }

}
