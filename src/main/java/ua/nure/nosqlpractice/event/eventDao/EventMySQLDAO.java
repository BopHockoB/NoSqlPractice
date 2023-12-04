package ua.nure.nosqlpractice.event.eventDao;

import ch.qos.logback.core.BasicStatusManager;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import ua.nure.nosqlpractice.dbConnections.MySQLConnection;
import ua.nure.nosqlpractice.event.*;
import ua.nure.nosqlpractice.observers.Observable;
import ua.nure.nosqlpractice.observers.Observer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class EventMySQLDAO implements IEventDAO, Observable {

    private final Connection connection;
    private final List<Observer> observers;

    public EventMySQLDAO() throws SQLException {
        connection = MySQLConnection.getDBSqlConnection();
        observers = new ArrayList<>();
    }

    @Override
    public void create(Event event) {
        try {

            String query = "INSERT INTO Event (event_id, name, description, event_date, address_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, event.getEventId().toHexString());
            preparedStatement.setString(2, event.getName());
            preparedStatement.setString(3, event.getDescription());
            preparedStatement.setTimestamp(4, new Timestamp(event.getEventDate().getTime()));
            if (event.getVenue().getId() != null)
                preparedStatement.setInt(5, event.getVenue().getId());
            else
                preparedStatement.setNull(5, Types.INTEGER );
            preparedStatement.executeUpdate();

            if (!event.getTickets().isEmpty() || event.getTickets() != null)
                createTickets(event.getTickets(), event.getEventId());
            if (!event.getEventCategories().isEmpty() || event.getEventCategories() != null)
                linkEventAndCategories(event.getEventCategories(), event.getEventId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            notifyObservers(event.toString() + " was inserted in DB");
        }
    }
    private void createTickets(List<Ticket> tickets, ObjectId eventId) throws SQLException {

            String query = "INSERT INTO Ticket (ticket_type_id, price, available_tickets, event_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (Ticket ticket : tickets) {
                preparedStatement.setInt(1, ticket.getTicketType().getId());
                preparedStatement.setDouble(2, ticket.getPrice());
                preparedStatement.setInt(3, ticket.getAvailableTickets());
                preparedStatement.setObject(4, eventId.toHexString());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

    }

    @Override
    public Optional<Event> getById(ObjectId id) {

        try {
            String query = "SELECT * FROM Event WHERE event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id.toHexString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(mapResultSetToEvent(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Event> getByName(String name) {
        try {
            String query = "SELECT * FROM Event WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(mapResultSetToEvent(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();
        try {
            String query = "SELECT * FROM Event";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                events.add(mapResultSetToEvent(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public void update(Event event) {
        try {
            String query = "UPDATE Event SET name = ?, description = ?, event_date = ?, address_id = ? WHERE event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, event.getName());
            preparedStatement.setString(2, event.getDescription());
            preparedStatement.setTimestamp(3, new Timestamp(event.getEventDate().getTime()));
            if (event.getVenue().getId() != null)
                preparedStatement.setInt(4, event.getVenue().getId());
            else
                preparedStatement.setNull(4, Types.INTEGER);
            preparedStatement.setString(5, event.getEventId().toHexString());
            preparedStatement.executeUpdate();
            
            updateCategoriesKeys(event.getEventCategories(), event.getEventId());
            updateTickets(event.getTickets(), event.getEventId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            notifyObservers(event.toString() + " was updated in DB");
        }
    }

    @Override
    public void delete(ObjectId id) {
        try {
            deleteCategoriesKeys(id);
            deleteTickets(id);
            String query = "DELETE FROM Event WHERE event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id.toHexString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            notifyObservers(id.toHexString() + " was vanished from DB");
        }
    }

    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        if (rs.getObject("event_id") != null) {
            ObjectId eventId = new ObjectId( rs.getString("event_id"));
            String name = rs.getString("name");
            String description = rs.getString("description");
            Date eventDate = rs.getTimestamp("event_date");
            int addressId = rs.getInt("address_id");

            // Retrieve venue details
            Venue venue = getVenueById(addressId);

            // Retrieve event categories associated with this event
            List<EventCategory> categories = getCategoriesByEventId(eventId);

            // Retrieve tickets associated with this event
            List<Ticket> tickets = getTicketsByEventId(eventId);

            return new Event.EventBuilder()
                    .setEventId(eventId)
                    .setName(name)
                    .setDescription(description)
                    .setEventDate(eventDate)
                    .setAddress(venue)
                    .setEventCategories(categories)
                    .setTickets(tickets)
                    .build();
        }
        return null;
    }

    private Venue getVenueById(int addressId) throws SQLException {
        String venueQuery = "SELECT * FROM Address WHERE address_id = ?";
        PreparedStatement venueStatement = connection.prepareStatement(venueQuery);
        venueStatement.setInt(1, addressId);
        ResultSet venueResultSet = venueStatement.executeQuery();

        if (venueResultSet.next()) {
            int venueId = venueResultSet.getInt("address_id");
            String venueName = venueResultSet.getString("venue_name");
            String venueCity = venueResultSet.getString("venue_city");
            String venueCountry = venueResultSet.getString("venue_country");

            return new Venue.VenueBuilder()
                    .setId(venueId)
                    .setName(venueName)
                    .setCity(venueCity)
                    .setCountry(venueCountry)
                    .build();
        }

        return null;
    }

    private List<Ticket> getTicketsByEventId(ObjectId eventId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String ticketsQuery = "SELECT * FROM Ticket WHERE event_id = ?";
        PreparedStatement ticketsStatement = connection.prepareStatement(ticketsQuery);
        ticketsStatement.setObject(1, eventId.toHexString());
        ResultSet ticketsResultSet = ticketsStatement.executeQuery();

        String typeQuery = "SELECT * FROM ticket_type WHERE ticket_type_id = ?";
        PreparedStatement typeStatement = connection.prepareStatement(typeQuery);


        while (ticketsResultSet.next()) {
            int ticketId = ticketsResultSet.getInt("ticket_id");

            int typeId = ticketsResultSet.getInt("ticket_type_id");

            String typeName = null;

            typeStatement.setInt(1, typeId);
            ResultSet typeResultSet = typeStatement.executeQuery();
            if (typeResultSet.next())
               typeName = typeResultSet.getString("type_name");

            double price = ticketsResultSet.getDouble("price");
            int availableTickets = ticketsResultSet.getInt("available_tickets");


            tickets.add(new Ticket(ticketId, new TicketType(typeId, typeName), price, availableTickets));
        }

        return tickets;
    }
    private void updateTickets(List<Ticket> tickets, ObjectId eventId) throws SQLException {
        // First, delete existing tickets for the given event
        deleteTickets(eventId);

        // Then, insert the updated tickets for the event
        String insertQuery = "INSERT INTO Ticket (ticket_type_id, price, available_tickets, event_id) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

        for (Ticket ticket : tickets) {
            insertStatement.setInt(1, ticket.getTicketType().getId());
            insertStatement.setDouble(2, ticket.getPrice());
            insertStatement.setInt(3, ticket.getAvailableTickets());
            insertStatement.setString(4, eventId.toHexString());
            insertStatement.addBatch();
        }

        insertStatement.executeBatch();
    }

    private void deleteTickets(ObjectId eventId) throws SQLException {
        String deleteQuery = "DELETE FROM Ticket WHERE event_id = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setString(1, eventId.toHexString());
        deleteStatement.executeUpdate();
    }

    private void linkEventAndCategories(List<EventCategory> eventCategories, ObjectId eventId) throws SQLException {

        String linkQuery = "INSERT INTO Event_has_Event_Category (event_id, event_category_id) VALUES (?, ?)";
        PreparedStatement linkStatement = connection.prepareStatement(linkQuery);

        for (EventCategory category : eventCategories) {
            linkStatement.setString(1, eventId.toHexString());
            linkStatement.setInt(2, category.getId()); // Assuming getCategoryId retrieves the category ID
            linkStatement.addBatch();
        }

        linkStatement.executeBatch();

    }
    private List<EventCategory> getCategoriesByEventId(ObjectId eventId) throws SQLException {
        List<EventCategory> categories = new ArrayList<>();
        String categoriesQuery = "SELECT ec.event_category_id, ec.category_name " +
                "FROM Event_has_Event_Category ehec " +
                "INNER JOIN Event_Category ec ON ehec.event_category_id = ec.event_category_id " +
                "WHERE ehec.event_id = ?";
        PreparedStatement categoriesStatement = connection.prepareStatement(categoriesQuery);
        categoriesStatement.setObject(1, eventId.toHexString());
        ResultSet categoriesResultSet = categoriesStatement.executeQuery();

        while (categoriesResultSet.next()) {
            int categoryId = categoriesResultSet.getInt("event_category_id");
            String categoryName = categoriesResultSet.getString("category_name");
            categories.add(new EventCategory(categoryId, categoryName));
        }

        return categories;
    }
    private void updateCategoriesKeys( List<EventCategory> eventCategories, ObjectId eventId) throws SQLException {
        // First, delete existing category associations for the given event
        deleteCategoriesKeys(eventId);

        // Then, insert the updated category associations for the event
        linkEventAndCategories(eventCategories, eventId);

    }

    private void deleteCategoriesKeys(ObjectId eventId) throws SQLException {
        String deleteQuery = "DELETE FROM Event_has_Event_Category WHERE event_id = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setString(1, eventId.toHexString());
        deleteStatement.executeUpdate();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object o) {
        observers.forEach(observer -> observer.onDataChanged(o));
    }
}
