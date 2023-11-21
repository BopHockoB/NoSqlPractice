package ua.nure.nosqlpractice.customerTicket.customerTicketDao;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.dbConnections.MySQLConnection;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.eventDao.EventDAOFactory;
import ua.nure.nosqlpractice.event.eventDao.IEventDAO;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerTicketMySQLDAO implements ICustomerTicketDAO {

    private final Connection connection;
    private final IEventDAO eventDAO;

    @Autowired
    public CustomerTicketMySQLDAO(EventDAOFactory eventDAOFactory) throws SQLException {
        connection = MySQLConnection.getDBSqlConnection();
        eventDAO = eventDAOFactory.getEventDAO("MySql");
    }

    @Override
    public void create(CustomerTicket ticket) {
        try {
            String query = "INSERT INTO customer_ticket (cutomer_ticket_id, purchased_date, ticket_type, price, event_id, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ticket.getTicketId().toHexString());
            preparedStatement.setTimestamp(2, new Timestamp(ticket.getPurchasedDate().getTime()));
            preparedStatement.setString(3, ticket.getTicketType());
            preparedStatement.setDouble(4, ticket.getPrice());
            preparedStatement.setString(5, ticket.getEvent().getEventId().toHexString());
            preparedStatement.setString(6, ticket.getUserId().toHexString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<CustomerTicket> getById(ObjectId id) {
        try {
            String query = "SELECT * FROM customer_ticket WHERE cutomer_ticket_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id.toHexString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(mapResultSetToCustomerTicket(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<CustomerTicket> getAll() {
        List<CustomerTicket> customerTickets = new ArrayList<>();
        try {

            String query = "SELECT * FROM customer_ticket";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                customerTickets.add(mapResultSetToCustomerTicket(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerTickets;
    }

    @Override
    public void update(CustomerTicket customerTicket) {
        try {
            String query = "UPDATE customer_ticket SET purchased_date = ?, ticket_type = ?, price = ?, event_id = ?, user_id = ? WHERE cutomer_ticket_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, new Timestamp(customerTicket.getPurchasedDate().getTime()));
            preparedStatement.setString(2, customerTicket.getTicketType());
            preparedStatement.setDouble(3, customerTicket.getPrice());
            preparedStatement.setString(4, customerTicket.getEvent().getEventId().toHexString());
            preparedStatement.setString(5, customerTicket.getUserId().toHexString());
            preparedStatement.setString(6, customerTicket.getTicketId().toHexString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ObjectId id) {
        try {
            String query = "DELETE FROM customer_ticket WHERE cutomer_ticket_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id.toHexString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CustomerTicket mapResultSetToCustomerTicket(ResultSet rs) throws SQLException {
        ObjectId customerId = new ObjectId(rs.getString("cutomer_ticket_id"));
        Timestamp purchasedDate = rs.getTimestamp("purchased_date");
        String ticketType = rs.getString("ticket_type");
        double price = rs.getDouble("price");
        Optional<Event> event = eventDAO.getById(new ObjectId(rs.getString("event_id")));
        ObjectId userId = new ObjectId(rs.getString("user_id"));

        return new CustomerTicket.CustomerTicketBuilder()
                .setTicketId(customerId)
                .setPrice(price)
                .setTicketType(ticketType)
                .setEvent(event.orElse(null))
                .setUserId(userId)
                .setPurchasedDate(purchasedDate)
                .build();


    }
}
