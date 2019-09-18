package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;
import com.kakaopay.internet.domain.InternetPK;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Transactional
public class InternetRepositoryTest {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    InternetRepository internetRepository;

    @Test
    @Rollback
    public void insertInternetTest(){

        Device d1 = new Device("test1", "test1");
        deviceRepository.save(d1);

        InternetPK p1 = new InternetPK(2011, d1);
        InternetPK p2 = new InternetPK(2012, d1);

        Internet i1 = new Internet();
        i1.setInternetPK(p1);
        i1.setRate(33.22);

        Internet i2 = new Internet();
        i2.setInternetPK(p2);
        i2.setRate(44.32);

        internetRepository.save(i1);
        internetRepository.save(i2);

        assertEquals(Integer.valueOf(2011), internetRepository.findById(p1).get().getInternetPK().getYear());
        assertNotSame(Integer.valueOf(2011), internetRepository.findById(p2).get().getInternetPK().getYear());

    }

}
