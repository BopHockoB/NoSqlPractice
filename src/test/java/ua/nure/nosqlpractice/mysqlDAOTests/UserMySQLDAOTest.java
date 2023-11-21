package ua.nure.nosqlpractice.mysqlDAOTests;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;
import ua.nure.nosqlpractice.user.userDao.UserDAOFactory;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMySQLDAOTest {

    @Autowired
    private UserDAOFactory userDAOFactory;
    private IUserDAO userMySQLDAO;

    @Before
    public void setUp() {
        userMySQLDAO = userDAOFactory.getUserDAO("MYSQL");
    }


    @Test
    public void testCreateAndGetById() {
        // Create a user
        User user = createUserObject();
        userMySQLDAO.create(user);

        ObjectId userId = user.getUserId();
        System.out.println(userId.toHexString());
        // Retrieve the user by ID
        Optional<User> retrievedUserOpt = userMySQLDAO.getById(userId);
        assertTrue(retrievedUserOpt.isPresent());

        User retrievedUser = retrievedUserOpt.get();
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getUserId());
    }
    @Test
    public void testCreateAndGetByLastName() {
        // Create a user
        User user = createUserObject();
        userMySQLDAO.create(user);

        String userLastName = user.getLastName();
        // Retrieve the user by LastName
        Optional<User> retrievedUserOpt = userMySQLDAO.getByLastName(userLastName);
        assertTrue(retrievedUserOpt.isPresent());

        User retrievedUser = retrievedUserOpt.get();
        assertNotNull(retrievedUser);
        assertEquals(userLastName, retrievedUser.getLastName());
    }

    // Other test methods (getAll, update, delete) should be adapted similarly

    @Test
    public void testUpdate() {
        // Create a user
        User user = createUserObject();
        userMySQLDAO.create(user);

        // Retrieve the user by ID
        ObjectId userId = user.getUserId();
        User retrievedUser = userMySQLDAO.getById(userId).orElse(null);
        assertNotNull(retrievedUser);

        // Modify the user's email
        String newEmail = "email" + retrievedUser.getUserId().toHexString() + "@example.com";
        retrievedUser.setEmail(newEmail);

        // Update the user
        userMySQLDAO.update(retrievedUser);

        // Retrieve the updated user
        User updatedUser = userMySQLDAO.getById(userId).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(newEmail, updatedUser.getEmail());
    }

    @Test
    public void testDelete() {
        // Create a user
        User user = createUserObject();
        userMySQLDAO.create(user);

        // Retrieve the user by ID
        ObjectId userId = user.getUserId();
        User retrievedUser = userMySQLDAO.getById(userId).orElse(null);
        assertNotNull(retrievedUser);

        // Delete the user
        userMySQLDAO.delete(userId);

        // Try to retrieve the deleted user
        User deletedUser = userMySQLDAO.getById(userId).orElse(null);
        assertNull(deletedUser);
    }

    private User createUserObject() {
        ObjectId id = new ObjectId();
        return new User.UserBuilder()
                .setUserId(id)
                .setEmail("test" + id.toHexString() +"@example.com")
                .setPassword("password123")
                .setFirstName("John")
                .setLastName("Doe" + id)
                .setAge((short) 30)
                .setTickets(new ArrayList<>())
                .build();
    }
}
