package com.kakaopay.internet.configuration;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.service.ConfigService;
import com.kakaopay.internet.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

@Configuration
@Slf4j
public class DataConfiguration {

    @Autowired
    ConfigService configService;

    @Bean
    public void setData(){

        List<String> list = FileUtil.readFile(getClass().getClassLoader().getResource("2019.csv").getPath());
        list.stream().forEach(log::info);

        List<Device> devices = configService.registerDevices(list.get(0));
        devices.stream().map(d -> d.getDevice_name()).forEach(log::info);

        list.stream().skip(1).map(word -> word.split(",")).forEach(data -> {
            configService.saveUseData(devices, data);
        });

    }



}
