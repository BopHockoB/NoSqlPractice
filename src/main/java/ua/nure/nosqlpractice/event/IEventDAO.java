package ua.nure.nosqlpractice.event;

import org.bson.types.ObjectId;

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
