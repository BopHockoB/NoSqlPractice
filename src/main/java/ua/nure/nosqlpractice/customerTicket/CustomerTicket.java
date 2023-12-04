package ua.nure.nosqlpractice.customerTicket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.TicketType;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTicket {

    private ObjectId ticketId;
    private Event event;
    private ObjectId userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchasedDate;
    private TicketType ticketType;
    private Double price;

    private CustomerTicket(CustomerTicketBuilder builder) {
        this.ticketId = builder.ticketId;
        this.event = builder.event;
        this.userId = builder.userId;
        this.purchasedDate = builder.purchasedDate;
        this.ticketType = builder.ticketType;
        this.price = builder.price;
    }

    // Getter methods for CustomerTicket fields (assuming they exist)

    // Create a static inner class for the CustomerTicketBuilder
    public static class CustomerTicketBuilder {
        private ObjectId ticketId;
        private Event event;
        private ObjectId userId;
        private Date purchasedDate;
        private TicketType ticketType;
        private Double price;

        // Builder methods for setting fields
        public CustomerTicketBuilder setTicketId(ObjectId ticketId) {
            this.ticketId = ticketId;
            return this;
        }

        public CustomerTicketBuilder setEvent(Event event) {
            this.event = event;
            return this;
        }

        public CustomerTicketBuilder setUserId(ObjectId userId) {
            this.userId = userId;
            return this;
        }

        public CustomerTicketBuilder setPurchasedDate(Date purchasedDate) {
            this.purchasedDate = purchasedDate;
            return this;
        }

        public CustomerTicketBuilder setTicketType(TicketType ticketType) {
            this.ticketType = ticketType;
            return this;
        }

        public CustomerTicketBuilder setPrice(Double price) {
            this.price = price;
            return this;
        }

        // Build method to create a CustomerTicket instance
        public CustomerTicket build() {
            return new CustomerTicket(this);
        }
    }
    //TODO move serialization and deserialization methods to this class from DAO

}
