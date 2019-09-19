package com.kakaopay.internet.controller;

import com.kakaopay.internet.auth.CustomAuthenticationProvider;
import com.kakaopay.internet.auth.JwtAuthenticationEntryPoint;
import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.DeviceList;
import com.kakaopay.internet.domain.InternetUseRow;
import com.kakaopay.internet.domain.InternetUseRowList;
import com.kakaopay.internet.service.MemberService;
import com.kakaopay.internet.service.StatService;
import com.kakaopay.internet.util.JwtTokenUtil;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatController.class)
@RolesAllowed(value = {"ADMIN"})
public class StatControllerTest {

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private CustomAuthenticationProvider customAuthenticationProvider;

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
