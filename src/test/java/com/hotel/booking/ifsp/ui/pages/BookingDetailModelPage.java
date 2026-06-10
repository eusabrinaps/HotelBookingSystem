package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BookingDetailModelPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By guestName = By.xpath("//div[contains(@class,'max-w-lg')]//h2");
    private static final By closingButton = By.xpath("//div[contains(@class,'max-w-lg')]//button[contains(@class,'rounded-full')]");
    private static final By cancelButton = By.xpath("//div[contains(@class,'max-w-lg')]//button[contains(.,'Cancelar')]");
    private static final By checkinButton = By.xpath("//div[contains(@class,'max-w-lg')]//button[contains(.,'Check-in')]");
    private static final By checkoutButton = By.xpath("//div[contains(@class,'max-w-lg')]//button[contains(.,'Finalizar estadia')]");

    public BookingDetailModelPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpen(){
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(guestName));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean isClose(){
        try{
            wait.until(ExpectedConditions.invisibilityOfElementLocated(guestName));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public String getGuestName(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(guestName)).getText();
    }

    public void close() {
        wait.until(ExpectedConditions.elementToBeClickable(closingButton)).click();
    }

    public void clickCancel() {
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
    }

    public void clickCheckIn() {
        wait.until(ExpectedConditions.elementToBeClickable(checkinButton)).click();
    }

    public void clickCheckOut() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }

}
