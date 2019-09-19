package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberService {

    public void signUp(String id, String pw);

    public Member loadUserByUsername(String id) throws UsernameNotFoundException;
}
