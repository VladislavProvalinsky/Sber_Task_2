package org.sberbank.api.service;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.sberbank.api.constants.City;
import org.sberbank.api.model.Weather;
import org.sberbank.util.PropertiesReader;

import java.util.Properties;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private static final Properties properties = PropertiesReader.loadProperties("/api/api.properties");

    private RequestSpecification getBaseWeatherSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(properties.getProperty("base.api.url"))
                .setBasePath(properties.getProperty("weather.api.endpoint"))
                .addQueryParam("key", properties.getProperty("base.api.key"))
                .setContentType(ContentType.JSON)
                .build();
    }

    public Weather getWeatherForCity(City city) {
        log.info(() -> format("[REST - GET] getting weather info for city: %s", city.getName()));
        return given()
                .spec(getBaseWeatherSpec())
                .param("q", String.join(",", city.getLat(), city.getLon()))
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .as(Weather.class);
    }

}