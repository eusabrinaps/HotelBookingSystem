package com.hotel.booking.ifsp.config;

import com.hotel.booking.ifsp.application.booking.*;
import com.hotel.booking.ifsp.domain.booking.BookingRepository;
import com.hotel.booking.ifsp.domain.guest.GuestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public BookingService bookingService(GuestRepository guestRepository, BookingRepository bookingRepository) {
        return new BookingService(guestRepository, bookingRepository);
    }

    @Bean
    public BookingQueryService bookingQueryService(BookingRepository bookingRepository, GuestRepository guestRepository) {
        return new BookingQueryService(bookingRepository, guestRepository);
    }

    @Bean
    public BookingUpdateService bookingUpdateService(BookingRepository bookingRepository) {
        return new BookingUpdateService(bookingRepository);
    }

    @Bean
    public BookingCancelService bookingCancelService(BookingRepository bookingRepository) {
        return new BookingCancelService(bookingRepository);
    }

    @Bean
    public BookingCheckInOutService bookingCheckInOutService(BookingRepository bookingRepository) {
        return new BookingCheckInOutService(bookingRepository);
    }
}
