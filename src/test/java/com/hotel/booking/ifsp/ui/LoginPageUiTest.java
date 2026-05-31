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

    @Test
    @Tag("UiTest")
    @DisplayName("Switching to register tab shows register form")
    void SwitchingToRegisterTabShowsRegisterForm() {
        LoginPage page = new LoginPage(driver);
        page.switchToRegisterTab();

        assertThat(page.isOnRegisterTab()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Switching back to login tab shows login form")
    void SwitchingBackToLoginTabShowsLoginForm() {
        LoginPage page = new LoginPage(driver);
        page.switchToRegisterTab();
        page.switchToLoginTab();

        assertThat(page.isOnLoginTab()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Register with mismatched passwords shows error message")
    void RegisterWithMismatchedPasswordsShowsErrorMessage() {
        LoginPage page = new LoginPage(driver);
        page.switchToRegisterTab();
        page.register("Ana", "Lima", "ana.lima@test.com",
                "senha123", "senha456");

        assertThat(page.isErrorDisplayed()).isTrue();
        assertThat(page.getErrorMessage()).isEqualTo("As senhas não coincidem.");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Register with short password shows error message")
    void RegisterWithShortPasswordShowsErrorMessage() {
        LoginPage page = new LoginPage(driver);
        page.switchToRegisterTab();
        page.register("Ana", "Lima", "ana.lima@test.com",
                "abc", "abc");

        assertThat(page.isErrorDisplayed()).isTrue();
        assertThat(page.getErrorMessage()).isEqualTo("A senha deve ter pelo menos 6 caracteres.");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Register with duplicate email shows error message")
    void RegisterWithDuplicateEmailShowsErrorMessage() {
        LoginPage page = new LoginPage(driver);
        page.switchToRegisterTab();
        page.register("Admin", "Hotel", "admin@hotel.com",
                "senha123", "senha123");

        assertThat(page.isErrorDisplayed()).isTrue();
        assertThat(page.getErrorMessage()).isEqualTo("Este e-mail já está cadastrado.");
    }
}