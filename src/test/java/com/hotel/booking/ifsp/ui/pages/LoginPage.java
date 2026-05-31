package com.hotel.booking.ifsp.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By LOGIN_TAB     = By.xpath("//div[contains(@class,'grid-cols-2')]//button[1]");
    private static final By REGISTER_TAB  = By.xpath("//div[contains(@class,'grid-cols-2')]//button[2]");

    // Login form
    private static final By EMAIL_INPUT    = By.cssSelector("input[type='email']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[type='password']");
    private static final By SUBMIT_BUTTON  = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE  = By.cssSelector("p.text-red-500");

    // Register form
    private static final By NAME_INPUT            = By.cssSelector("input[placeholder='João']");
    private static final By LASTNAME_INPUT        = By.cssSelector("input[placeholder='Silva']");
    private static final By REG_PASSWORD_INPUT    = By.xpath("(//input[@type='password'])[1]");
    private static final By REG_CONFIRM_INPUT     = By.xpath("(//input[@type='password'])[2]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void switchToRegisterTab() {
        driver.findElement(REGISTER_TAB).click();
    }

    public void switchToLoginTab() {
        driver.findElement(LOGIN_TAB).click();
    }

    public void login(String email, String password) {
        driver.findElement(EMAIL_INPUT).sendKeys(email);
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
        driver.findElement(SUBMIT_BUTTON).click();
    }

    public void register(String name, String lastname, String email, String password, String confirm) {
        driver.findElement(NAME_INPUT).sendKeys(name);
        driver.findElement(LASTNAME_INPUT).sendKeys(lastname);
        driver.findElement(EMAIL_INPUT).sendKeys(email);
        driver.findElement(REG_PASSWORD_INPUT).sendKeys(password);
        driver.findElement(REG_CONFIRM_INPUT).sendKeys(confirm);
        driver.findElement(SUBMIT_BUTTON).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)).getText();
    }

    public boolean isErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnLoginTab() {
        return !driver.findElements(By.xpath("//h1[contains(text(),'Bem-vindo')]")).isEmpty();
    }

    public boolean isOnRegisterTab() {
        return !driver.findElements(By.xpath("//h1[contains(text(),'Criar conta')]")).isEmpty();
    }

    // O app não usa React Router — após login a Sidebar aparece com os itens de navegação
    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("aside")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
