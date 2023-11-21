package ua.nure.nosqlpractice.event.eventDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDAOFactory {

    private final EventMySQLDAO eventMySQLDAO;
    private final EventMongoDAO eventMongoDAO;

    public IEventDAO getEventDAO(String type) {
        if (type.equalsIgnoreCase("MYSQL")) {
            return eventMySQLDAO;
        } else if (type.equalsIgnoreCase("MONGODB")) {
            return eventMongoDAO;
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }
}
