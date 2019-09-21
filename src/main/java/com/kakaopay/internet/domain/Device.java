package com.kakaopay.internet.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Device {

    @Id
    @Column(name = "DEVICE_ID")
    String device_id;

    String device_name;

    public Device(String device_id){
        this.device_id = device_id;
    }
}
