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

    @Test
    @Tag("UiTest")
    @DisplayName("Booking form rejects invalid CPF")
    void shouldRejectInvalidCpf() {
        BookingsPage bookingsPage = loginAndGoToBookings();

        BookingDrawerPage drawerPage = bookingsPage.openNewBookingDrawer();
        assertThat(drawerPage.isOpen()).isTrue();

        drawerPage.fillGuestName(faker.name().fullName());
        drawerPage.fillCpf("123");
        drawerPage.selectDeluxeRoom();
        drawerPage.fillCheckIn(LocalDate.now().plusDays(20));
        drawerPage.fillCheckOut(LocalDate.now().plusDays(22));
        drawerPage.confirmReservation();

        assertThat(drawerPage.isOpen()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Booking form rejects checkout before checkin")
    void shouldRejectCheckoutBeforeCheckin() {
        BookingsPage bookingsPage = loginAndGoToBookings();

        BookingDrawerPage drawerPage = bookingsPage.openNewBookingDrawer();
        assertThat(drawerPage.isOpen()).isTrue();

        drawerPage.fillGuestName(faker.name().fullName());
        drawerPage.fillCpf("529.982.247-25");
        drawerPage.selectStandardRoom();
        drawerPage.fillCheckIn(LocalDate.now().plusDays(30));
        drawerPage.fillCheckOut(LocalDate.now().plusDays(29));
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