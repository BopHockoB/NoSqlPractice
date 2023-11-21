package ua.nure.nosqlpractice.user.userDao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDAOFactory {


    private final UserMongoDAO userMongoDAO;
    private final UserMySQLDAO userMySQLDAO;

    public IUserDAO getUserDAO(String type) {
        if (type.equalsIgnoreCase("MYSQL")) {
            return userMySQLDAO;
        } else if (type.equalsIgnoreCase("MONGODB")) {
            return userMongoDAO;
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }
}
