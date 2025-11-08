package com.example.tests.steps;

import com.example.tests.pages.BorrowingPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BorrowingSteps {

    private WebDriver driver;
    private BorrowingPage page;
    // Use the target URL provided
    private static final String TARGET_URL = "https://www.anz.com.au/personal/home-loans/calculators-tools/borrowing-power-calculator/";

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        page = new BorrowingPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Given("I open the borrowing calculator page")
    public void i_open_the_borrowing_calculator_page() {
        page.open(TARGET_URL);
    }

    @When("I choose {string} application type")
    public void i_choose_application_type(String type) {
        page.chooseApplicationType(type);
    }

    @When("I choose {string} dependants")
    public void i_choose_dependants(String number) {
        page.chooseDependants(number);
    }

    @When("I choose property type {string}")
    public void i_choose_property_type(String propertyText) {
        page.choosePropertyType(propertyText);
    }

    @When("I enter annual income {string}")
    public void i_enter_annual_income(String amount) {
        page.setInputByLabel("Your annual income", amount);
    }

    @When("I enter annual other income {string}")
    public void i_enter_annual_other_income(String amount) {
        page.setInputByLabel("Your annual other income", amount);
    }

    @When("I enter monthly living expenses {string}")
    public void i_enter_monthly_living_expenses(String amount) {
        page.setInputByLabel("Monthly living expenses", amount);
    }

    @When("I enter current home loan repayments {string}")
    public void i_enter_current_home_loan_repayments(String amount) {
        page.setInputByLabel("Current home loan monthly repayments", amount);
    }

    @When("I enter other loan repayments {string}")
    public void i_enter_other_loan_repayments(String amount) {
        page.setInputByLabel("Other loan monthly repayments", amount);
    }

    @When("I enter other commitments {string}")
    public void i_enter_other_commitments(String amount) {
        page.setInputByLabel("Other monthly commitments", amount);
    }

    @When("I enter total credit card limits {string}")
    public void i_enter_total_credit_card_limits(String amount) {
        page.setInputByLabel("Total credit card limits", amount);
    }

    @When("I click calculate")
    public void i_click_calculate() throws InterruptedException {
        page.clickCalculate();
        // Wait a short while for calculation to run / DOM update. In CI use a better wait if flaky.
        Thread.sleep(1500);
    }

    @Then("I should see an estimated borrowing of {string}")
    public void i_should_see_an_estimated_borrowing_of(String expectedNumber) {
        String raw = page.readEstimate(); // e.g. "$547,000"
        // extract digits
        String digits = raw.replaceAll("[^0-9]", "");
        Assert.assertFalse("Estimate was not found on page. Raw: " + raw, digits.isEmpty());
        // Compare numeric values
        long actual = Long.parseLong(digits);
        long expected = Long.parseLong(expectedNumber);
        Assert.assertEquals("Borrowing estimate mismatch", expected, actual);
    }

    // Additional steps for start-over scenario
    @Given("the form is filled with example values")
    public void the_form_is_filled_with_example_values() {
        // Reuse the same as earlier scenario
        i_open_the_borrowing_calculator_page();
        i_choose_application_type("Single");
        i_choose_dependants("0");
        i_choose_property_type("Home to live in");
        i_enter_annual_income("100000");
        i_enter_annual_other_income("10000");
        i_enter_monthly_living_expenses("2000");
        i_enter_current_home_loan_repayments("0");
        i_enter_other_loan_repayments("100");
        i_enter_other_commitments("0");
        i_enter_total_credit_card_limits("10000");
    }

    @When("I click start over")
    public void i_click_start_over() throws InterruptedException {
        page.clickStartOver();
        Thread.sleep(500);
    }

    @Then("the form should be cleared")
    public void the_form_should_be_cleared() {
        // check a few representative fields are empty and estimate reset to $0 or blank
        Assert.assertTrue("Annual income not cleared", page.isInputEmptyByLabel("Your annual income"));
        Assert.assertTrue("Monthly living expenses not cleared", page.isInputEmptyByLabel("Monthly living expenses"));
        String estimate = page.readEstimate().replaceAll("[^0-9]", "");
        // either empty or zero
        if (!estimate.isEmpty()) {
            long val = Long.parseLong(estimate);
            Assert.assertEquals("Estimate not reset", 0L, val);
        }
    }
}
