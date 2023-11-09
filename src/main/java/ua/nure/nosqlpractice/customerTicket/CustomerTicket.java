package ua.nure.nosqlpractice.customerTicket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import ua.nure.nosqlpractice.event.Event;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTicket {

    private ObjectId ticketId;
    private Event event;
    private ObjectId userId;
    private Date purchasedDate;
    private String ticketType;
    private Double price;

    //TODO move serialization and deserialization methods to this class from DAO

}
