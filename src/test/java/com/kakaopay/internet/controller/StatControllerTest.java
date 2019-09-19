package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.DeviceList;
import com.kakaopay.internet.domain.InternetUseRow;
import com.kakaopay.internet.domain.InternetUseRowList;
import com.kakaopay.internet.service.StatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class StatControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private StatService statService;

    private MockMvc mvc;

    @Before
    public void setUp(){
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Configuration
    @EnableWebMvc
    public static class TestConfiguration {

        @MockBean
        private StatService statService;

        @Bean
        public StatController StatController() {
            return new StatController(statService);
        }

        @Bean
        public StatService statService(){
            return statService;
        }
    }

    @Test
    public void getDevices() throws Exception {

        Device d1 = new Device("DIS001", "스마트폰");
        Device d2 = new Device("DIS002", "데스크탑");

        List<Device> deviceList = new ArrayList<>();
        deviceList.add(d1);
        deviceList.add(d2);


        given(statService.getDeviceList()).willReturn(new DeviceList(deviceList));

        mvc.perform(get("/api/devices").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.devices").isArray())
                .andExpect(jsonPath("@.devices.length()").value(2))
                .andExpect(jsonPath("$.devices[0].device_id").value("DIS001"))
        ;
    }

    @Test
    public void getTopDeviceEachYear() throws Exception{

        InternetUseRow row1 = new InternetUseRow(2011, new Device("DIS001", "스마트폰"), 55.23);
        InternetUseRow row2 = new InternetUseRow(2012, new Device("DIS002", "데스크탑"), 90.51);

        List<InternetUseRow> list = Arrays.asList(row1, row2);

        given(statService.getTopDeviceEachYear()).willReturn(new InternetUseRowList(list));

        mvc.perform(get("/api/topDeviceEachYear").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.device[0].device_id").value("DIS001"))
                .andExpect(jsonPath("$.device[1].device_id").value("DIS002"))
                ;
    }

}
