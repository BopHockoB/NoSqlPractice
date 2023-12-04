package ua.nure.nosqlpractice.event;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private ObjectId eventId;
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date eventDate;

    private Venue venue;
    private List<EventCategory> eventCategories;
    private List<Ticket> tickets;

    private Event(EventBuilder builder) {
        this.eventId = builder.eventId;
        this.name = builder.name;
        this.description = builder.description;
        this.eventDate = builder.eventDate;
        this.venue = builder.address;
        this.eventCategories = builder.eventCategories;
        this.tickets = builder.tickets;
    }

    @NoArgsConstructor
    public static class EventBuilder{
        private ObjectId eventId;
        private String name;
        private String description;
        private Date eventDate;
        private Venue address;
        private List<EventCategory> eventCategories;
        private List<Ticket> tickets;

        public Event build(){
            return new Event(this);
        }

        public EventBuilder setEventId(ObjectId eventId) {
            this.eventId = eventId;
            return this;
        }

        public EventBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder setEventDate(Date eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public EventBuilder setAddress(Venue address) {
            this.address = address;
            return this;
        }

        public EventBuilder setEventCategories(List<EventCategory> eventCategories) {
            this.eventCategories = eventCategories;
            return this;
        }

        public EventBuilder setTickets(List<Ticket> tickets) {
            this.tickets = tickets;
            return this;
        }
    }

    //TODO move serialization and deserialization methods to this class from DAO

}
