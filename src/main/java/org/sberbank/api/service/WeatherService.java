package org.sberbank.api.service;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.qameta.allure.Allure;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.sberbank.api.constants.City;
import org.sberbank.api.model.Weather;
import org.sberbank.util.AllureDataLogger;
import org.sberbank.util.PropertiesReader;

import java.util.Properties;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.METHOD;
import static io.restassured.filter.log.LogDetail.URI;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private static final Properties properties = PropertiesReader.loadProperties("/api/api.properties");

    private RequestSpecification getBaseWeatherSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(properties.getProperty("base.api.url"))
                .setBasePath(properties.getProperty("weather.api.endpoint"))
                .addQueryParam("key", properties.getProperty("base.api.key"))
                .setContentType("application/json")
                .addFilter(new RequestLoggingFilter(ALL))
                .build();
    }

    public Weather getWeatherForCity(City city) {
        RequestSpecification spec = getBaseWeatherSpec();
        var urlLogString = ((RequestSpecificationImpl) spec).getBaseUri()
                .concat(((RequestSpecificationImpl) spec).getBasePath());
        Allure.step(format("Execute GET Weather api call %s for city %s", city.getName(), urlLogString));
        Response response = given()
                .spec(spec)
                .queryParam("q", String.join(",", city.getLat(), city.getLon()))
                .when()
                .get();
        return logResponseBodyWithTimeStampAndExtract(response, Method.GET, urlLogString)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .as(Weather.class);
    }

    private Response logResponseBodyWithTimeStampAndExtract(Response response, Method method, String urlLogString) {
        AllureDataLogger.logJsonData(String.format("Response body for %s api call:", method),
                response.asPrettyString());
        response.then()
                .log()
                .status()
                .log()
                .body();
        long executionTime = response.getTimeIn(MILLISECONDS);
        log.info(() -> format("Execution time for %s method %s is %d milliseconds", method, urlLogString,
                executionTime));
        return response;
    }

}