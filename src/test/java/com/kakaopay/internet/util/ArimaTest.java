package com.kakaopay.internet.util;

import com.github.signaflo.math.stats.distributions.Normal;
import com.github.signaflo.timeseries.TimePeriod;
import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;
import org.junit.Test;

public class ArimaTest {

    @Test
    public void internetUse(){

        TimePeriod year = TimePeriod.oneYear();

        // double[] values = {52.9, 53.3, 53.4, 57.2, 60.3, 63.2, 68, 68.7};
        // double[] values = {26.3, 33.5, 64.3, 64.2, 73.2, 85.1, 90.6, 90.5};
        // double[] values = {95.1, 93.9, 67.1, 61.5, 61.9, 58.5, 61.4, 51.2};
        // double[] values = {14.3, 13.1, 14.7, 14.1, 13.9, 19.3, 10.8, 17.3};
        double[] values = {9.5, 1.9, 4.4, 0.1, 0.2, 0.2, 0.2, 0.3};

        // double[] values = {0, 0, 0, 0.9, 2.3, 2.1, 2, 3.3};
        // double[] values = {0, 0};

        TimeSeries series = TimeSeries.from(year, values);

        ArimaOrder order = ArimaOrder.order(0, 0, 0, 1,2,1);

        Arima model = Arima.model(series, order, year);

        Forecast forecast = model.forecast(7);

        System.out.println(forecast.toString());
    }

}
