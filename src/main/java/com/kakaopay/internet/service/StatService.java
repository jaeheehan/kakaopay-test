package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.*;

import java.util.List;

public interface StatService {

    DeviceList getDeviceList();

    InternetUseRowList getTopDeviceEachYear();

    StatResult internetUseTopByYear(int year);

    StatResult internetUseYearTopByDevice(Device device);

}
