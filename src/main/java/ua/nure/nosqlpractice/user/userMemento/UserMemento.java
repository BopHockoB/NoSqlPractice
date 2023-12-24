package ua.nure.nosqlpractice.user.userMemento;

import org.bson.types.ObjectId;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.user.User;

import java.util.List;

public class UserMemento{
    private final ObjectId userId;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final short age;
    private final List<CustomerTicket> tickets;

    public UserMemento(User user){
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.age = user.getAge();
        this.tickets = user.getTickets();
    }

    public User getState() {
        return new User.UserBuilder()
                .setUserId(userId)
                .setEmail(email)
                .setPassword(password)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAge(age)
                .setTickets(tickets)
                .build();
    }
}
