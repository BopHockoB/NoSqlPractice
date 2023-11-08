package ua.nure.nosqlpractice.event;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final IEventDAO eventDAO;

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
        eventDAO.delete(new Event(new ObjectId(id), null, null, null, null, null, null));
        return HttpStatus.OK;
    }
}
