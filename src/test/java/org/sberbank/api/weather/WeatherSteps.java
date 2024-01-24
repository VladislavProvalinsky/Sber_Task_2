package org.sberbank.api.weather;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.sberbank.api.constants.City;
import org.sberbank.api.model.Weather;
import org.sberbank.api.model.Weather.Current;
import org.sberbank.api.model.Weather.Location;
import org.sberbank.api.service.WeatherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.qameta.allure.Allure.step;

public class WeatherSteps {

    private List<Weather> expectedCitiesWeather = new ArrayList<>();
    private List<Weather> actualCitiesWeather = new ArrayList<>();
    private WeatherService weatherService = new WeatherService();

    @Given("^I have the following weather data for several cities$")
    public void iHaveTheFollowingCitiesWeatherData(DataTable table) {
        step("Extracting expected weather data from table");
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            expectedCitiesWeather.add(Weather.builder()
                    .location(Location.builder()
                            .name(columns.get("city"))
                            .build())
                    .current(Current.builder()
                            .tempC(Double.parseDouble(columns.get("temperature in celsius")))
                            .windKph(Double.parseDouble(columns.get("wind speed in kph")))
                            .build())
                    .build());
        }
    }

    @When("^I request the current weather for above cities and parse results$")
    public void iRequestTheCurrentWeatherForCitesAndParseResults() {
        step("Extracting actual weather data from API");
        for (Weather weather : expectedCitiesWeather) {
            actualCitiesWeather.add(weatherService.getWeatherForCity(City.findCityByName(weather.getLocation()
                    .getName())));
        }
    }

    @Then("I should analyze and log the difference between actual data and expected from table above")
    public void iShouldReceiveASuccessfulResponse() {
        // Шаг, в котором вы проверяете успешность ответа
    }

}