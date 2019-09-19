package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.DeviceList;
import com.kakaopay.internet.service.StatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatController.class)
public class StatControllerTest {

    @MockBean
    private StatService statService;

    @Autowired
    private MockMvc mvc;

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

}
