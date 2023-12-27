package ua.nure.nosqlpractice.event.eventDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.dataTransfer.IDataTransfer;

@Component
@RequiredArgsConstructor
public class EventDataTransfer implements IDataTransfer {

    private final EventMongoDAO eventMongoDAO;
    private final EventMySQLDAO eventMySQLDAO;

    @Override
    public void transferDataToMongo(){
        eventMySQLDAO.getAll().forEach(eventMongoDAO::create);
    }

    @Override
    public void transferDataToMySQL(){
        eventMongoDAO.getAll().forEach(eventMySQLDAO::create);
    }
}
