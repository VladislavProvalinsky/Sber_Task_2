package org.sberbank.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesReader {

    public static Properties loadProperties(String path) {
        Properties result = new Properties();
        try (InputStream inputStream = PropertiesReader.class.getResourceAsStream(path)) {
            result.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't load properties from resource with path:" + path, e);
        }
        return result;
    }

}