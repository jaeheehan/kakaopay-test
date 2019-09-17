package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Device;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Rollback
public class DeviceRepositoryTest {

    @Autowired
    DeviceRepository deviceRepository;

    @Test
    public void insertTest(){
        IntStream.range(1, 10).forEach(i -> {
            Device device = new Device();
            device.setDevice_id("device_" + i);
            device.setDevice_name("test_" + i);

            deviceRepository.save(device);

        });
    }

}
