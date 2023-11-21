package ua.nure.nosqlpractice.event.eventDao;

import org.bson.types.ObjectId;
import ua.nure.nosqlpractice.event.Event;

import java.util.List;
import java.util.Optional;


public interface IEventDAO {
    void create(Event event);
    Optional<Event> getById(ObjectId id);
    Optional<Event> getByName(String name);
    List<Event> getAll();
    void update(Event event);
    void delete(ObjectId id);

}
