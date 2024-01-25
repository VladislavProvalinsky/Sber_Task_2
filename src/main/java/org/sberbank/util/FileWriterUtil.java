package org.sberbank.util;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class FileWriterUtil {

    public static void writeTableToCsvFile(List<List<String>> tableData) {
        File file = new File("WeatherDiff.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (List<String> row : tableData) {
                for (String cell : row) {
                    writer.write(cell + ",");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

}
