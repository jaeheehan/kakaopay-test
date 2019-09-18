package com.kakaopay.internet.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Internet {

    @EmbeddedId
    private InternetPK internetPK;

    private Double rate;
}
