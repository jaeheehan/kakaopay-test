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

import java.security.InvalidParameterException;

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

        if (member.getUsername() == null || member.getUsername().trim().isEmpty()){
            throw new InvalidParameterException("Invalid Username");
        }

        if (member.getPassword() == null || member.getPassword().trim().isEmpty()){
            throw new InvalidParameterException("Invalid Password");
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

        return generateToke(member);
    }

    @Override
    public Token signIn(Member login) {

        Member member = memberRepository.findById(login.getUsername()).orElseThrow(()
                -> new UsernameNotFoundException("User not Exist"));

        if(!passwordEncoder.matches(login.getPassword(), member.getPassword())){
            throw new BadCredentialsException("Password Invalid");
        }

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
