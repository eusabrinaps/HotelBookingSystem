package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GuestsPage extends BasePage {

    private static final By TITLE    = By.xpath("//h1[normalize-space()='Hóspedes']");
    private static final By SUBTITLE = By.xpath("//p[contains(text(),'hóspedes cadastrados')]");

    public GuestsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(TITLE);
    }

    public String getSubtitleText() {
        return text(SUBTITLE);
    }

    public boolean isGuestVisible(String name) {
        return isVisible(By.xpath("//td[normalize-space()='" + name + "']"));
    }

    public boolean isCpfVisible(String cpf) {
        return isVisible(By.xpath("//td[normalize-space()='" + cpf + "']"));
    }
}