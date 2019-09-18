package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;

import java.util.List;

public interface ConfigService {

    List<Device> registerDevices(String devices);

    List<Internet> saveUseData(List<Device> devices, String[] data);
}
