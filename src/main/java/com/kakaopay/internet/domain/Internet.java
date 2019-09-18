package com.kakaopay.internet.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Internet {

    @Id
    @GeneratedValue
    @Column(name = "SEQ")
    private Long seq;

    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_ID")
    private Device device;

    private Double rate;
}
