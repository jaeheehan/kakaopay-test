package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.Internet;
import com.kakaopay.internet.domain.InternetPK;
import com.kakaopay.internet.domain.InternetUseRow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InternetRepository extends CrudRepository<Internet, InternetPK> {

    @Query(value = "select year, i.device_id, rate \n" +
            " from internet i inner join device d on d.device_id = i.device_id\n" +
            "where  0 = (select count(*) from internet where year = i.year and rate > i.rate) " , nativeQuery = true)
    List<Internet> findTopYearDevice();

    List<Internet> findTop1ByInternetPKYearOrderByRateDesc(Integer year);

    List<Internet> findTop1ByInternetPKDeviceOrderByRateDesc(Device device);

}
