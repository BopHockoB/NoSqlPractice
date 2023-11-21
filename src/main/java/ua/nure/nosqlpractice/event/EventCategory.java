package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventCategory {
        private Integer id;
        private String categoryName;

}
