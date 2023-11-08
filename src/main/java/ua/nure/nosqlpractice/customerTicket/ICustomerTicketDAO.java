package ua.nure.nosqlpractice.customerTicket;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface ICustomerTicketDAO {

    void create(CustomerTicket ticket);
    Optional<CustomerTicket> getById(ObjectId id);
    List<CustomerTicket> getAll();
    void update(CustomerTicket customerTicket);
    void delete(CustomerTicket customerTicket);

    Document customerTicketToDocument(CustomerTicket customerTicket);
    CustomerTicket documentToCustomerTicket(Document document);

}
