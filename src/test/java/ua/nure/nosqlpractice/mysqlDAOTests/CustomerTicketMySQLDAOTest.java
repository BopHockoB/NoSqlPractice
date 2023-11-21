package ua.nure.nosqlpractice.mysqlDAOTests;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.CustomerTicketMySQLDAO;
import ua.nure.nosqlpractice.event.eventDao.EventMySQLDAO;
import ua.nure.nosqlpractice.user.userDao.UserMySQLDAO;

import java.sql.SQLException;
import java.util.Date;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTicketMySQLDAOTest {

    @Autowired
    private CustomerTicketMySQLDAO customerTicketMySQLDAO;
    @Autowired
    private EventMySQLDAO eventMySQLDAO;
    @Autowired
    private UserMySQLDAO userMySQLDAO;

    @Test
    public void testCreateAndGetById() {
        CustomerTicket customerTicket = createCustomerTicketObject();
        customerTicketMySQLDAO.create(customerTicket);

        ObjectId ticketId = customerTicket.getTicketId();
        CustomerTicket retrievedTicket = customerTicketMySQLDAO.getById(ticketId).orElse(null);

        assertNotNull(retrievedTicket);
        assertEquals(customerTicket.getTicketType(), retrievedTicket.getTicketType());
    }

    // Other test methods (getAll, update, delete) should be adapted similarly

    private CustomerTicket createCustomerTicketObject() {
        return new CustomerTicket.CustomerTicketBuilder()
                .setTicketId(new ObjectId())
                .setPurchasedDate(new Date())
                .setTicketType("Standard")
                .setPrice(60.0)
                .setEvent(eventMySQLDAO.getByName("Sample Event 6557859fe6ea474565b0c8fa").orElse(null))
                .setUserId(userMySQLDAO.getById(new ObjectId("655c99e09a78b31ba88552be")).get().getUserId())
                .build();
    }

    @Test
    public void testUpdate() {
        // Create a customer ticket
        CustomerTicket customerTicket = createCustomerTicketObject();
        customerTicketMySQLDAO.create(customerTicket);

        // Retrieve the customer ticket by its ID
        ObjectId ticketId = customerTicket.getTicketId();
        CustomerTicket retrievedTicket = customerTicketMySQLDAO.getById(ticketId).orElse(null);
        assertNotNull(retrievedTicket);

        // Modify the ticket type
        String newTicketType = "Updated Ticket Type";
        retrievedTicket.setTicketType(newTicketType);

        // Update the customer ticket
        customerTicketMySQLDAO.update(retrievedTicket);

        // Retrieve the updated customer ticket
        CustomerTicket updatedTicket = customerTicketMySQLDAO.getById(ticketId).orElse(null);
        assertNotNull(updatedTicket);
        assertEquals(newTicketType, updatedTicket.getTicketType());
    }

    @Test
    public void testDelete() {
        // Create a customer ticket
        CustomerTicket customerTicket = createCustomerTicketObject();
        customerTicketMySQLDAO.create(customerTicket);

        // Retrieve the customer ticket by its ID
        ObjectId ticketId = customerTicket.getTicketId();
        CustomerTicket retrievedTicket = customerTicketMySQLDAO.getById(ticketId).orElse(null);
        assertNotNull(retrievedTicket);

        // Delete the customer ticket
        customerTicketMySQLDAO.delete(ticketId);

        // Try to retrieve the deleted customer ticket
        CustomerTicket deletedTicket = customerTicketMySQLDAO.getById(ticketId).orElse(null);
        assertNull(deletedTicket);
    }
}
