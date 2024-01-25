package org.sberbank.util;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.UUID;

/**
 * Special class for logging API data in Allure Report
 */
public final class AllureDataLogger {

    private static AllureDataLogger instance;

    public static synchronized AllureDataLogger getInstance() {
        if (instance == null) {
            instance = new AllureDataLogger();
        }
        return instance;
    }

    public Response logJsonData(Response response, Method method) {
        var lifecycle = Allure.getLifecycle();
        lifecycle.getCurrentTestCase().ifPresent(uuid -> {
            final String step1UUID = UUID.randomUUID().toString();
            lifecycle.startStep(step1UUID, new StepResult()
                    .setName(String.format("Response body for %s api call:", method))
                    .setStatus(Status.PASSED));
            lifecycle.addAttachment("JSON", "application/json", "json", response.asPrettyString().getBytes());
            lifecycle.stopStep(step1UUID);
        });
        return response.then()
                .log()
                .status()
                .log()
                .body()
                .extract()
                .response();
    }

}