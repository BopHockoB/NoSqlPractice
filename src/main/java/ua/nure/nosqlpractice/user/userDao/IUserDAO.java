package ua.nure.nosqlpractice.user.userDao;

import org.bson.types.ObjectId;
import ua.nure.nosqlpractice.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    void create(User user);
    Optional<User> getById(ObjectId id);
    Optional<User> getByLastName(String name);
    List<User> getAll();
    void update(User user);
    void delete(ObjectId id);

    Optional<User> getByEmail(String email);
}
