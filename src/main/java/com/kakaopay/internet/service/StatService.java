package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;

import java.util.List;

public interface StatService {

    List<Device> getDeviceList();

    List<Internet> getTopDeviceEachYear();



}
