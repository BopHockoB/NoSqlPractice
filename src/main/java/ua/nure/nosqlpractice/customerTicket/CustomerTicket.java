package ua.nure.nosqlpractice.customerTicket;

import lombok.*;
import org.bson.types.ObjectId;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.event.Ticket;

import java.util.Date;
import java.util.List;
import java.util.UUID;


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

    public CustomerTicket(CustomerTicketBuilder builder) {
        this.ticketId = builder.ticketId;
        this.event = builder.event;
        this.userId = builder.userId;
        this.purchasedDate = builder.purchasedDate;
        this.ticketType = builder.ticketType;
        this.price = builder.price;
    }


    @NoArgsConstructor
    public static class CustomerTicketBuilder {
        private ObjectId ticketId;
        private Event event;
        private ObjectId userId;
        private Date purchasedDate;
        private String ticketType;
        private Double price;

        public CustomerTicket build() {
            return new CustomerTicket(this);
        }

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

        public CustomerTicketBuilder setTicketType(String ticketType) {
            this.ticketType = ticketType;
            return this;
        }

        public CustomerTicketBuilder setPrice(Double price) {
            this.price = price;
            return this;
        }

        //TODO move serialization and deserialization methods to this class from DAO

    }
}