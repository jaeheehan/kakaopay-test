package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.*;
import com.kakaopay.internet.repository.DeviceRepository;
import com.kakaopay.internet.repository.InternetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatServiceImpl implements StatService {

    DeviceRepository deviceRepository;

    InternetRepository internetRepository;

    @Autowired
    public StatServiceImpl(DeviceRepository deviceRepository, InternetRepository internetRepository){
        this.deviceRepository = deviceRepository;
        this.internetRepository = internetRepository;
    }

    @Cacheable(value = "DeviceList")
    @Override
    public DeviceList getDeviceList() {
        log.info("getDeviceList");
        List<Device> devices = (List<Device>)deviceRepository.findAll();
        return new DeviceList(devices);
    }

    @Override
    public InternetUseRowList getTopDeviceEachYear() {
        List<InternetUseRow> list = internetRepository.findTopYearDevice().stream()
                .map(m -> new InternetUseRow(m)).collect(Collectors.toList());
        return new InternetUseRowList(list);
    }

    @Override
    public StatResult internetUseTopByYear(int year) {
        Internet internet = internetRepository.findTop1ByInternetPKYearOrderByRateDesc(year).stream().findFirst().orElse(null);
        InternetUseRow row = Optional.ofNullable(internet).map( m -> new InternetUseRow(m)).orElse(null);
        return new StatResult(row);
    }

    @Override
    public StatResult internetUseYearTopByDevice(Device device) {
        Internet internet = internetRepository.findTop1ByInternetPKDeviceOrderByRateDesc(device).stream().findFirst().orElse(null);
        InternetUseRow row = Optional.ofNullable(internet).map( m -> new InternetUseRow(m)).orElse(null);
        return new StatResult(row);
    }
}
