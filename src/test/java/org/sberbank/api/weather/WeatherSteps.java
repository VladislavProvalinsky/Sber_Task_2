package org.sberbank.api.weather;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.sberbank.api.constants.City;
import org.sberbank.api.model.Error;
import org.sberbank.api.model.Weather;
import org.sberbank.api.model.Weather.Current;
import org.sberbank.api.model.Weather.Location;
import org.sberbank.api.service.WeatherService;
import org.sberbank.util.DiffUtil;
import org.sberbank.util.FileWriterUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeatherSteps {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherSteps.class);
    private final List<Weather> expectedCitiesWeather = new ArrayList<>();
    private final List<Weather> actualCitiesWeather = new ArrayList<>();
    private final WeatherService weatherService = new WeatherService();
    private Response response;

    @Given("The following weather data for several cities")
    public void iHaveTheFollowingCitiesWeatherData(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            expectedCitiesWeather.add(Weather.builder()
                    .location(Location.builder()
                            .name(columns.get("name"))
                            .build())
                    .current(Current.builder()
                            .tempC(Double.parseDouble(columns.get("temp_c")))
                            .windKph(Double.parseDouble(columns.get("wind_kph")))
                            .build())
                    .build());
        }
    }

    @When("I request the current weather for above cities and parse results")
    public void iRequestTheCurrentWeatherForCitesAndParseResults() {
        for (Weather weather : expectedCitiesWeather) {
            actualCitiesWeather.add(weatherService.getWeatherForCity(City.findCityByName(weather.getLocation()
                    .getName())));
        }
    }

    @Then("I should analyze and log the difference between actual and expected data from table above")
    public void iShouldAnalyzeAndLogTheDifferenceOfWeatherDataForCities() {
        Allure.step("The difference between expected and actual cities weather is:");
        List<List<String>> table = new ArrayList<>();
        table.add(List.of("name", "temp_c", "wind_kph"));
        for (int i = 0; i < expectedCitiesWeather.size(); i++) {
            String tempCDiff = DiffUtil.getDiffBetweenDoubles(expectedCitiesWeather.get(i).getCurrent().getTempC(),
                    actualCitiesWeather.get(i).getCurrent().getTempC());
            String windKphDiff = DiffUtil.getDiffBetweenDoubles(expectedCitiesWeather.get(i).getCurrent()
                    .getWindKph(), actualCitiesWeather.get(i).getCurrent().getWindKph());
            table.add(List.of(expectedCitiesWeather.get(i).getLocation().getName(), tempCDiff, windKphDiff));
        }
        FileWriterUtil.writeTableToCsvFile(table);
        try {
            Allure.addAttachment("Diff Table", "text/csv", new FileInputStream("WeatherDiff.csv"), ".csv");
        } catch (FileNotFoundException e) {
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @Given("Request URI with missed {string} query parameter")
    public void requestURIWithMissedQueryParameter(String missedParam) {}

    @When("I execute GET request with missed {string} query parameter")
    public void iExecuteGETRequestWithMissedQueryParameter(String missedParam) {
        switch (missedParam) {
            case "q" -> response = weatherService.executeGetWithMissedQParam();
            case "API key" -> response = weatherService.executeGetWithMissedApiKeyParam();
            default -> LOG.error(new IllegalArgumentException(), () -> "Invalid query parameter!");
        }

    }

    @Given("Invalid API Request URI")
    public void invalidAPIRequestURI() {}

    @When("I execute GET request with invalid API Request URI")
    public void iExecuteGETRequestWithInvalidAPIRequestURI() {
        response = weatherService.executeGetWithInvalidAPIRequestURI();
    }

    @Given("API Request URI with invalid {string} location parameter")
    public void apiRequestURIWithInvalidQLocationParameter(String invalidParam) {}

    @When("I execute GET request with invalid {string} location parameter")
    public void iExecuteGETRequestWithInvalidQLocationParameter(String invalidParam) {
        response = weatherService.executeGetWithInvalidLocationParameter();
    }

    @Then("I should get {int} error code with {int} HTTP Status Code from Weather API")
    public void iShouldGetErrorCodeWithHTTPStatusCodeFromWeatherAPI(int errorCode, int httpCode) {
        Error error = response.then()
                .assertThat()
                .statusCode(httpCode)
                .and()
                .extract()
                .body()
                .as(Error.class);
        Assertions.assertEquals(error.getErrorInfo().getCode(), errorCode);
    }

}