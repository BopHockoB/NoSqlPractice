package ua.nure.nosqlpractice.mysqlDAOTests;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.util.DotPath;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.CustomerTicketMySQLDAO;
import ua.nure.nosqlpractice.event.TicketType;
import ua.nure.nosqlpractice.event.eventDao.EventMySQLDAO;
import ua.nure.nosqlpractice.user.userDao.UserMySQLDAO;

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
                .setTicketType(new TicketType(1, "Standard"))
                .setPrice(60.0)
                .setEvent(eventMySQLDAO.getById(new ObjectId("656b0b761489752a3e3cfda4")).orElse(null))
                .setUserId(userMySQLDAO.getById(new ObjectId("6568983c0af578498ea2a797")).orElse(null).getUserId())
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
        Double newPrice = 555.;
        retrievedTicket.setPrice(newPrice);

        // Update the customer ticket
        customerTicketMySQLDAO.update(retrievedTicket);

        // Retrieve the updated customer ticket
        CustomerTicket updatedTicket = customerTicketMySQLDAO.getById(ticketId).orElse(null);
        assertNotNull(updatedTicket);
        assertEquals(newPrice, updatedTicket.getPrice());
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
