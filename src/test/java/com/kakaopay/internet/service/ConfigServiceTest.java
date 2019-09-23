package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;
import com.kakaopay.internet.domain.InternetPK;
import com.kakaopay.internet.repository.DeviceRepository;
import com.kakaopay.internet.repository.InternetRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class ConfigServiceTest{

    private ConfigServiceImpl configService;

    @Mock
    DeviceRepository deviceRepository;

    @Mock
    InternetRepository internetRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        configService = new ConfigServiceImpl(deviceRepository, internetRepository);
    }

    @Test
    public void registerDevices(){

        Device d1 = new Device("DIS001", "스마트폰");
        Device d2 = new Device("DIS002", "데스크탑");

        List<Device> deviceList = new ArrayList<>();
        deviceList.add(d1);
        deviceList.add(d2);

        given(deviceRepository.saveAll(deviceList)).willReturn(deviceList);

        List<Device> list = configService.registerDevices("기간,이용률,스마트폰,데스크탑");

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getDevice_id()).isEqualTo("DIS001");
        assertThat(list.get(1).getDevice_id()).isEqualTo("DIS002");
        assertThat(list.get(0).getDevice_name()).isNotEqualTo("데스크탑");
        assertThat(list.get(1).getDevice_name()).isEqualTo("데스크탑");

    }

    @Test
    public void setUseData(){

        Device d1 = new Device("DIS001", "스마트폰");
        Device d2 = new Device("DIS002", "데스크탑");
        Device d3 = new Device("DIS003", "기타");

        List<Device> deviceList = new ArrayList<>();
        deviceList.add(d1);
        deviceList.add(d2);
        deviceList.add(d3);

        String data = "2011,52.9,26.3,95.1,-";

        Internet internet1 = new Internet();
        internet1.setInternetPK(new InternetPK(2011, d1));
        internet1.setRate(26.3);

        Internet internet2 = new Internet();
        internet2.setInternetPK(new InternetPK(2011, d2));
        internet2.setRate(95.1);

        Internet internet3 = new Internet();
        internet3.setInternetPK(new InternetPK(2011, d3));
        internet3.setRate(0.0);

        List<Internet> list = Arrays.asList(internet1, internet2, internet3);

        given(internetRepository.saveAll(list)).willReturn(list);

        List<Internet> internetList = configService.saveUseData(deviceList, data.split(","));

        assertThat(internetList.get(0).getRate()).isEqualTo(26.3);
        assertThat(internetList.get(1).getRate()).isEqualTo(95.1);
        assertThat(internetList.get(2).getRate()).isEqualTo(0.0);
    }



}
