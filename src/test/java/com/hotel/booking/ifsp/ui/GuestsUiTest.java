package com.hotel.booking.ifsp.ui;

import com.hotel.booking.ifsp.ui.pages.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class GuestsUiTest extends UiTestBase {

    @Test
    @Tag("UiTest")
    @DisplayName("Guests page displays mock guests with name and CPF")
    void shouldDisplayGuestsListWithMockData() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@hotel.com", "admin123");
        assertThat(loginPage.isLoginSuccessful())
                .as("Login deve redirecionar para o dashboard").isTrue();

        SidebarPage sidebar = new SidebarPage(driver);
        sidebar.goToGuests();

        GuestsPage guestsPage = new GuestsPage(driver);
        assertThat(guestsPage.isLoaded())
                .as("Página de hóspedes deve estar carregada").isTrue();

        assertThat(guestsPage.isGuestVisible("Carlos Silva"))
                .as("Hóspede Carlos Silva deve estar visível na lista").isTrue();

        assertThat(guestsPage.isCpfVisible("123.456.789-09"))
                .as("CPF de Carlos Silva deve estar visível na lista").isTrue();
    }

//    @Disabled
//    @Test
//    @Tag("UiTest")
//    @DisplayName("Guest associated to booking appears in guests list")
//    void shouldShowGuestInListAfterBookingCreation() {
//        Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));
//        String guestName = faker.name().fullName();
//        String cpf = "529.982.247-25";
//
//        LoginPage loginPage = new LoginPage(driver);
//        loginPage.login("admin@hotel.com", "admin123");
//        assertThat(loginPage.isLoginSuccessful()).isTrue();
//
//        SidebarPage sidebar = new SidebarPage(driver);
//        sidebar.goToBookings();
//
//        BookingsPage bookingsPage = new BookingsPage(driver);
//        BookingDrawerPage drawer = bookingsPage.openNewBookingDrawer();
//        drawer.fillGuestName(guestName);
//        drawer.fillCpf(cpf);
//        drawer.selectStandardRoom();
//        drawer.fillCheckIn(LocalDate.now().plusDays(30));
//        drawer.fillCheckOut(LocalDate.now().plusDays(32));
//        drawer.confirmReservation();
//
//        assertThat(drawer.isToastVisibleContaining("criada"))
//                .as("Toast de sucesso deve aparecer após criação da reserva").isTrue();
//
//        sidebar.goToGuests();
//
//        GuestsPage guestsPage = new GuestsPage(driver);
//        assertThat(guestsPage.isLoaded())
//                .as("Página de hóspedes deve estar carregada").isTrue();
//        assertThat(guestsPage.isCpfVisible(cpf))
//                .as("CPF do hóspede deve aparecer na lista de hóspedes").isTrue();
//    }
}
