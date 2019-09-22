package com.kakaopay.internet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.internet.domain.Member;
import com.kakaopay.internet.domain.Token;
import com.kakaopay.internet.service.MemberService;
import io.jsonwebtoken.JwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MemberService memberService;

    private MockMvc mvc;

    @Before
    public void setUp(){
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Configuration
    @EnableWebMvc
    public static class TestConfiguration {

        @MockBean
        private MemberService memberService;

        @Bean
        public AuthController AuthController() {
            return new AuthController(memberService);
        }

        @Bean
        public MemberService memberService(){
            return memberService;
        }

    }

    @Test
    public void signUpTest() throws  Exception{

        Member member = new Member("test", "1234");

        given(memberService.signUp(member)).willReturn(new Token("access", "refresh"));

        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(post("/auth/signUp").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(member)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access"))
                .andExpect(jsonPath("$.refresh_token").value("refresh"));
    }


    @Test
    public void signUpTest2() {

        Member member = new Member("", "1234");
        ObjectMapper mapper = new ObjectMapper();

        try {
            mvc.perform(post("/auth/signUp").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(member)));
        }catch (Exception e){
            e.printStackTrace();
            assertThat(e).hasCauseInstanceOf(InvalidParameterException.class);
        }

        member = new Member("test", " ");

        try {
            mvc.perform(post("/auth/signUp").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(member)));
        }catch (Exception e){
            e.printStackTrace();
            assertThat(e).hasCauseInstanceOf(InvalidParameterException.class);
        }

    }

    @Test
    public void signInTest() throws Exception{

        Member member = new Member("test", "1234");

        given(memberService.signIn(member)).willReturn(new Token("access_signIn", "refresh_signIn"));
        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(post("/auth/signIn").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(member)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access_signIn"))
                .andExpect(jsonPath("$.refresh_token").value("refresh_signIn"));

    }

    @Test
    public void signInTest2() {
        // Username Not Found
        Member member = new Member("test1", "1234");
        ObjectMapper mapper = new ObjectMapper();

        try{
            mvc.perform(post("/auth/signIn").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(member)));
        }catch (Exception e){
            e.printStackTrace();
            assertThat(e).hasCauseInstanceOf(UsernameNotFoundException.class);
        }

    }

    @Test
    public void signInTest3() {
        // Password Wrong
        Member member = new Member("test", "12345");
        ObjectMapper mapper = new ObjectMapper();

        try{
            mvc.perform(post("/auth/signIn").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(member)));
        }catch (Exception e){
            e.printStackTrace();
            assertThat(e).hasCauseInstanceOf(BadCredentialsException.class);
        }

    }

    @Test
    public void refreshTest() throws Exception {

        given(memberService.refreshToken("")).willReturn(new Token("access", "refresh"));

        mvc.perform(post("/auth/refresh").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access"))
                .andExpect(jsonPath("$.refresh_token").value("refresh"));


        given(memberService.refreshToken("")).willThrow(new JwtException("Invalid Token"));

        try {
            mvc.perform(post("/auth/refresh").accept(MediaType.APPLICATION_JSON));
        } catch (Exception e){
            e.printStackTrace();
            assertThat(e).hasCauseInstanceOf(JwtException.class);
        }
    }

}
