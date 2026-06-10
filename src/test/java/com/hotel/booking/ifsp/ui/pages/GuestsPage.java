package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GuestsPage extends BasePage {

    private static final By TITLE = By.xpath("//h1[normalize-space()='Hóspedes']");

    public GuestsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(TITLE);
    }
}