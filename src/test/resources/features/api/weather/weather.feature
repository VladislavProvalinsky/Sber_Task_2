Feature: Current weather API Testing

  @positive
  Scenario: Log the difference of actual weather data for cities with expected
    Given The following weather data for several cities
      | name   | temp_c | wind_kph |
      | Minsk  | 0.0    | 19.1     |
      | Moscow | 0.0    | 19.1     |
      | Tokyo  | 1.0    | 13.0     |
      | London | 8.0    | 9.0      |
    When I request the current weather for above cities and parse results
    Then I should analyze and log the difference between actual and expected data from table above

  @negative
  Scenario: Get 1002 error code with 401 HTTP Status Code from Weather API
    Given Request URI with missed 'API key' query parameter
    When I execute GET request with missed 'API key' query parameter
    Then I should get 1002 error code with 401 HTTP Status Code from Weather API

  @negative
  Scenario: Get 1003 error code with 400 HTTP Status Code from Weather API
    Given Request URI with missed 'q' query parameter
    When I execute GET request with missed 'q' query parameter
    Then I should get 1003 error code with 400 HTTP Status Code from Weather API

  @negative
  Scenario: Get 1005 error code with 400 HTTP Status Code from Weather API
    Given Invalid API Request URI
    When I execute GET request with invalid API Request URI
    Then I should get 1005 error code with 400 HTTP Status Code from Weather API

  @negative
  Scenario: Get 1006 error code with 400 HTTP Status Code from Weather API
    Given API Request URI with invalid 'q' location parameter
    When I execute GET request with invalid 'q' location parameter
    Then I should get 1006 error code with 400 HTTP Status Code from Weather API