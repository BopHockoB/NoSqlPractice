package ua.nure.nosqlpractice.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;

import ua.nure.nosqlpractice.customerTicket.CustomerTicket;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonProperty("_id") // Annotate the field with the desired JSON field name
    private ObjectId userId;
    @JsonProperty("email") // Annotate the field with the desired JSON field name
    private String email;
    @JsonProperty("password") // Annotate the field with the desired JSON field name
    private String password;
    @JsonProperty("firstName") // Annotate the field with the desired JSON field name
    private String firstName;
    @JsonProperty("lastName") // Annotate the field with the desired JSON field name
    private String lastName;
    @JsonProperty("age") // Annotate the field with the desired JSON field name
    private short age;

    private List<CustomerTicket> tickets;

    private User(UserBuilder builder){
        this.userId = builder.userId;
        this.email = builder.email;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.tickets = builder.tickets;
    }

    @NoArgsConstructor
    public static class UserBuilder{
        private ObjectId userId;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private short age;
        private List<CustomerTicket> tickets;

        public User build(){
            return new User(this);
        }

        public UserBuilder setUserId(ObjectId userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setAge(short age) {
            this.age = age;
            return this;
        }

        public UserBuilder setTickets(List<CustomerTicket> tickets) {
            this.tickets = tickets;
            return this;
        }
    }

    //TODO move serialization and deserialization methods to this class from DAO
}
