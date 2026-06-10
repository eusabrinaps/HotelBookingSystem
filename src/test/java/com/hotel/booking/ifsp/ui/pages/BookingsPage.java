package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BookingsPage extends BasePage {

    private static final By TITLE = By.xpath("//h1[normalize-space()='Reservas']");
    private static final By NEW_BOOKING_BUTTON = By.xpath("//button[contains(normalize-space(.),'Nova Reserva')]");

    public BookingsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(TITLE);
    }

    public BookingDrawerPage openNewBookingDrawer() {
        click(NEW_BOOKING_BUTTON);
        return new BookingDrawerPage(driver);
    }

    public void search(String text) {
        By searchInput = By.cssSelector("input[placeholder='Buscar por nome, CPF ou ID...']");
        type(searchInput, text);
    }

    public boolean isBookingVisible(String guestName) {
        By row = By.xpath("//table//tbody//tr[contains(.,'" + guestName + "')]");
        return isVisible(row);
    }

    public void clickViewDetails(String guestName) {
        By btn = By.xpath("//table//tbody//tr[contains(.,'" + guestName + "')]//button[@title='Ver detalhes']");
        click(btn);
    }
    public void clickCancelBooking(String guestName) {
        By btn = By.xpath("//table//tbody//tr[contains(.,'" + guestName + "')]//button[@title='Cancelar']");
        click(btn);
    }

    public boolean isCancelButtonVisible(String guestName) {
        By btn = By.xpath("//table//tbody//tr[contains(.,'" + guestName + "')]//button[@title='Cancelar']");
        return !driver.findElements(btn).isEmpty();
    }
}