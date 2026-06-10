package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ConfirmDialogPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ConfirmDialogPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }



}
