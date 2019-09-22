package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.domain.Token;
import com.kakaopay.internet.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private MemberService memberService;

    @Autowired
    public AuthController(MemberService memberService){
        this.memberService = memberService;
    }

    @ApiOperation(value = "가입하기")
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<Token> signUp(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.signUp(member));
    }

    @ApiOperation(value = "로그인")
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ResponseEntity<Token> signIn(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.signIn(member));
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "토큰갱신")
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<Token> refreshToken(HttpServletRequest request) {

        String refresh_token = Optional.ofNullable(request.getAttribute("refresh_token"))
                .orElse("").toString();

        return ResponseEntity.ok(memberService.refreshToken(refresh_token));
    }



}

