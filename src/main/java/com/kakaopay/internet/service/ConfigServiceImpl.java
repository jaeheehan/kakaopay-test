package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;
import com.kakaopay.internet.domain.InternetPK;
import com.kakaopay.internet.repository.DeviceRepository;
import com.kakaopay.internet.repository.InternetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    private DeviceRepository deviceRepository;

    private InternetRepository internetRepository;

    @Autowired
    public ConfigServiceImpl(DeviceRepository deviceRepository, InternetRepository internetRepository){
        this.deviceRepository = deviceRepository;
        this.internetRepository = internetRepository;
    }

    @Override
    public List<Device> registerDevices(String devices) {

        List<String> list = Arrays.asList(devices.split(","))
                .stream().skip(2).collect(Collectors.toList());

        List<Device> deviceList = IntStream.range(0, list.size()).mapToObj(idx ->
                new Device(String.format("%s%3s","DIS", idx+1).replace(' ', '0'), list.get(idx))
        ).collect(Collectors.toList());

        deviceRepository.saveAll(deviceList);

        return deviceList;
    }


    @Override
    public List<Internet> saveUseData(List<Device> devices, String[] data){

        List<Internet> useDataList = IntStream.range(0, devices.size()).mapToObj(idx -> {
            Internet internet = new Internet();
            internet.setInternetPK(new InternetPK(Integer.valueOf(data[0]), devices.get(idx)));
            String rate = data[idx + 2];
            internet.setRate("-".equals(rate) ? 0 : Double.valueOf(rate));
            return internet;
        }).collect(Collectors.toList());

        internetRepository.saveAll(useDataList);

        return useDataList;
    }
}
