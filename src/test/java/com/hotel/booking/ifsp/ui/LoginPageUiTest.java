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
}