package ua.nure.nosqlpractice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venue venue)) return false;
        if (this.hashCode() != venue.hashCode()) return false;
        return Objects.equals(id, venue.id)
                && Objects.equals(name, venue.name)
                && Objects.equals(city, venue.city)
                && Objects.equals(country, venue.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, country);
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
