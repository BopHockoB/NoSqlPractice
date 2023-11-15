package ua.nure.nosqlpractice.event;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public class EventMySQLDAO implements IEventDAO{
    @Override
    public void create(Event event) {

    }

    @Override
    public Optional<Event> getById(ObjectId id) {
        return Optional.empty();
    }

    @Override
    public Optional<Event> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Event> getAll() {
        return null;
    }

    @Override
    public void update(Event event) {

    }

    @Override
    public void delete(ObjectId id) {
        new ObjectId(String.valueOf(id.getDate().getTime()));
    }
}
