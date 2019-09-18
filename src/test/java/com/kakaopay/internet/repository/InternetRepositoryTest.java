package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Rollback
public class InternetRepositoryTest {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    InternetRepository internetRepository;

    @Test
    public void insertInternetTest(){

        Device d1 = new Device("test1", "test1");
        deviceRepository.save(d1);

        Internet i1 = new Internet();
        i1.setYear(2011);
        i1.setDevice(d1);
        i1.setRate(22.22);

        Internet i2 = new Internet();
        i2.setYear(2012);
        i2.setDevice(d1);
        i2.setRate(33.00);

        internetRepository.save(i1);
        internetRepository.save(i2);

        assertEquals(2, internetRepository.count());
        assertEquals(Integer.valueOf(2011), internetRepository.findInternetsByYear(2011).get(0).getYear());



    }

}
