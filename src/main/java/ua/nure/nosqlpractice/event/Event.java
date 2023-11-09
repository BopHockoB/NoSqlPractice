package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private ObjectId eventId;
    private String name;
    private String description;
    private Date eventDate;

    //Address field is consists of 3 cells:
    //1st - name of event venue
    //2nd - name of city
    //3rd - name of country
    private String[] address;
    private List<String> eventCategories;

    //Field Tickets is represented by Map where
    //Ticket is the class that contains name and price of a ticket type(Standard, Premium, etc.)
    //and Integer represents amount of available tickets to buy.
    private List<Ticket> Tickets;

    //TODO move serialization and deserialization methods to this class from DAO

}
