package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Venue {
    private Integer id;
    private String name;
    private String city;
    private String country;

    private Venue(VenueBuilder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.city = builder.city;
        this.country = builder.country;
    }

    @NoArgsConstructor
    public static class VenueBuilder{
        private Integer id;
        private String name;
        private String city;
        private String country;

        public Venue build(){
            return new Venue(this);
        }

        public VenueBuilder setId(Integer id) {
            this.id = id;
            return this;
        }

        public VenueBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public VenueBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public VenueBuilder setCountry(String country) {
            this.country = country;
            return this;
        }
    }
}
