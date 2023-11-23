package ua.nure.nosqlpractice.customerTicket;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.ICustomerTicketDAO;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/customer-tickets")

public class CustomerTicketController {

    @Autowired
    @Qualifier("customerTicketMySQLDAO")
    private ICustomerTicketDAO customerTicketDAO;

    @GetMapping
    public List<CustomerTicket> findAllCustomerTickets() {
        return customerTicketDAO.getAll();
    }

    @GetMapping("/{id}")
    public CustomerTicket findCustomerTicketById(@PathVariable String id) {
        return customerTicketDAO.getById(new ObjectId(id)).orElse(null);
    }

    @PostMapping("/create")
    public HttpStatus createCustomerTicket(@RequestBody CustomerTicket customerTicket) {
        customerTicketDAO.create(customerTicket);
        return HttpStatus.CREATED;
    }

    @PutMapping("/update")
    public HttpStatus updateCustomerTicket(@RequestBody CustomerTicket customerTicket) {
        customerTicketDAO.update(customerTicket);
        return HttpStatus.OK;
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteCustomerTicket(@PathVariable String id) {
        customerTicketDAO.delete(new ObjectId(id));
        return HttpStatus.OK;
    }
}
