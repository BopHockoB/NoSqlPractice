package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Integer id;
    private TicketType ticketType;
    private Double price;
    private Integer availableTickets;


    //TODO move serialization and deserialization methods to this class from DAO
}
