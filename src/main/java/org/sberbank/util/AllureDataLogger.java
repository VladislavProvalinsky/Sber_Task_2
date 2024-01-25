package org.sberbank.util;


import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AllureDataLogger {

    public static void logJsonData(String stepName, String json) {
        var lifecycle = Allure.getLifecycle();
        lifecycle.getCurrentTestCase().ifPresent(uuid -> {
            final String stepUUID = UUID.randomUUID().toString();
            lifecycle.startStep(stepUUID, new StepResult()
                    .setName(stepName)
                    .setStatus(Status.PASSED));
            lifecycle.addAttachment("JSON", "application/json", "json", json.getBytes());
            lifecycle.stopStep(stepUUID);
        });
    }

}