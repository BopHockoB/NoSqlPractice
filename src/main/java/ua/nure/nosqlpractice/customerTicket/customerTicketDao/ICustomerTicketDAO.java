package ua.nure.nosqlpractice.customerTicket.customerTicketDao;

import org.bson.types.ObjectId;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;

import java.util.List;
import java.util.Optional;

public interface ICustomerTicketDAO {

    void create(CustomerTicket ticket) ;
    Optional<CustomerTicket> getById(ObjectId id);
    List<CustomerTicket> getAll();
    void update(CustomerTicket customerTicket);
    void delete(ObjectId id);

}
