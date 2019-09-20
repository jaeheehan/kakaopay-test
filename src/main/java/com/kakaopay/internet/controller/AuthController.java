package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.domain.Token;
import com.kakaopay.internet.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@CrossOrigin
public class AuthController {

    private AuthenticationManager authenticationManager;

    private MemberService memberService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, MemberService memberService){
        this.authenticationManager = authenticationManager;
        this.memberService = memberService;
    }

    @ApiOperation(value = "가입하기")
    @RequestMapping(value = "/auth/signUp", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.signUp(member));
    }

    @ApiOperation(value = "로그인")
    @RequestMapping(value = "/auth/signIn", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody Member member) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword()));
        return ResponseEntity.ok(memberService.signIn(member));
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "토큰갱신")
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        String refresh_token = Optional.ofNullable(request.getAttribute("refresh_token"))
                .orElse(null).toString();

        return ResponseEntity.ok(memberService.refreshToken(refresh_token));
    }



}

