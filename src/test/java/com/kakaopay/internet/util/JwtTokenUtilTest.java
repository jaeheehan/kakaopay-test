package com.kakaopay.internet.util;

import com.kakaopay.internet.domain.Member;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class JwtTokenUtilTest {

    @Test
    public void generateTokenTest(){

        Member m1 = new Member("test", "pw");
        JwtTokenUtil jwt = new JwtTokenUtil("test", "refresh");
        String token = jwt.generateToken(m1);

        assertThat(jwt.getUsernameFromToken(true, token)).isEqualTo("test");
        assertThat(jwt.getExpirationDateFromToken(true, token)).isInTheFuture();
    }

    @Test
    public void generateRefreshTokenTest(){

        Member m1 = new Member("test", "pw");
        JwtTokenUtil jwt = new JwtTokenUtil("test", "refresh");
        String token = jwt.generateRefreshToken(m1);

        assertThat(jwt.getUsernameFromToken(false, token)).isEqualTo("test");
        assertThat(jwt.getExpirationDateFromToken(false, token)).isInTheFuture();
    }
}
