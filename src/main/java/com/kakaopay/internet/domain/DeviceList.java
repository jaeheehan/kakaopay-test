package com.kakaopay.internet.domain;

import jdk.jfr.DataAmount;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class DeviceList {

    List<Device> devices;
}
