package com.kakaopay.internet.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class ForecastTest {

    @Test
    public void getForecast(){

        double[] values = {52.9, 53.3, 53.4, 57.2, 60.3, 63.2, 68, 68.7};

        double forecast_value = ForecastUtil.getForecast(values);

        assertThat(forecast_value).isNotZero();
        assertThat(forecast_value).isCloseTo(60, within(20.0));

        double[] values1 = {0, 0};
        double forecast_value1 = ForecastUtil.getForecast(values1);

        assertThat(forecast_value1).isZero();

    }
}
