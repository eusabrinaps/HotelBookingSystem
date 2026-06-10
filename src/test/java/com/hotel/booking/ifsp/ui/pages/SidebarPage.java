package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SidebarPage extends BasePage {

    private static final By DASHBOARD_BUTTON = By.xpath("//aside//button[contains(normalize-space(.),'Dashboard')]");
    private static final By BOOKINGS_BUTTON = By.xpath("//aside//button[contains(normalize-space(.),'Reservas')]");
    private static final By GUESTS_BUTTON = By.xpath("//aside//button[contains(normalize-space(.),'Hóspedes')]");
    private static final By LOGOUT_BUTTON = By.xpath("//aside//button[contains(normalize-space(.),'Sair')]");

    public SidebarPage(WebDriver driver) {
        super(driver);
    }

    public void goToDashboard() {
        click(DASHBOARD_BUTTON);
    }

    public void goToBookings() {
        click(BOOKINGS_BUTTON);
    }

    public void goToGuests() {
        click(GUESTS_BUTTON);
    }

    public void logout() {
        click(LOGOUT_BUTTON);
    }
}