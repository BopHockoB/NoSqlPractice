package ua.nure.nosqlpractice.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @JsonProperty("tickets") // Annotate the field with the desired JSON field name
    private List<CustomerTicket> tickets;

    //TODO move serialization and deserialization methods to this class from DAO
}
