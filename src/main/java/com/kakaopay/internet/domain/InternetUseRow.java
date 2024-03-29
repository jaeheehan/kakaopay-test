package com.kakaopay.internet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InternetUseRow {

    private Integer year;

    String device_name;

    private Double rate;

    public InternetUseRow(Integer year, Device device, Double rate){
        this.year = year;
        this.device_name = device.getDevice_name();
        this.rate = rate;
    }

    public InternetUseRow(Internet internet){
        this.year = internet.getInternetPK().getYear();
        this.device_name = internet.getInternetPK().getDevice().getDevice_name();
        this.rate = internet.getRate();
    }
}
