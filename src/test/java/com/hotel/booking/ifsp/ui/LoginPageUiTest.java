package com.hotel.booking.ifsp.ui;

import com.hotel.booking.ifsp.ui.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
@DisplayName("LoginPage UI Tests")
class LoginPageUiTest extends UiTestBase {

    @Test
    @Tag("UiTest")
    @DisplayName("Login with valid credentials shows dashboard sidebar")
    void LoginWithValidCredentialsShowsDashboardSidebar() {
        LoginPage page = new LoginPage(driver);
        page.login("admin@hotel.com", "admin123");

        assertThat(page.isLoginSuccessful()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Login with invalid credentials does not navigate to dashboard")
    void LoginWithInvalidCredentialsDoesNotNavigateToDashboard() {
        LoginPage page = new LoginPage(driver);
        page.login("admin@hotel.com", "senhaErrada");

        assertThat(page.isLoginSuccessful()).isFalse();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Login with blank email stays on login page")
    void LoginWithBlankEmailStaysOnLoginPage() {
        LoginPage page = new LoginPage(driver);
        page.login("", "admin123");

        assertThat(page.isOnLoginTab()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Login with blank password stays on login page")
    void LoginWithBlankPasswordStaysOnLoginPage() {
        LoginPage page = new LoginPage(driver);
        page.login("admin@hotel.com", "");

        assertThat(page.isOnLoginTab()).isTrue();
    }
}