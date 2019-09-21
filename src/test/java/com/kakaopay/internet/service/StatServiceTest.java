package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.*;
import com.kakaopay.internet.repository.DeviceRepository;
import com.kakaopay.internet.repository.InternetRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class StatServiceTest {

    private StatServiceImpl statService;

    @Mock
    DeviceRepository deviceRepository;

    @Mock
    InternetRepository internetRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        statService = new StatServiceImpl(deviceRepository, internetRepository);
    }

    @Test
    public void getDeviceListTest(){

        Device d1 = new Device("DIS001", "스마트폰");
        Device d2 = new Device("DIS002", "데스크탑");

        List<Device> deviceList = new ArrayList<>();
        deviceList.add(d1);
        deviceList.add(d2);

        DeviceList devices = new DeviceList(deviceList);

        // given
        given(deviceRepository.findAll()).willReturn(deviceList);

        // when
        DeviceList resultList = statService.getDeviceList();

        // then
        assertThat(resultList.getDevices().size()).isEqualTo(devices.getDevices().size());
        assertThat(resultList.getDevices()).contains(d1).contains(d2);

    }

    @Test
    public void internetUseTopByYear(){

        Device d1 = new Device("DIS001", "스마트폰");
        Internet i1 = new Internet(new InternetPK(2011, d1), 55.55);

        given(internetRepository.findTop1ByInternetPKYearOrderByRateDesc(2011)).willReturn(Arrays.asList(i1));
        given(internetRepository.findTop1ByInternetPKYearOrderByRateDesc(2099)).willReturn(Collections.emptyList());

        StatResult result1 = statService.internetUseTopByYear(2011);
        StatResult result2 = statService.internetUseTopByYear(2099);

        assertThat(result1.getResult().getDevice_id()).isEqualTo("DIS001");
        assertThat(result2.getResult()).isNull();
    }

    @Test
    public void internetUseYearTopByDevice(){

        Device d1 = new Device("DIS001");
        Internet i1 = new Internet(new InternetPK(2011, d1), 55.55);

        Device d2 = new Device("DIS002");

        given(internetRepository.findTop1ByInternetPKDeviceOrderByRateDesc(d1)).willReturn(Arrays.asList(i1));
        given(internetRepository.findTop1ByInternetPKDeviceOrderByRateDesc(d2)).willReturn(Collections.emptyList());

        StatResult result1 = statService.internetUseYearTopByDevice("DIS001");
        StatResult result2 = statService.internetUseYearTopByDevice("DIS002");

        assertThat(result1.getResult().getDevice_id()).isEqualTo("DIS001");
        assertThat(result2.getResult()).isNull();
    }

    @Test
    public void forecastUseByYearTest(){

        Device device = new Device("DIS001");

        // 52.9, 53.3, 53.4, 57.2, 60.3, 63.2, 68, 68.7
        Device d1 = new Device("DIS001", "스마트폰");
        Internet i1 = new Internet(new InternetPK(2014, d1), 52.9);
        Internet i2 = new Internet(new InternetPK(2015, d1), 53.3);
        Internet i3 = new Internet(new InternetPK(2016, d1), 53.4);
        Internet i4 = new Internet(new InternetPK(2017, d1), 57.2);
        Internet i5 = new Internet(new InternetPK(2018, d1), 60.3);

        List<Internet> list = Arrays.asList(i1, i4, i5, i2, i3);

        given(internetRepository.findByInternetPKDevice(device)).willReturn(list);

        InternetUseRow result = statService.forecastUseByYear("DIS001");

        assertThat(result.getDevice_id()).isNotEqualTo("DIS001");
        assertThat(result.getDevice_name()).isEqualTo("스마트폰");
        assertThat(result.getYear()).isEqualTo(2019);
        assertThat(result.getRate()).isNotZero();


        List<Internet> list_one = Arrays.asList(i5);

        given(internetRepository.findByInternetPKDevice(device)).willReturn(list_one);

        InternetUseRow result_one = statService.forecastUseByYear("DIS001");

        assertThat(result_one).isNull();


        Device device_null = new Device("DIS999");
        List<Internet> list_empty = Collections.EMPTY_LIST;

        given(internetRepository.findByInternetPKDevice(device_null)).willReturn(list_empty);

        InternetUseRow result_empty = statService.forecastUseByYear("DIS999");

        assertThat(result_empty).isNull();
    }
}
