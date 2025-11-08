Feature: Borrowing calculator

  Background:
    Given I open the borrowing calculator page

  Scenario: Calculate borrowing power for single applicant and expected estimate
    When I choose "Single" application type
    And I choose "0" dependants
    And I choose property type "Home to live in"
    And I enter annual income "100000"
    And I enter annual other income "10000"
    And I enter monthly living expenses "2000"
    And I enter current home loan repayments "0"
    And I enter other loan repayments "100"
    And I enter other commitments "0"
    And I enter total credit card limits "10000"
    And I click calculate
    Then I should see an estimated borrowing of "547000"

  Scenario: Start over clears the form
    Given the form is filled with example values
    When I click start over
    Then the form should be cleared
