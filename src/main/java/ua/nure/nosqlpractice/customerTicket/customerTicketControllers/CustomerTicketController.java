package ua.nure.nosqlpractice.customerTicket.customerTicketControllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.CustomerTicketMySQLDAO;
import ua.nure.nosqlpractice.customerTicket.customerTicketDao.ICustomerTicketDAO;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.TicketType;
import ua.nure.nosqlpractice.event.eventDao.IEventDAO;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class CustomerTicketController {

    private final ICustomerTicketDAO customerTicketDAO;
    private final IEventDAO eventDAO;
    private final IUserDAO userDAO;
    private String message = "There's no message yet";

    @Autowired
    public CustomerTicketController(@Qualifier("customerTicketMySQLDAO") ICustomerTicketDAO customerTicketDAO,
                                    @Qualifier("eventMySQLDAO") IEventDAO eventDAO,
                                    @Qualifier("userMySQLDAO") IUserDAO userDAO) {
        this.customerTicketDAO = customerTicketDAO;
        this.eventDAO = eventDAO;
        this.userDAO = userDAO;

        ((CustomerTicketMySQLDAO)customerTicketDAO).registerObserver(
                o -> message = o.toString()
        );
    }

    // Display all customer tickets
    @GetMapping
    public String getAllCustomerTickets(Model model) {
        List<CustomerTicket> customerTickets = customerTicketDAO.getAll();
        model.addAttribute("customerTickets", customerTickets);
        model.addAttribute("message", message);
        return "ticket/list";
    }

    // Display form for adding a new customer ticket
    @GetMapping("/new")
    public String showAddCustomerTicketForm(Model model) {
        model.addAttribute("customerTicket",
                new CustomerTicket.CustomerTicketBuilder()
                        .setPurchasedDate(new Date())
                        .setEvent(new Event())
                        .setTicketType(new TicketType())
                        .build()
        );
        model.addAttribute("users", userDAO.getAll());

        List<Event> events = eventDAO.getAll()
                .stream()
                .filter(event -> event.getTickets() != null && !event.getTickets().isEmpty())
                .toList();

        model.addAttribute("events", events);

        //TODO implement ticket type selector

        return "ticket/new";
    }

    // Save a new customer ticket
    @PostMapping("/new")
    public String addCustomerTicket(@ModelAttribute("customerTicket") CustomerTicket customerTicket) {
       customerTicket.setTicketId(new ObjectId());
       customerTicket.setTicketType(new TicketType(1, null));
        customerTicketDAO.create(customerTicket);

        return "redirect:/tickets";
    }

    // Display form for updating a customer ticket by ID
    @GetMapping("/edit/{ticketId}")
    public String showUpdateForm(@PathVariable("ticketId") ObjectId ticketId, Model model) {
        CustomerTicket customerTicket = customerTicketDAO.getById(ticketId).orElse(null);
        model.addAttribute("customerTicket", customerTicket);
        model.addAttribute("users", userDAO.getAll());
        List<Event> events = eventDAO.getAll()
                .stream()
                .filter(event -> event.getTickets() != null && !event.getTickets().isEmpty())
                .toList();

        model.addAttribute("events", events);


        return "ticket/edit";
    }

    // Update a customer ticket
    @PostMapping("/edit/{ticketId}")
    public String updateCustomerTicket(@PathVariable("ticketId") ObjectId ticketId,
                                       @ModelAttribute("customerTicket") CustomerTicket customerTicket) {
        customerTicket.setTicketId(ticketId);
        customerTicket.setTicketType(new TicketType(1, null));
        customerTicketDAO.update(customerTicket);
        return "redirect:/tickets";
    }

    // Delete a customer ticket by ID
    @GetMapping("/delete/{ticketId}")
    public String deleteCustomerTicket(@PathVariable("ticketId") ObjectId ticketId) {
        customerTicketDAO.delete    (ticketId);
        return "redirect:/tickets";
    }
}
