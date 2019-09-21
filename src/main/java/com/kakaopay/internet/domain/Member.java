package com.kakaopay.internet.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.beans.ConstructorProperties;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Member {

    @Id
    private String username;

    private String password;

    @ConstructorProperties({"username", "password"})
    public Member(String username, String password){
        this.username = username;
        this.password = password;
    }
}
