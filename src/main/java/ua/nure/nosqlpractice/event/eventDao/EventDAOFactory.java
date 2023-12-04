package ua.nure.nosqlpractice.event.eventDao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class EventDAOFactory {

    private final EventMySQLDAO eventMySQLDAO;
    private final EventMongoDAO eventMongoDAO;

    public EventDAOFactory(EventMySQLDAO eventMySQLDAO, EventMongoDAO eventMongoDAO) {
        this.eventMySQLDAO = eventMySQLDAO;
        this.eventMongoDAO = eventMongoDAO;
    }

    public IEventDAO getEventDAO(String type) {
        if (type.equalsIgnoreCase("MYSQL")) {
            return eventMySQLDAO;
        } else if (type.equalsIgnoreCase("MONGODB")) {
            return eventMongoDAO;
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }
}
