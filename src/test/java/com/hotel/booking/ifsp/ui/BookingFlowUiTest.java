package com.hotel.booking.ifsp.ui;

import com.hotel.booking.ifsp.ui.pages.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingFlowUiTest extends UiTestBase {

    private static final String cpfValido = "862.818.248-40";

    @Test
    @Tag("UiTest")
    @DisplayName("Create, search, view and cancel a booking")
    void CreateSearchViewAndCancelBooking() {
        Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));
        String guestName = faker.name().fullName();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@hotel.com", "admin123");
        assertThat(loginPage.isLoginSuccessful())
                .as("Login deve redirecionar para o dashboard").isTrue();

        SidebarPage sidebar = new SidebarPage(driver);
        sidebar.goToBookings();

        BookingsPage bookingsPage = new BookingsPage(driver);
        assertThat(bookingsPage.isLoaded())
                .as("Deve estar na página de Reservas").isTrue();

        BookingDrawerPage drawer = bookingsPage.openNewBookingDrawer();
        assertThat(drawer.isOpen())
                .as("Drawer de nova reserva deve estar aberto").isTrue();

        drawer.fillGuestName(guestName);
        drawer.fillCpf(cpfValido);
        drawer.selectStandardRoom();
        drawer.fillCheckIn(LocalDate.of(2026, 8, 10));
        drawer.fillCheckOut(LocalDate.of(2026, 8, 13));

        drawer.confirmReservation();

        assertThat(bookingsPage.isBookingVisible(guestName))
                .as("Reserva criada deve aparecer na tabela").isTrue();

        bookingsPage.search(guestName);
        assertThat(bookingsPage.isBookingVisible(guestName))
                .as("Reserva deve aparecer no resultado da busca").isTrue();

        bookingsPage.clickViewDetails(guestName);
        BookingDetailModelPage modal = new BookingDetailModelPage(driver);
        assertThat(modal.isOpen())
                .as("Modal de detalhes deve estar aberto").isTrue();
        assertThat(modal.getGuestName())
                .as("Modal deve mostrar o nome do hóspede").contains(guestName);

        modal.close();
        assertThat(modal.isClose())
                .as("Modal deve ter fechado").isTrue();

        bookingsPage.clickCancelBooking(guestName);
        ConfirmDialogPage dialog = new ConfirmDialogPage(driver);
        assertThat(dialog.isOpen())
                .as("Diálogo de confirmação deve aparecer").isTrue();
        dialog.confirm();
        assertThat(dialog.isClosed())
                .as("Diálogo deve fechar após confirmar").isTrue();
        assertThat(bookingsPage.isCancelButtonVisible(guestName))
                .as("Botão cancelar não deve mais existir após cancelamento").isFalse();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Created booking appears in booking list")
    void shouldDisplayCreatedBookingInBookingList() {

        Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));
        String guestName = faker.name().fullName();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@hotel.com", "admin123");

        assertThat(loginPage.isLoginSuccessful()).isTrue();

        SidebarPage sidebar = new SidebarPage(driver);
        sidebar.goToBookings();

        BookingsPage bookingsPage = new BookingsPage(driver);

        BookingDrawerPage drawer =
                bookingsPage.openNewBookingDrawer();

        drawer.fillGuestName(guestName);
        drawer.fillCpf(cpfValido);
        drawer.selectDeluxeRoom();

        drawer.fillCheckIn(LocalDate.now().plusDays(20));
        drawer.fillCheckOut(LocalDate.now().plusDays(22));

        drawer.confirmReservation();

        assertThat(bookingsPage.isBookingVisible(guestName))
                .isTrue();
    }
}

