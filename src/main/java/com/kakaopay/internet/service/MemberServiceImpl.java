package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.domain.Token;
import com.kakaopay.internet.repository.MemberRepository;
import com.kakaopay.internet.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                             PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Token signUp(Member member) {
        Member user = new Member(member.getUsername(), passwordEncoder.encode(member.getPassword()));
        memberRepository.save(user);
        return generateToke(user);
    }

    @Override
    public Token signIn(Member member) {
        return generateToke(member);
    }

    @Override
    public Token refreshToken(String refresh_token) {

        String username = jwtTokenUtil.getUsernameFromToken(false, refresh_token);

        Member member = memberRepository.findById(username).orElseThrow(()
                -> new UsernameNotFoundException("User not Exist"));

        return generateToke(member);
    }

    private Token generateToke(Member member){

        String access_token = jwtTokenUtil.generateToken(member);
        String refresh_token = jwtTokenUtil.generateRefreshToken(member);

        return new Token(access_token, refresh_token);
    }
}
