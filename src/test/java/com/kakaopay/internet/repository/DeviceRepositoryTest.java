package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Device;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Transactional
public class DeviceRepositoryTest {

    @Autowired
    DeviceRepository deviceRepository;

    @Test
    @Rollback
    public void insertTest(){
        IntStream.range(1, 10).forEach(i -> {
            Device device = new Device("DIS00" + i, "test_" + i);
            deviceRepository.save(device);
        });

        assertEquals(9, deviceRepository.count());
    }

}
