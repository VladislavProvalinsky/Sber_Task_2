package org.sberbank.api.service;

import io.qameta.allure.Allure;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.sberbank.api.constants.City;
import org.sberbank.api.model.Weather;
import org.sberbank.util.AllureDataLogger;
import org.sberbank.util.PropertiesReader;

import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;
import static java.lang.String.format;

/**
 * Current weather API service class implementation for /current.json endpoint
 */
public class WeatherService {

    private final Properties properties = PropertiesReader.loadProperties("/api/api.properties");
    private final AllureDataLogger allureDataLogger = AllureDataLogger.getInstance();

    /**
     * Base Current weather API RequestSpecification builder
     * @return {@link RequestSpecification} instance
     */
    private RequestSpecification getBaseWeatherSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(properties.getProperty("base.api.url"))
                .setBasePath(properties.getProperty("weather.api.endpoint"))
                .setContentType("application/json")
                .addFilter(new RequestLoggingFilter(ALL))
                .build();
    }

    /**
     * Method for getting Weather for special city
     * @param city - {@link City} instance
     * @return {@link Weather} deserialized instance
     */
    public Weather getWeatherForCity(City city) {
        RequestSpecification spec = getBaseWeatherSpec();
        Allure.step(format("Execute GET Weather api call for city %s", city.getName()));
        Response response = given()
                .spec(spec)
                .queryParam("key", properties.getProperty("base.api.key"))
                .queryParam("q", String.join(",", city.getLat(), city.getLon()))
                .when()
                .get();
        return allureDataLogger.logJsonData(response, Method.GET)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .as(Weather.class);
    }

    /**
     * Method for getting weather with missed 'q' parameter
     * @return {@link Response} instance
     */
    public Response executeGetWithMissedQParam() {
        RequestSpecification spec = getBaseWeatherSpec();
        Allure.step("Execute GET Weather api call with missed 'q' parameter");
        Response response = given()
                .spec(spec)
                .queryParam("key", properties.getProperty("base.api.key"))
                .when()
                .get();
        return allureDataLogger.logJsonData(response, Method.GET)
                .then()
                .extract()
                .response();
    }

    /**
     * Method for getting weather with missed 'API key' parameter
     * @return {@link Response} instance
     */
    public Response executeGetWithMissedApiKeyParam() {
        RequestSpecification spec = getBaseWeatherSpec();
        Allure.step("Execute GET Weather api call with missed API key parameter");
        Response response = given()
                .spec(spec)
                .when()
                .get();
        return allureDataLogger.logJsonData(response, Method.GET)
                .then()
                .extract()
                .response();
    }

    /**
     * Method for getting weather with invalid API request URI
     * @return {@link Response} instance
     */
    public Response executeGetWithInvalidAPIRequestURI() {
        RequestSpecification spec = getBaseWeatherSpec();
        Allure.step("Execute GET Weather api call with invalid API request URI");
        Response response = given()
                .spec(spec)
                .basePath(properties.getProperty("weather.api.endpoint").concat("/")
                        .concat(RandomStringUtils.randomAlphabetic(3)))
                .when()
                .get();
        return allureDataLogger.logJsonData(response, Method.GET)
                .then()
                .extract()
                .response();
    }

    /**
     * Method for getting weather with invalid 'q' location parameter
     * @return {@link Response} instance
     */
    public Response executeGetWithInvalidLocationParameter() {
        RequestSpecification spec = getBaseWeatherSpec();
        Allure.step("Execute GET Weather api call with invalid 'q' location parameter");
        Response response = given()
                .spec(spec)
                .queryParam("key", properties.getProperty("base.api.key"))
                .queryParam("q", RandomStringUtils.randomAlphabetic(10))
                .when()
                .get();
        return allureDataLogger.logJsonData(response, Method.GET)
                .then()
                .extract()
                .response();
    }

}