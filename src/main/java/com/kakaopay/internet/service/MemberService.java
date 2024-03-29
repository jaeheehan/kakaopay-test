package com.kakaopay.internet.service;

import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.domain.Token;

public interface MemberService {

    public Token signUp(Member member);

    public Token signIn(Member member);

    public Token refreshToken(String refresh_token);
}
