package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MemberServiceImpl implements MemberService {

    PasswordEncoder passwordEncoder;

    MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signUp(String id, String pw) {
        Member member = new Member(id, passwordEncoder.encode(pw));
        memberRepository.save(member);
    }

    @Override
    public Member loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findById(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username : " + username));

        return member;

    }
}
