package com.hotel.booking.ifsp.ui;

import com.hotel.booking.ifsp.ui.pages.BookingDrawerPage;
import com.hotel.booking.ifsp.ui.pages.BookingsPage;
import com.hotel.booking.ifsp.ui.pages.LoginPage;
import com.hotel.booking.ifsp.ui.pages.SidebarPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
@DisplayName("Validation and Responsive UI Tests")
public class ValidationAndResponsiveUiTest extends UiTestBase {

    private final Faker faker = new Faker();

    @Test
    @Tag("UiTest")
    @DisplayName("Booking form rejects empty required fields")
    void shouldRejectEmptyRequiredFields() {
        BookingsPage bookingsPage = loginAndGoToBookings();

        BookingDrawerPage drawerPage = bookingsPage.openNewBookingDrawer();
        assertThat(drawerPage.isOpen()).isTrue();

        drawerPage.confirmReservation();

        assertThat(drawerPage.isOpen()).isTrue();
    }

    private BookingsPage loginAndGoToBookings() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@hotel.com", "admin123");

        SidebarPage sidebarPage = new SidebarPage(driver);
        sidebarPage.goToBookings();

        BookingsPage bookingsPage = new BookingsPage(driver);
        assertThat(bookingsPage.isLoaded()).isTrue();

        return bookingsPage;
    }
}