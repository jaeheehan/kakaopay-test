package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Internet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InternetRepository extends CrudRepository<Internet, Integer> {

    public List<Internet> findInternetsByYear(int year);
}
