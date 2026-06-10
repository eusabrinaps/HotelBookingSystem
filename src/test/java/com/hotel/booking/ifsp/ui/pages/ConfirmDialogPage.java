package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ConfirmDialogPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By backButton = By.xpath("//button[normalize-space(.)='Voltar']");
    private static final By confirmButton = By.xpath("//button[contains(@class,'bg-red-500')]");
    private static final By dialogTitle = By.xpath("//button[normalize-space(.)='Voltar']/ancestor::div[contains(@class,'rounded-2xl')]//h3")

    public ConfirmDialogPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpen() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(backButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isClosed() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(backButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dialogTitle)).getText();
    }

    public void confirm(){
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
    }

    public void back(){
        wait.until(ExpectedConditions.elementToBeClickable(backButton)).click();
    }
}
