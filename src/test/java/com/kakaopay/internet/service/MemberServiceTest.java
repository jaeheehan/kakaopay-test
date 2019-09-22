package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.domain.Token;
import com.kakaopay.internet.repository.MemberRepository;
import com.kakaopay.internet.util.JwtTokenUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MemberServiceTest {

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        memberService = new MemberServiceImpl(memberRepository, passwordEncoder, jwtTokenUtil);
    }

    @Test
    public void signUpTest(){

        Member member = new Member("test", "pw");

        when(passwordEncoder.encode("pw")).thenReturn("pw");
        when(memberRepository.save(member)).thenReturn(member);
        when(jwtTokenUtil.generateToken(member)).thenReturn("access");
        when(jwtTokenUtil.generateRefreshToken(member)).thenReturn("refresh");

        Token result = memberService.signUp(member);

        assertThat(result.getAccess_token()).isEqualTo("access");
        assertThat(result.getRefresh_token()).isEqualTo("refresh");

    }

    @Test
    public void signInTest(){

        Member member = new Member("id", "pw");

        when(memberRepository.findById("id")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("pw", "pw")).thenReturn(true);

        when(jwtTokenUtil.generateToken(member)).thenReturn("access");
        when(jwtTokenUtil.generateRefreshToken(member)).thenReturn("refresh");

        Token result = memberService.signIn(member);

        assertThat(result.getAccess_token()).isEqualTo("access");
        assertThat(result.getRefresh_token()).isEqualTo("refresh");
    }

    @Test
    public void refreshTest(){

        Member member = new Member("username", "pw");

        when(jwtTokenUtil.getUsernameFromToken(false, "test")).thenReturn("username");
        when(memberRepository.findById("username")).thenReturn(Optional.of(member));
        when(jwtTokenUtil.generateToken(member)).thenReturn("access");
        when(jwtTokenUtil.generateRefreshToken(member)).thenReturn("refresh");

        Token result = memberService.refreshToken("test");

        assertThat(result.getAccess_token()).isEqualTo("access");
        assertThat(result.getRefresh_token()).isEqualTo("refresh");

    }
}
