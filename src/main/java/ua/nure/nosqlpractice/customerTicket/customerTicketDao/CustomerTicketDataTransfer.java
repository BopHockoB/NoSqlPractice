package ua.nure.nosqlpractice.customerTicket.customerTicketDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.dataTransfer.IDataTransfer;


@Component
@RequiredArgsConstructor
public class CustomerTicketDataTransfer implements IDataTransfer {
    private final CustomerTicketMongoDAO customerTicketMongoDAO;
    private final CustomerTicketMySQLDAO customerTickerMySQLDAO;

    @Override
    public void transferDataToMongo(){
        customerTickerMySQLDAO.getAll().forEach(customerTicketMongoDAO::create);
    }

    @Override
    public void transferDataToMySQL(){
        customerTicketMongoDAO.getAll().forEach(customerTickerMySQLDAO::create);
    }
}
