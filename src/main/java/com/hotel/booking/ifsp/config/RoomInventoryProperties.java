package com.hotel.booking.ifsp.config;

import com.hotel.booking.ifsp.domain.room.RoomCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hotel.rooms")
@Getter
@Setter
public class RoomInventoryProperties {

    private int standard = 1;
    private int deluxe = 1;
    private int suite = 1;

    public int getCountFor(RoomCategory category) {
        return switch (category) {
            case STANDARD -> standard;
            case DELUXE -> deluxe;
            case SUITE -> suite;
        };
    }
}
