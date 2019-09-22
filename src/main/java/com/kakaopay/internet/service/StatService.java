package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.DeviceList;
import com.kakaopay.internet.domain.InternetUseRow;
import com.kakaopay.internet.domain.InternetUseRowList;
import com.kakaopay.internet.domain.StatResult;

public interface StatService {

    DeviceList getDeviceList();

    InternetUseRowList getTopDeviceEachYear();

    StatResult internetUseTopByYear(int year);

    StatResult internetUseYearTopByDevice(String device_id);

    InternetUseRow forecastUseByYear(String device_id);

    void cacheEvict() throws Exception;

}
