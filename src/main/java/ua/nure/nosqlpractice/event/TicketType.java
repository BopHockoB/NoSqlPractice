package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketType {
    private Integer id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketType ticketType)) return false;
        if (this.hashCode() != ticketType.hashCode()) return false;
        return Objects.equals(id, ticketType.id)
                && Objects.equals(name, ticketType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
