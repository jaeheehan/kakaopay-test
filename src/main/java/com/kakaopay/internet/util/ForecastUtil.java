package com.kakaopay.internet.util;

import com.github.signaflo.timeseries.TimePeriod;
import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;

public class ForecastUtil {

    public static double getForecast(double[] values){

        TimePeriod year = TimePeriod.oneYear();

        TimeSeries series = TimeSeries.from(TimePeriod.oneYear(), values);

        ArimaOrder order = ArimaOrder.order(0, 0, 0, 1,2,1);

        Arima model = Arima.model(series, order, year);

        Forecast forecast = model.forecast(1);

        double result = forecast.pointEstimates().mean();

        result = (result < 0.0) ? 0.0 : result;
        result = (result > 100.0) ? 100.0 : result;

        return result;
    }
}
