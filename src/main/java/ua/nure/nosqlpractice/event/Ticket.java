package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Integer id;
    private TicketType ticketType;
    private Double price;
    private Integer availableTickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;
        if (this.hashCode() != ticket.hashCode()) return false;
        return  Objects.equals(ticketType, ticket.ticketType)
                && Objects.equals(price, ticket.price)
                && Objects.equals(availableTickets, ticket.availableTickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticketType, price, availableTickets);
    }

//TODO move serialization and deserialization methods to this class from DAO
}
