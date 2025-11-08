package com.example.tests.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Small page-object helper with resilient locators.
 */
public class BorrowingPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public BorrowingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForPage() {
        wait.until((ExpectedCondition<Boolean>) d ->
            d.findElements(By.xpath("//*[contains(text(),'We estimate you could borrow') or contains(.,'Your annual income')]")).size() > 0
        );
    }

    public void open(String url) {
        driver.get(url);
        waitForPage();
    }

    // Select application type - Single or Joint (radio)
    public void chooseApplicationType(String type) {
        // radio label contains the type text
        WebElement el = driver.findElement(By.xpath("//label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + type.toLowerCase() + "')]//input[@type='radio']"));
        if (!el.isSelected()) el.click();
    }

    public void chooseDependants(String number) {
        // the dependants are likely buttons/links — find element by exact text number
        WebElement el = driver.findElement(By.xpath("//button[normalize-space()='" + number + "'] | //label[normalize-space()='" + number + "']//input"));
        el.click();
    }

    public void choosePropertyType(String propertyText) {
        WebElement el = driver.findElement(By.xpath("//label[contains(.,'" + propertyText + "')]//input[@type='radio']"));
        if (!el.isSelected()) el.click();
    }

    // Generic method to type a dollar input by finding the label text
    public void setInputByLabel(String labelContains, String value) {
        // Locate label then the following input
        String xpath = "//label[contains(normalize-space(.),'" + labelContains + "')]/following::input[1]";
        WebElement input = driver.findElement(By.xpath(xpath));
        input.clear();
        input.sendKeys(value);
    }

    public void clickCalculate() {
        // Try button text
        WebElement btn = driver.findElement(By.xpath("//button[contains(.,'Work out how much') or contains(.,'Work out how much I could borrow') or contains(.,'Work out') or //*[contains(text(),'Work out how much')]]"));
        btn.click();
    }

    public String readEstimate() {
        // The estimate follows the "We estimate you could borrow" text - find first element with a $ after that heading
        try {
            WebElement el = driver.findElement(By.xpath("//*[contains(.,'We estimate you could borrow')]/following::*[contains(text(),'$')][1]"));
            return el.getText().trim();
        } catch (NoSuchElementException e) {
            // fallback: find any element that looks like $nnn,nnn
            try {
                WebElement el2 = driver.findElement(By.xpath("//*[contains(text(),'$') and string-length(normalize-space())<15][1]"));
                return el2.getText().trim();
            } catch (NoSuchElementException ex) {
                return "";
            }
        }
    }

    public void clickStartOver() {
        WebElement btn = driver.findElement(By.xpath("//button[contains(.,'Start over') or contains(.,'Start over') or //*[contains(text(),'Start over')]]"));
        btn.click();
    }

    // helpers for assertions
    public boolean isInputEmptyByLabel(String labelContains) {
        String xpath = "//label[contains(normalize-space(.),'" + labelContains + "')]/following::input[1]";
        WebElement input = driver.findElement(By.xpath(xpath));
        return input.getAttribute("value") == null || input.getAttribute("value").trim().isEmpty();
    }
}
