package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;
import com.kakaopay.internet.domain.InternetPK;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Comparator.comparing;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Transactional
@Rollback
public class InternetRepositoryTest {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    InternetRepository internetRepository;

    @Before
    public void setup(){

        Device d1 = new Device("TEST001", "스마트폰");
        Device d2 = new Device("TEST002", "데스크탑");

        deviceRepository.save(d1);
        deviceRepository.save(d2);

        InternetPK p1 = new InternetPK(2001, d1);
        InternetPK p2 = new InternetPK(2001, d2);
        InternetPK p3 = new InternetPK(2002, d1);
        InternetPK p4 = new InternetPK(2002, d2);

        internetRepository.save(new Internet(p3, 11.2));
        internetRepository.save(new Internet(p1, 33.22));
        internetRepository.save(new Internet(p2, 4.23));
        internetRepository.save(new Internet(p4, 55.0));

    }


    @Test
    public void insertInternetTest(){

        Device testDevice = new Device("TEST001", "스마트폰");

        Internet null_internet = new Internet(new InternetPK(0, null), 0.0);

        assertEquals(Integer.valueOf(2001), internetRepository.findById(
                new InternetPK(2001, testDevice)).orElse(null_internet).getInternetPK().getYear());
        assertNotEquals(Integer.valueOf(2001), internetRepository.findById(
                new InternetPK(2002, testDevice)).orElse(null_internet).getInternetPK().getYear());

    }

    @Test
    public void getTopDeviceEachYear(){

        List<Internet> list = internetRepository.findTopYearDevice();

        Internet null_internet = new Internet(new InternetPK(0, new Device(null, null)), 0.0);

        assertEquals(2, list.stream().filter(m -> m.getInternetPK().getYear() < 2010).count());
        assertEquals("스마트폰" , list.stream().filter(m -> m.getInternetPK().getYear() == 2001)
                .findFirst().orElse(null_internet).getInternetPK().getDevice().getDevice_name());
        assertNotSame("데스크탑" , list.stream().filter(m -> m.getInternetPK().getYear() == 2001)
                .findFirst().orElse(null_internet).getInternetPK().getDevice().getDevice_name());
    }

    @Test
    public void internetUseTopByYear(){

        List<Internet> list1 = internetRepository.findTop1ByInternetPKYearOrderByRateDesc(2001);
        List<Internet> list2 = internetRepository.findTop1ByInternetPKYearOrderByRateDesc(2002);

        assertEquals("TEST001", list1.get(0).getInternetPK().getDevice().getDevice_id());
        assertEquals("TEST002", list2.get(0).getInternetPK().getDevice().getDevice_id());
    }

    @Test
    public void internetUseYearTopByDevice(){

        Device device1 = new Device("TEST001");
        Device device2 = new Device("TEST002");

        List<Internet> list1 =  internetRepository.findTop1ByInternetPKDeviceOrderByRateDesc(device1);
        List<Internet> list2 =  internetRepository.findTop1ByInternetPKDeviceOrderByRateDesc(device2);

        assertNotNull(list1);
        assertNotNull(list2);

        assertEquals(Integer.valueOf(2001), list1.get(0).getInternetPK().getYear());
        assertEquals(Integer.valueOf(2002), list2.get(0).getInternetPK().getYear());

    }

    @Test
    public void findByInternetPKDevice(){

        Device device = new Device("TEST001");

        List<Internet> list = internetRepository.findByInternetPKDevice(device);


        double[] rates = list.stream().sorted(comparing(a -> a.getInternetPK().getYear()))
                .mapToDouble(Internet::getRate).toArray();

        assertNotNull(rates);
        assertEquals(2, rates.length);
        assertEquals(33.22, rates[0], 0);
        assertEquals(11.2, rates[rates.length - 1], 0);

    }

}
