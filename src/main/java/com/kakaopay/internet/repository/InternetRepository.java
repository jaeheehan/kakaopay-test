package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Internet;
import com.kakaopay.internet.domain.InternetPK;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InternetRepository extends CrudRepository<Internet, InternetPK> {
}
