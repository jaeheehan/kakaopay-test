package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.service.MemberService;
import com.kakaopay.internet.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/auth/signUp", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Member member) throws Exception {

        String pw = passwordEncoder.encode(member.getPassword());
        memberService.signUp(member.getUsername(), pw);

        String token = jwtTokenUtil.generateToken(member);

        return ResponseEntity.ok(token);
    }

}

