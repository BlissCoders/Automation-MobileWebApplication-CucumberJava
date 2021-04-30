Feature: Ascenda Assignment

  Scenario: Test One
    When app launches go to url "https://www.kaligo.com"
    And set Language to "English"
    And set Currency to "SGD"
    Then change loyalty program to "Etihad Guest"
    And set destination to "Singapore" and select first that shows up
    And select checkin date of one month from today and two day later for check out
    And select room as "1"
    And select adult as "2"
    And select children as "0"
    Then search for hotels and result page should show up
    Given all the data above now perform assertions
    Then filter result and look for "Hilton"
    And now make sure "Hilton Singapore" appears in the result
    Then click on the result to navigate to page
