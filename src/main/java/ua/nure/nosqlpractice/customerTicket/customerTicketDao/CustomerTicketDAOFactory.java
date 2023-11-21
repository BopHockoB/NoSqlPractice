package ua.nure.nosqlpractice.customerTicket.customerTicketDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerTicketDAOFactory {

    private final CustomerTicketMySQLDAO customerTicketMySQLDAO;
    private final CustomerTicketMongoDAO customerTicketMongoDAO;


    public ICustomerTicketDAO getCustomerTicketDAO(String type) {
        if (type.equalsIgnoreCase("MYSQL")) {
            return customerTicketMySQLDAO;
        } else if (type.equalsIgnoreCase("MONGODB")) {
            return customerTicketMongoDAO;
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }
}
