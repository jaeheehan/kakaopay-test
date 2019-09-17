package com.kakaopay.internet.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Device {

    @Id
    String device_id;

    String device_name;
}
