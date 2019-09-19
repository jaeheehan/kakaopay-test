package com.kakaopay.internet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InternetUseRow {

    private Integer year;

    String device_id;

    String device_name;

    private Double rate;

    public InternetUseRow(Integer year, Device device, Double rate){
        this.year = year;
        this.device_id = device.getDevice_id();
        this.device_name = device.getDevice_name();
        this.rate = rate;
    }

    public InternetUseRow(Internet internet){
        this.year = internet.getInternetPK().getYear();
        this.device_id = internet.getInternetPK().getDevice().getDevice_id();
        this.device_name = internet.getInternetPK().getDevice().getDevice_id();
        this.rate = internet.getRate();
    }
}
