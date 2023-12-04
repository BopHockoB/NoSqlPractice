package ua.nure.nosqlpractice.event.eventControllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.Venue;
import ua.nure.nosqlpractice.event.eventDao.EventMySQLDAO;
import ua.nure.nosqlpractice.event.eventDao.IEventDAO;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private String message = "There's no message yet";
    private final IEventDAO eventDAO;

    public EventController(@Qualifier("eventMySQLDAO") IEventDAO eventDAO) {
        this.eventDAO = eventDAO;
        ((EventMySQLDAO)eventDAO).registerObserver(o -> message = o.toString());
    }

    // Display all events
    @GetMapping
    public String getAllEvents(Model model) {
        List<Event> events = eventDAO.getAll();
        model.addAttribute("events", events);

        model.addAttribute("message", message);
        return "event/list";
    }

    // Display form for adding a new event
    @GetMapping("/new")
    public String showAddEventForm(Model model) {
        Event event = new Event.EventBuilder()
                .setAddress(new Venue())
                .build();

        model.addAttribute("event", event);
        return "event/new";
    }

    // Save a new event
    @PostMapping("/new")
    public String addEvent(@ModelAttribute("event") Event event) {
        event.setEventId(new ObjectId());
        eventDAO.create(event);
        return "redirect:/events";
    }

    // Display form for updating an event by ID
    @GetMapping("/edit/{eventId}")
    public String showUpdateForm(@PathVariable("eventId") ObjectId eventId, Model model) {
        Event event = eventDAO.getById(eventId).orElse(null);
        model.addAttribute("event", event);
        return "event/edit";
    }

    // Update an event
    @PostMapping("/edit/{eventId}")
    public String updateEvent(@PathVariable("eventId") ObjectId eventId, @ModelAttribute("event") Event event) {
        event.setEventId(eventId);
        eventDAO.update(event);
        return "redirect:/events";
    }

    // Delete an event by ID
    @GetMapping("/delete/{eventId}")
    public String deleteEvent(@PathVariable("eventId") ObjectId eventId) {
        eventDAO.delete(eventId);
        return "redirect:/events";
    }
}
