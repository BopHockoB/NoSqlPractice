package ua.nure.nosqlpractice.event;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosqlpractice.event.eventDao.IEventDAO;

import java.util.List;

@RestController
@RequestMapping("/event")

public class EventController {
    @Autowired
    @Qualifier("eventMongoDAO")
    private IEventDAO eventDAO;

    @GetMapping
    public List<Event> findAllEvents() {
        return eventDAO.getAll();
    }

    @GetMapping("/{id}")
    public Event findEventById(@PathVariable String id) {
        return eventDAO.getById(new ObjectId(id)).orElse(null);
    }

    @GetMapping("/name/{name}")
    public Event findEventByName(@PathVariable String name) {
        return eventDAO.getByName(name).orElse(null);
    }

    @PostMapping("/create")
    public HttpStatus createEvent(@RequestBody Event event) {
        eventDAO.create(event);
        return HttpStatus.CREATED;
    }

    @PutMapping("/update")
    public HttpStatus updateEvent(@RequestBody Event event) {
        eventDAO.update(event);
        return HttpStatus.OK;
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteEvent(@PathVariable String id) {
        eventDAO.delete(new ObjectId(id));
        return HttpStatus.OK;
    }
}
