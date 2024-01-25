package org.sberbank.api.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
public enum City {

    MINSK("Minsk", "53.8930", "27.5674"),
    MOSCOW("Moscow", "55.7558", "37.6173"),
    TOKYO("Tokyo", "35.6764", "139.6500"),
    LONDON("London", "51.5072", "0.1276");

    private final String name;
    private final String lat;
    private final String lon;

    public static City findCityByName(String name) {
        return Arrays.stream(City.values()).filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such city found with name: " + name));
    }

    @Override
    public String toString() {
        return name;
    }

}