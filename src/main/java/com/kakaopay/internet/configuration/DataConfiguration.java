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

        // 파일 가져오기
        List<String> list = FileUtil.readFile("classpath:2019.csv");

        // 디바이스 등록
        List<Device> devices = configService.registerDevices(list.get(0));

        // 데이터 등록
        list.stream().skip(1).map(word -> word.split(",")).forEach(data -> {
            configService.saveUseData(devices, data);
        });

    }



}
