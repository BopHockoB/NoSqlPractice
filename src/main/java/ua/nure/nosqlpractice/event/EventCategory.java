package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventCategory {
        private Integer id;
        private String categoryName;

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof EventCategory eventCategory)) return false;
                if (this.hashCode() != eventCategory.hashCode()) return false;

                return Objects.equals(id, eventCategory.id)
                        && Objects.equals(categoryName, eventCategory.categoryName);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, categoryName);
        }
}
