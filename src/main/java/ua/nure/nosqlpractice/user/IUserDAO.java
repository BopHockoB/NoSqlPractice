package ua.nure.nosqlpractice.user;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    void create(User user);
    Optional<User> getById(ObjectId id);
    Optional<User> getByLastName(String name);
    List<User> getAll();
    void update(User user);
    void delete(User user);
    Document userToDocument(User user);
    User documentToUser(Document document);
}
