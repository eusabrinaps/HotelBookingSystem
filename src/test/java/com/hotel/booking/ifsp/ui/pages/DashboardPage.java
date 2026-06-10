package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    private static final By TITLE = By.xpath("//h1[normalize-space()='Dashboard']");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(TITLE);
    }
}