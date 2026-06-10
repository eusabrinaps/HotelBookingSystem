package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

public class BookingDrawerPage extends BasePage {

    private static final By TITLE = By.xpath("//h2[normalize-space()='Nova Reserva']");
    private static final By GUEST_NAME_INPUT = By.cssSelector("input[placeholder='Ex: João Silva']");
    private static final By GUEST_CPF_INPUT = By.cssSelector("input[placeholder='000.000.000-00']");
    private static final By CHECKIN_INPUT = By.xpath("(//input[@type='date'])[1]");
    private static final By CHECKOUT_INPUT = By.xpath("(//input[@type='date'])[2]");
    private static final By CONFIRM_BUTTON = By.xpath("//button[normalize-space()='Confirmar reserva']");
    private static final By CLOSE_BUTTON = By.xpath("//h2[normalize-space()='Nova Reserva']/ancestor::div[contains(@class,'fixed')]//button[.//*[name()='svg']][1]");

    public BookingDrawerPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpen() {
        return isVisible(TITLE);
    }

    public void fillGuestName(String name) {
        type(GUEST_NAME_INPUT, name);
    }

    public void fillCpf(String cpf) {
        type(GUEST_CPF_INPUT, cpf);
    }

    public void selectStandardRoom() {
        click(By.xpath("//button[.//p[normalize-space()='Standard']]"));
    }

    public void selectDeluxeRoom() {
        click(By.xpath("//button[.//p[normalize-space()='Deluxe']]"));
    }

    public void selectSuiteRoom() {
        click(By.xpath("//button[.//p[normalize-space()='Suite']]"));
    }

    public void fillCheckIn(LocalDate checkIn) {
        type(CHECKIN_INPUT, checkIn.toString());
    }

    public void fillCheckOut(LocalDate checkOut) {
        type(CHECKOUT_INPUT, checkOut.toString());
    }

    public void confirmReservation() {
        click(CONFIRM_BUTTON);
    }

    public void close() {
        click(CLOSE_BUTTON);
    }

    public boolean isToastVisibleContaining(String text) {
        By toast = By.xpath("//div[contains(@class,'fixed') and contains(normalize-space(.),'" + text + "')]");
        return isVisible(toast);
    }
}