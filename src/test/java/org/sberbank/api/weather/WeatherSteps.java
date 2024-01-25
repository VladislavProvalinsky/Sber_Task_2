package org.sberbank.api.weather;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import org.sberbank.api.constants.City;
import org.sberbank.api.model.Weather;
import org.sberbank.api.model.Weather.Current;
import org.sberbank.api.model.Weather.Location;
import org.sberbank.api.service.WeatherService;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeatherSteps {

    private List<Weather> expectedCitiesWeather = new ArrayList<>();
    private List<Weather> actualCitiesWeather = new ArrayList<>();
    private WeatherService weatherService = new WeatherService();

    @Given("^I have the following weather data for several cities$")
    public void iHaveTheFollowingCitiesWeatherData(DataTable table) {
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
        for (Weather weather : expectedCitiesWeather) {
            actualCitiesWeather.add(weatherService.getWeatherForCity(City.findCityByName(weather.getLocation()
                    .getName())));
        }
    }

    @Then("^I should analyze and log the difference between actual and expected data from table above$")
    public void iShouldAnalyzeAndLogTheDifferenceOfWeatherDataForCities() {
        Allure.step("Found the following differences of cities weather:");
        List<List<String>> raws = new ArrayList<>();
        raws.add(List.of("city", "temperature in celsius", "wind speed in kph"));
        for (int i = 0; i < expectedCitiesWeather.size(); i++) {
            double tempCDiff = Math.abs(expectedCitiesWeather.get(i)
                    .getCurrent()
                    .getTempC() - actualCitiesWeather.get(i)
                    .getCurrent()
                    .getTempC());
            double windKphDiff = Math.abs(expectedCitiesWeather.get(i)
                    .getCurrent()
                    .getWindKph() - actualCitiesWeather.get(i)
                    .getCurrent()
                    .getWindKph());
            raws.add(List.of(expectedCitiesWeather.get(i)
                    .getLocation()
                    .getName(), String.valueOf(tempCDiff), String.valueOf(windKphDiff)));
        }
        DataTable dataTable = DataTable.create(raws);
        Allure.getLifecycle().addAttachment("Diff Table", "text/tab-separated-values", "csv",
                        new ByteArrayInputStream(dataTable.toString().getBytes(StandardCharsets.UTF_8)));
    }

}