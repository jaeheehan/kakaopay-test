package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatController {

    private StatService statService;

    @Autowired
    public StatController(StatService statService){
        this.statService= statService;
    }

    @GetMapping(path = "/devices")
    public ResponseEntity getDevices(){
        return ResponseEntity.ok(statService.getDeviceList());
    }

}
