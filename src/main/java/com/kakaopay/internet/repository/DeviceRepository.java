package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Device;
import org.springframework.data.repository.CrudRepository;

public interface DeviceRepository extends CrudRepository<Device, String> {
}
