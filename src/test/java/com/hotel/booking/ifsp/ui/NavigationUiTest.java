package com.hotel.booking.ifsp.ui;

import com.hotel.booking.ifsp.ui.pages.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
@DisplayName("Navigation UI Tests")
class NavigationUiTest extends UiTestBase {

    @Test
    @Tag("UiTest")
    @DisplayName("Login and navigate through main pages")
    void loginAndNavigateThroughMainPages() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@hotel.com", "admin123");

        DashboardPage dashboardPage = new DashboardPage(driver);
        assertThat(dashboardPage.isLoaded()).isTrue();

        SidebarPage sidebarPage = new SidebarPage(driver);

        sidebarPage.goToBookings();
        BookingsPage bookingsPage = new BookingsPage(driver);
        assertThat(bookingsPage.isLoaded()).isTrue();

        sidebarPage.goToGuests();
        GuestsPage guestsPage = new GuestsPage(driver);
        assertThat(guestsPage.isLoaded()).isTrue();

        sidebarPage.logout();
        assertThat(loginPage.isOnLoginTab()).isTrue();
    }
}
