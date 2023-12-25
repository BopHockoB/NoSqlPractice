package ua.nure.nosqlpractice.user.userDao;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.dbConnections.MySQLConnection;
import ua.nure.nosqlpractice.observers.Observable;
import ua.nure.nosqlpractice.observers.Observer;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.role.roleDAO.IRoleDAO;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Primary
public class UserMySQLDAO implements IUserDAO, Observable {
    private final Connection connection;
    private final List<Observer> observers;
    private final IRoleDAO roleDAO;

    public UserMySQLDAO(IRoleDAO roleDAO) throws SQLException {
        this.roleDAO = roleDAO;
        this.connection = MySQLConnection.getDBSqlConnection();
        this.observers = new ArrayList<>();
    }

    //TODO Implement roles save
    @Override
    public void create(User user) {

        String query = "INSERT INTO user (user_id, email, password, first_name, last_name, age) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUserId().toHexString());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setShort(6, user.getAge());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            notifyObservers(user.toString() + " was inserted in DB");
        }
    }

    @Override
    public Optional<User> getById(ObjectId id) {
        String query = "SELECT * FROM user WHERE user_id = ?";
       return getUser(id.toHexString(), query);
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
      return new User.UserBuilder()
                .setUserId(new ObjectId(resultSet.getString("user_id")))
                .setEmail(resultSet.getString("email"))
                .setPassword(resultSet.getString("password"))
                .setFirstName(resultSet.getString("first_name"))
                .setLastName(resultSet.getString("last_name"))
                .setAge(resultSet.getShort("age"))
                .build();

    }


    @Override
    public Optional<User> getByLastName(String lastName) {
        String query = "SELECT * FROM user WHERE last_name = ?";
        return getUser(lastName, query);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        return getUser(email, query);
    }

    private Optional<User> getUser(String queryParam, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, queryParam);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                   Optional<User> user = Optional.of(mapResultSetToUser(resultSet));
                   user.ifPresent(u -> {
                       u.setRoles(roleDAO.getAllByUserId(u.getUserId()));
                   });
                   return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                userList.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public void update(User user) {
        String query = "UPDATE user SET email = ?, password = ?, first_name = ?, last_name = ?, age = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setShort(5, user.getAge());
            statement.setString(6, user.getUserId().toHexString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            notifyObservers(user.toString() + " was updated in DB");
        }
    }

    @Override
    public void delete(ObjectId id) {
        String query = "DELETE FROM user WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id.toHexString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            notifyObservers("User with id " + id.toHexString() + " was vanished from DB");
        }
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
