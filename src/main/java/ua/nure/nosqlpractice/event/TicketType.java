package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketType {
    private Integer id;
    private String name;
}
