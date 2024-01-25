#language: en
#@allure.weather.api.layer:rest
#@allure.weather.api.owner:upravalinski
Feature: Weather API Testing

  @smoke
  Scenario: Log the difference of actual weather data for cities with expected
    Given I have the following weather data for several cities
      | city   | temperature in celsius | wind speed in kph |
      | Minsk  | 0.0                    | 19.1              |
      | Moscow | 0.0                    | 19.1              |
      | Tokyo  | 1.0                    | 13.0              |
      | London | 8.0                    | 9.0               |
    When I request the current weather for above cities and parse results
    Then I should analyze and log the difference between actual and expected data from table above