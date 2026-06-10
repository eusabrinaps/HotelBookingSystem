package com.hotel.booking.ifsp.ui;

import com.hotel.booking.ifsp.ui.pages.GuestsPage;
import com.hotel.booking.ifsp.ui.pages.LoginPage;
import com.hotel.booking.ifsp.ui.pages.SidebarPage;
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
}
