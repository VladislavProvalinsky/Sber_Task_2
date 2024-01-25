package org.sberbank.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class DiffUtil {

    public static String getDiffBetweenDoubles(double expected, double actual) {
        expected = BigDecimal.valueOf(expected).setScale(1, RoundingMode.HALF_EVEN).doubleValue();
        actual = BigDecimal.valueOf(actual).setScale(1, RoundingMode.HALF_EVEN).doubleValue();
        return BigDecimal.valueOf(actual - expected).setScale(1, RoundingMode.HALF_EVEN).toString();
    }

}